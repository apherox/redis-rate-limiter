package com.ratelimiter.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

public class User {

    @Getter
    private int userId;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String fullName;

    @Getter
    @Setter
    private Timestamp createdAt;

    @Getter
    @Setter
    private Timestamp updatedAt;

    @Getter
    @Setter
    private Timestamp lastLogin;
}