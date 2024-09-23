package com.ratelimiter.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

public class RequestLog {

    @Getter
    private int id;

    @Getter
    @Setter
    private String requestMethod;

    @Getter
    @Setter
    private String requestUrl;

    @Getter
    @Setter
    private String remoteAddress;

    @Getter
    @Setter
    private int responseCode;

    @Getter
    @Setter
    private String userAgent;

    @Getter
    @Setter
    private Timestamp currentTime;
}
