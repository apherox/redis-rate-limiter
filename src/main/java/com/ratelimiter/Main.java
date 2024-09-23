package com.ratelimiter;

import com.ratelimiter.config.ConfigLoader;
import com.ratelimiter.config.FlywayConfigurer;
import com.ratelimiter.server.JettyServer;

public class Main {

    public static void main(String[] args) throws Exception {
        FlywayConfigurer.configureFlyway();
        JettyServer.runServer(ConfigLoader.getServerPort());
    }

}
