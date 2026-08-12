<%-- 
 * Purpose: Invalidate patient/staff session and redirect to login page.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Hủy bỏ phiên làm việc (Session) của người dùng hiện tại
    session.invalidate(); // Gọi hàm invalidate của Servlet Session API
    
    // Chuyển hướng người dùng về trang đăng nhập
    response.sendRedirect(request.getContextPath() + "/login.jsp"); // Gọi hàm chuyển hướng từ HttpServletResponse
%>
