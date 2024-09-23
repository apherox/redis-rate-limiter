package com.ratelimiter.config;

import com.moandjiezana.toml.Toml;

import java.io.File;

public class ConfigLoader {

    private ConfigLoader() {}

    private static final Toml config;

    static {
        config = new Toml().read(new File("src/main/resources/config.toml"));
    }

    public static String getDatabaseUrl() {
        return config.getString("database.url");
    }

    public static String getDatabaseDriver() {
        return config.getString("database.driver");
    }

    public static int getServerPort() {
        return config.getLong("server.port").intValue();
    }

    public static String getRedisHost() {
        return config.getString("redis.host");
    }

    public static int getRedisPort() {
        return config.getLong("redis.port").intValue();
    }
}
