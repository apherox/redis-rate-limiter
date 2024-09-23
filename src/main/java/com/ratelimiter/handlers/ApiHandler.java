package com.ratelimiter.handlers;

import com.ratelimiter.server.JettyServer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class ApiHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html");

        Context ctx = new Context();
        PrintWriter writer = resp.getWriter();
        JettyServer.getTemplateEngine().process("api", ctx, writer);
    }

}