package com.ratelimiter.handlers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LogoutHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Invalidate the session
        request.getSession().invalidate();

        // Redirect to login page
        response.sendRedirect("/login");
    }
}
