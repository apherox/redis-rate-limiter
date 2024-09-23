package com.ratelimiter.service;

import com.ratelimiter.dao.RequestLogMapper;
import com.ratelimiter.model.RequestLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@RequiredArgsConstructor
public class RequestLogService {

    private final RequestLogMapper requestLogMapper;
    private final SqlSession session;

    public void logRequest(HttpServletRequest request, HttpServletResponse response) {
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestMethod(request.getMethod());
        requestLog.setRequestUrl(request.getRequestURI());
        requestLog.setRemoteAddress(request.getRemoteAddr());
        requestLog.setUserAgent(request.getHeader("USER-AGENT"));
        requestLog.setResponseCode(response.getStatus());

        requestLogMapper.insertRequestLog(requestLog);
        session.commit();
    }

    public RequestLog getRequestLog(int id) {
        return requestLogMapper.getRequestLogById(id);
    }

    public List<RequestLog> getAllRequestLogs() {
        return requestLogMapper.getAllRequestLogs();
    }
}
