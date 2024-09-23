package com.ratelimiter.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisConfig {

    private static JedisPool jedisPool;

    static {
        // Jedis Pool configuration
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(16);
        poolConfig.setMinIdle(4);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        // Initialize the Jedis pool with Redis server details
        jedisPool = new JedisPool(poolConfig, ConfigLoader.getRedisHost(), ConfigLoader.getRedisPort()); // Use your Redis server's details here
    }

    private JedisConfig() {}

    // Get Jedis instance
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    // Close the Jedis pool
    public static void closePool() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}
