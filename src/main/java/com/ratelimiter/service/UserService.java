package com.ratelimiter.service;

import com.ratelimiter.dao.UserMapper;
import com.ratelimiter.model.User;
import com.ratelimiter.util.PasswordUtil;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RequiredArgsConstructor
public class UserService implements Serializable {

    private final UserMapper userMapper;

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public boolean validateUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        User user = getUserByUsername(username);
        return user != null && PasswordUtil.validatePassword(password, user.getPassword());
    }
}
