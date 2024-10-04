package com.ratelimiter.config;

import com.moandjiezana.toml.Toml;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

@Slf4j
public class ConfigLoader {

    private ConfigLoader() {}

    private static Toml config;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (var reader = Optional.ofNullable(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.toml"))
                .map(InputStreamReader::new)
                .orElseThrow(() -> new IOException("config.toml not found in classpath"))) {
            config = new Toml().read(reader);
        } catch (IOException e) {
            log.error("Error loading config.toml", e);
            config = new Toml();
        }
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
