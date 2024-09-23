package com.ratelimiter.dao;

import com.ratelimiter.model.RequestLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

public interface RequestLogMapper extends Serializable {

    @Insert("INSERT INTO request_log (request_method, request_uri, remote_address, response_code, user_agent, current_time) " +
            "VALUES (#{requestMethod}, #{requestUrl}, #{remoteAddress}, #{responseCode}, #{userAgent}, CURRENT_TIMESTAMP)")
    void insertRequestLog(RequestLog requestLog);

    @Select("SELECT * FROM request_log WHERE id = #{id}")
    RequestLog getRequestLogById(int id);

    @Select("SELECT * FROM request_log")
    List<RequestLog> getAllRequestLogs();
}
