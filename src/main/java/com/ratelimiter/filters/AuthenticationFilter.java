package com.ratelimiter.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Cast to HttpServletRequest and HttpServletResponse to access session and status codes.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get the session (don't create a new one if it doesn't exist).
        HttpSession session = httpRequest.getSession(false);

        // Get the requested URI.
        String requestURI = httpRequest.getRequestURI();

        // Allow access to login page and static resources.
        if (requestURI.equals("/login") || requestURI.equals("/login.html") || requestURI.startsWith("/static/")) {
            chain.doFilter(request, response); // Continue to the next filter or resource.
            return;
        }

        // Check if user is authenticated.
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (loggedIn) {
            // User is authenticated, proceed to requested resource.
            chain.doFilter(request, response);
        } else {
            // User is not authenticated, redirect to login page.
            httpResponse.sendRedirect("/login");
        }
    }

    /*
            HttpSession session = httpRequest.getSession(false);
    String loginURI = httpRequest.getContextPath() + "/login";

    boolean loggedIn = (session != null && session.getAttribute("user") != null);
    boolean loginRequest = httpRequest.getRequestURI().equals(loginURI);
    boolean rootRequest = httpRequest.getRequestURI().equals(httpRequest.getContextPath())
                           || httpRequest.getRequestURI().equals("/");

    if (loggedIn || loginRequest || rootRequest) {
        chain.doFilter(request, response);
    } else {
        httpResponse.sendRedirect(loginURI);
    }
     */
    @Override
    public void destroy() {
        // Cleanup logic if needed.
    }
}
