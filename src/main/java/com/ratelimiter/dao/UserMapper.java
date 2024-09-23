package com.ratelimiter.dao;

import com.ratelimiter.model.User;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.List;

public interface UserMapper extends Serializable {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUserByUsername(String username);

    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    User getUserById(int userId);

    @Select("SELECT * FROM users")
    List<User> getAllUsers();

    @Insert("INSERT INTO users (username, password, email, full_name, created_at, updated_at, last_login) " +
            "VALUES (#{username}, #{password}, #{email}, #{fullName}, #{createdAt}, #{updatedAt}, #{lastLogin})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insertUser(User user);

    @Update("UPDATE users SET password = #{password}, email = #{email}, full_name = #{fullName}, " +
            "updated_at = #{updatedAt}, last_login = #{lastLogin} WHERE user_id = #{userId}")
    void updateUser(User user);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    void deleteUser(int userId);
}
