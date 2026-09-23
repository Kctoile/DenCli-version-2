package com.devjava.dencli.filter;

import com.devjava.dencli.util.CsrfUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CsrfFilterTest {

    private CsrfFilter csrfFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        csrfFilter = new CsrfFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        session = mock(HttpSession.class);

        when(request.getSession(true)).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/DenCli");
    }

    @Test
    void testSafeMethodAllowsThrough() throws Exception {
        when(request.getMethod()).thenReturn("GET");

        csrfFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testExemptPathAllowsThrough() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/DenCli/login");

        csrfFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testPostWithoutTokenBlocked() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/DenCli/doctor/examination");
        when(session.getAttribute(CsrfUtil.CSRF_SESSION_ATTR)).thenReturn("secret-csrf-token");
        when(request.getHeader(CsrfUtil.CSRF_HEADER)).thenReturn(null);
        when(request.getParameter(CsrfUtil.CSRF_PARAM)).thenReturn(null);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        csrfFilter.doFilter(request, response, chain);

        verify(chain, never()).doFilter(request, response);
        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    }

    @Test
    void testPostWithValidHeaderTokenAllowed() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/DenCli/doctor/examination");
        when(session.getAttribute(CsrfUtil.CSRF_SESSION_ATTR)).thenReturn("valid-token-123");
        when(request.getHeader(CsrfUtil.CSRF_HEADER)).thenReturn("valid-token-123");

        csrfFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testPostWithValidFormParamTokenAllowed() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/DenCli/staff/invoice");
        when(session.getAttribute(CsrfUtil.CSRF_SESSION_ATTR)).thenReturn("valid-token-456");
        when(request.getHeader(CsrfUtil.CSRF_HEADER)).thenReturn(null);
        when(request.getParameter(CsrfUtil.CSRF_PARAM)).thenReturn("valid-token-456");

        csrfFilter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }
}
