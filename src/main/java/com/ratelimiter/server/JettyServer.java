package com.ratelimiter.server;

import com.ratelimiter.filters.AuthenticationFilter;
import com.ratelimiter.filters.LoggingFilter;
import com.ratelimiter.handlers.*;
import jakarta.servlet.DispatcherType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.EnumSet;

@Slf4j
public class JettyServer {

    @Getter
    private static TemplateEngine templateEngine;

    private JettyServer() {}

    public static void runServer(int port) throws Exception {

        configureThymeleaf();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        addFilters(context);
        addHandlers(context);

        configureAndStartServer(context, port);
    }

    private static void configureAndStartServer(ServletContextHandler context, int port) throws Exception {
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context, new DefaultHandler()});

        Server server = new Server(port);
        server.setHandler(handlers);
        server.setErrorHandler(new GlobalExceptionHandler());

        // Register a shutdown hook to stop the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                log.info("Stopping Jetty server...");
                server.stop();
                server.join();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }));

        server.start();
        server.join();
    }

    private static void configureThymeleaf() {
        // Configure the TemplateEngine
        templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        templateEngine.setTemplateResolver(resolver);
    }

    private static void addFilters(ServletContextHandler handler) {
        // Register the LoggingFilter for all paths
        handler.addFilter(new FilterHolder(new LoggingFilter()), "/*", null);

        // Register the AuthenticationFilter for all paths
        handler.addFilter(AuthenticationFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
    }

    private static void addHandlers(ServletContextHandler handler) {
        // Serve static files from /static
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("src/main/resources/static");
        resourceHandler.setDirectoriesListed(false);
        handler.insertHandler(resourceHandler);

        handler.addServlet(new ServletHolder(new LoginHandler()), "/login");
        handler.addServlet(new ServletHolder(new ApiHandler()), "/api/*");
        handler.addServlet(WelcomeHandler.class, "/welcome");
        handler.addServlet(LogoutHandler.class, "/logout");
    }
}
