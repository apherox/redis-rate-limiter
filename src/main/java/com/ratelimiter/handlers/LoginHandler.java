package com.ratelimiter.handlers;

import com.ratelimiter.algorithms.login.LoginRateLimiter;
import com.ratelimiter.algorithms.login.LoginRateLimiterFactory;
import com.ratelimiter.config.JedisConfig;
import com.ratelimiter.dao.UserMapper;
import com.ratelimiter.server.JettyServer;
import com.ratelimiter.service.UserService;
import com.ratelimiter.util.MyBatisUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class LoginHandler extends HttpServlet {

    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String LOGIN = "/login";
    private UserService userService;
    private transient LoginRateLimiter<String> rateLimiter;

    @Override
    public void init() {
        SqlSession sqlSession = MyBatisUtil.getSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userService = new UserService(userMapper);
        rateLimiter = LoginRateLimiterFactory.createLoginRateLimiter();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Context ctx = new Context();

        String errorMessage = (String) request.getSession().getAttribute(ERROR_MESSAGE);
        if (errorMessage != null) {
            ctx.setVariable(ERROR_MESSAGE, errorMessage);
            request.getSession().removeAttribute(ERROR_MESSAGE);
        }

        PrintWriter writer = response.getWriter();
        JettyServer.getTemplateEngine().process("login", ctx, writer);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        if (username == null || password == null) {
            session.setAttribute(ERROR_MESSAGE, "Username and password are required.");
            response.sendRedirect(LOGIN);
            return;
        }

        // Check if the user is rate-limited
        if (rateLimiter.isRateLimited(username)) {
            session.setAttribute(ERROR_MESSAGE, "Too many login attempts. Please try again later.");
            response.sendRedirect(LOGIN);
            return;
        }

        try {
            if (userService.validateUser(username, password)) {

                rateLimiter.resetAttempts(username);

                // Invalidate any existing session
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }

                HttpSession newSession = request.getSession(true);
                newSession.setAttribute("user", username);
                newSession.setMaxInactiveInterval(15 * 60); // Session expires after 15 minutes

                // Set username attribute for rendering in welcome.html
                request.setAttribute("username", username);

                response.sendRedirect("/welcome");
            } else {
                rateLimiter.incrementFailedAttempts(username);
                session.setAttribute(ERROR_MESSAGE, "Invalid credentials");
                response.sendRedirect(LOGIN);
            }

        } catch (Exception e) {
            session.setAttribute(ERROR_MESSAGE, "An error occurred. Please try again.");
            response.sendRedirect(LOGIN);
        } finally {
            JedisConfig.closePool();
        }
    }

}
