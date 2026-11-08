package com.devjava.dencli.service;

import com.devjava.dencli.dao.AppointmentDAO;
import com.devjava.dencli.dao.ServiceDAO;
import com.devjava.dencli.model.Appointment;
import com.devjava.dencli.model.dto.BookingRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TDD Test Suite - Kiểm thử Concurrency & Race Condition khi đặt lịch hẹn.
 * Đảm bảo khi 2 bệnh nhân cùng đặt 1 bác sĩ tại cùng 1 khung giờ trong cùng 1
 * mili-giây,
 * chỉ DUY NHẤT 1 lịch hẹn thành công (ID > 0), lịch hẹn còn lại bị từ chối
 * (-1).
 */
@DisplayName("TDD Concurrency Test - Race Condition Booking Prevention")
@SuppressWarnings({"java:S2925", "squid:S2925"})
class AppointmentBookingConcurrencyTest {

    @Test
    @DisplayName("TC-CONCURRENCY-01: 2 Bệnh nhân cùng tranh chấp 1 slot giờ của 1 Bác sĩ đồng thời")
    void testConcurrentBookingSameDoctorSameSlot() throws InterruptedException, ExecutionException {
        // 1. Arrange: Mock DAO mô phỏng cơ chế khóa UPDLOCK, HOLDLOCK nguyên tử
        AppointmentDAO appointmentDAO = mock(AppointmentDAO.class);
        ServiceDAO serviceDAO = mock(ServiceDAO.class);
        AppointmentService appointmentService = new AppointmentService(appointmentDAO, serviceDAO);

        // Mô phỏng atomic slot acquisition: chỉ người đầu tiên giữ lock mới lấy được
        // slot thành công
        AtomicBoolean slotAcquired = new AtomicBoolean(false);
        AtomicInteger generatedIdCounter = new AtomicInteger(100);

        when(appointmentDAO.insertAppointmentWithServices(any(Appointment.class), anyList()))
                .thenAnswer(invocation -> {
                    // Giả lập độ trễ I/O 25ms trong transaction cơ sở dữ liệu
                    Thread.sleep(25); // NOSONAR: Giả lập độ trễ transaction trong kiểm thử đồng thời
                    if (slotAcquired.compareAndSet(false, true)) {
                        return generatedIdCounter.getAndIncrement(); // Người đầu tiên thành công
                    } else {
                        return -1; // Người thứ 2 bị xung đột slot trùng lịch
                    }
                });

        BookingRequestDTO request1 = new BookingRequestDTO();
        request1.setDoctorId(2);
        request1.setAppointmentDate("2026-11-20");
        request1.setAppointmentTime("09:00");
        request1.setServiceIds(List.of(1));

        BookingRequestDTO request2 = new BookingRequestDTO();
        request2.setDoctorId(2);
        request2.setAppointmentDate("2026-11-20");
        request2.setAppointmentTime("09:00");
        request2.setServiceIds(List.of(2));

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);

        // 2. Act: Kích hoạt 2 luồng cùng bắn request tại cùng một thời điểm chính xác
        Future<Integer> future1 = executor.submit(() -> {
            readyLatch.countDown();
            startLatch.await(); // Đợi hiệu lệnh nổ súng đồng thời
            return appointmentService.bookAppointment(request1, 101); // Bệnh nhân 101
        });

        Future<Integer> future2 = executor.submit(() -> {
            readyLatch.countDown();
            startLatch.await(); // Đợi hiệu lệnh nổ súng đồng thời
            return appointmentService.bookAppointment(request2, 102); // Bệnh nhân 102
        });

        readyLatch.await(5, TimeUnit.SECONDS);
        startLatch.countDown(); // Bắn 2 luồng đồng thời

        int result1 = future1.get();
        int result2 = future2.get();

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // 3. Assert: Phải có đúng 1 ca thành công (> 0) và đúng 1 ca thất bại (-1)
        List<Integer> results = List.of(result1, result2);
        long successCount = results.stream().filter(id -> id > 0).count();
        long failCount = results.stream().filter(id -> id == -1).count();

        assertEquals(1, successCount, "Bắt buộc chỉ có đúng 1 yêu cầu đặt lịch thành công");
        assertEquals(1, failCount, "Yêu cầu đến sau hoặc thua tranh chấp slot phải trả về -1");
        verify(appointmentDAO, times(2)).insertAppointmentWithServices(any(Appointment.class), anyList());
    }

    @Test
    @DisplayName("TC-CONCURRENCY-02: Stress test 10 luồng cùng tranh 1 slot giờ duy nhất")
    void testStressTenConcurrentUsersCompetingOneSlot() throws InterruptedException {
        AppointmentDAO appointmentDAO = mock(AppointmentDAO.class);
        ServiceDAO serviceDAO = mock(ServiceDAO.class);
        AppointmentService appointmentService = new AppointmentService(appointmentDAO, serviceDAO);

        AtomicBoolean slotLocked = new AtomicBoolean(false);
        when(appointmentDAO.insertAppointmentWithServices(any(Appointment.class), anyList()))
                .thenAnswer(invocation -> {
                    Thread.sleep(10); // NOSONAR: Giả lập độ trễ transaction trong kiểm thử đồng thời
                    return slotLocked.compareAndSet(false, true) ? 888 : -1;
                });

        int numUsers = 10;
        ExecutorService pool = Executors.newFixedThreadPool(numUsers);
        CountDownLatch readyLatch = new CountDownLatch(numUsers);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Integer> results = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < numUsers; i++) {
            final int userId = 1000 + i;
            pool.submit(() -> {
                try {
                    BookingRequestDTO req = new BookingRequestDTO();
                    req.setDoctorId(5);
                    req.setAppointmentDate("2026-12-01");
                    req.setAppointmentTime("14:00");
                    readyLatch.countDown();
                    startLatch.await();
                    int res = appointmentService.bookAppointment(req, userId);
                    results.add(res);
                } catch (Exception e) {
                    results.add(-999);
                }
            });
        }

        readyLatch.await(5, TimeUnit.SECONDS);
        startLatch.countDown(); // Bắn 10 user đồng thời

        pool.shutdown();
        assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS));

        long successes = results.stream().filter(r -> r == 888).count();
        long rejects = results.stream().filter(r -> r == -1).count();

        assertEquals(1, successes, "Trong 10 người chỉ có đúng 1 người được giữ chỗ");
        assertEquals(9, rejects, "9 người còn lại phải nhận thông báo từ chối do đã hết chỗ");
    }
}
