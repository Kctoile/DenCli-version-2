/**
 * DenCli - Real-time Appointment Calendar Synchronization
 * Đồng bộ trạng thái khung giờ khám thời gian thực qua Jakarta WebSocket 2.1
 */
(function() {
    'use strict';

    var socket = null;
    var reconnectTimeout = null;
    var currentTopic = null;

    function getContextPath() {
        return window.APP_CONTEXT_PATH || (window.location.pathname.startsWith('/DenCli') ? '/DenCli' : '');
    }

    function initRealtimeCalendar() {
        var doctorSelect = document.getElementById('doctorId');
        var dateInput = document.getElementById('appointmentDate');
        var timeSelect = document.getElementById('appointmentTime');
        var alertBox = document.getElementById('bookingAlert');
        var alertText = document.getElementById('bookingAlertText');

        if (!doctorSelect || !dateInput || !timeSelect) {
            return;
        }

        // Cache nhãn hiển thị ban đầu của các options
        Array.from(timeSelect.options).forEach(function(opt) {
            if (opt.value && !opt.dataset.originalText) {
                opt.dataset.originalText = opt.textContent;
            }
        });

        function resetAllOptions() {
            Array.from(timeSelect.options).forEach(function(opt) {
                if (opt.value) {
                    opt.disabled = false;
                    opt.style.color = '';
                    opt.style.textDecoration = '';
                    opt.textContent = opt.dataset.originalText || opt.textContent;
                }
            });
        }

        function markSlotBooked(slotTime) {
            var formatted = (slotTime && slotTime.length >= 5) ? slotTime.substring(0, 5) : slotTime;
            Array.from(timeSelect.options).forEach(function(opt) {
                if (opt.value === formatted || opt.value.startsWith(formatted)) {
                    opt.disabled = true;
                    opt.style.color = '#94A3B8';
                    opt.style.textDecoration = 'line-through';
                    if (!opt.textContent.includes('(Đã có người đặt)')) {
                        opt.textContent = (opt.dataset.originalText || opt.textContent) + ' (Đã có người đặt)';
                    }
                    if (timeSelect.value === opt.value) {
                        timeSelect.value = '';
                        if (alertBox && alertText) {
                            alertText.textContent = 'Khung giờ bạn vừa chọn đã được người khác đặt trước vài giây. Vui lòng chọn khung giờ khác.';
                            alertBox.classList.remove('d-none');
                            alertBox.classList.add('d-flex');
                            alertBox.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
                        }
                    }
                }
            });
        }

        function fetchBookedSlots(doctorId, date) {
            var url = getContextPath() + '/api/appointments/booked-slots?doctorId=' + encodeURIComponent(doctorId) + '&date=' + encodeURIComponent(date);
            fetch(url)
                .then(function(res) { return res.json(); })
                .then(function(data) {
                    resetAllOptions();
                    if (data.success && Array.isArray(data.bookedSlots)) {
                        data.bookedSlots.forEach(markSlotBooked);
                    }
                })
                .catch(function(err) {
                    console.warn('[Realtime Calendar] Không thể lấy danh sách slot đã đặt:', err);
                });
        }

        function connectWebSocket(doctorId, date) {
            var topic = doctorId + '_' + date;
            if (socket && currentTopic === topic && socket.readyState === WebSocket.OPEN) {
                return;
            }

            if (socket) {
                try { socket.close(); } catch (e) {}
                socket = null;
            }

            currentTopic = topic;
            clearTimeout(reconnectTimeout);

            var protocol = window.location.protocol === 'https:' ? 'wss://' : 'ws://';
            var wsUrl = protocol + window.location.host + getContextPath() + '/ws/appointment-calendar/' + encodeURIComponent(doctorId) + '/' + encodeURIComponent(date);

            try {
                socket = new WebSocket(wsUrl);

                socket.onopen = function() {
                    console.log('[Realtime Calendar] Đã kết nối kênh Doctor ' + doctorId + ' ngày ' + date);
                };

                socket.onmessage = function(event) {
                    try {
                        var data = JSON.parse(event.data);
                        if (data.event === 'SLOT_BOOKED') {
                            markSlotBooked(data.timeSlot);
                            if (typeof window.showToast === 'function') {
                                window.showToast('info', data.message || ('Khung giờ ' + data.timeSlot + ' vừa được đặt thành công!'));
                            }
                        }
                    } catch (e) {
                        console.error('[Realtime Calendar] Lỗi giải mã message:', e);
                    }
                };

                socket.onclose = function(e) {
                    console.log('[Realtime Calendar] Mất kết nối WebSocket:', e.code);
                    if (currentTopic === topic) {
                        reconnectTimeout = setTimeout(function() {
                            if (doctorSelect.value === doctorId && dateInput.value === date) {
                                connectWebSocket(doctorId, date);
                            }
                        }, 3000);
                    }
                };

                socket.onerror = function(err) {
                    console.warn('[Realtime Calendar] Lỗi WebSocket:', err);
                };
            } catch (err) {
                console.error('[Realtime Calendar] Lỗi khởi tạo WebSocket:', err);
            }
        }

        function syncCalendar() {
            var doctorId = doctorSelect.value;
            var date = dateInput.value;

            if (doctorId && date) {
                fetchBookedSlots(doctorId, date);
                connectWebSocket(doctorId, date);
            } else {
                resetAllOptions();
                if (socket) {
                    try { socket.close(); } catch (e) {}
                    socket = null;
                    currentTopic = null;
                }
            }
        }

        doctorSelect.addEventListener('change', syncCalendar);
        dateInput.addEventListener('change', syncCalendar);

        if (doctorSelect.value && dateInput.value) {
            syncCalendar();
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initRealtimeCalendar);
    } else {
        initRealtimeCalendar();
    }
})();
