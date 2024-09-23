package com.ratelimiter.filters;

import com.ratelimiter.dao.RequestLogMapper;
import com.ratelimiter.service.RequestLogService;
import com.ratelimiter.util.MyBatisUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

@Slf4j
public class LoggingFilter implements Filter {

    private RequestLogService requestLogService;

    @Override
    public void init(FilterConfig filterConfig) {
        SqlSession sqlSession = MyBatisUtil.getSession();
        RequestLogMapper requestLogMapper = sqlSession.getMapper(RequestLogMapper.class);
        requestLogService = new RequestLogService(requestLogMapper, sqlSession);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            String uri = httpRequest.getRequestURI();
            String method = httpRequest.getMethod();
            log.info("Received request: {} {} from {}", method, uri, request.getRemoteAddr());
            if (response instanceof HttpServletResponse httpServletResponse) {
                logRequest(httpRequest, httpServletResponse);
            }
        }

        chain.doFilter(request, response);
    }

    private void logRequest(HttpServletRequest req, HttpServletResponse resp) {
        requestLogService.logRequest(req, resp);
    }

    @Override
    public void destroy() {
    }
}
