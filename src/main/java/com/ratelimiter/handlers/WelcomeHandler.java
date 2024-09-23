package com.ratelimiter.handlers;

import com.ratelimiter.server.JettyServer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.PrintWriter;

public class WelcomeHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = (String) request.getSession().getAttribute("user");
        // Set the username as a request attribute for Thymeleaf to render
        request.setAttribute("username", username);

        Context ctx = new Context();
        ctx.setVariable("username", username);
        PrintWriter writer = response.getWriter();
        JettyServer.getTemplateEngine().process("welcome", ctx, writer);
    }
}
