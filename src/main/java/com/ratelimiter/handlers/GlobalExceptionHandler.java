package com.ratelimiter.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

import java.io.IOException;
import java.io.Writer;

public class GlobalExceptionHandler extends ErrorHandler {

    @Override
    protected void handleErrorPage(HttpServletRequest request, Writer writer, int code, String message) throws IOException {
        writer.write("<html><head><title>Error</title></head><body>");
        writer.write("<h2>Error Details</h2>");
        writer.write("<p>Status Code: " + code + "</p>");
        writer.write("<p>Message: " + message + "</p>");
        writer.write("<p>Request URI: " + request.getRequestURI() + "</p>");
        writer.write("</body></html>");
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Custom handling of errors can be added here if needed
        super.handle(target, baseRequest, request, response);
    }
}
