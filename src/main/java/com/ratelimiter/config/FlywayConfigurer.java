package com.ratelimiter.config;
import org.flywaydb.core.Flyway;

public class FlywayConfigurer {

    private FlywayConfigurer() {}

    public static void configureFlyway() {

        Flyway flyway = Flyway.configure()
                .dataSource(ConfigLoader.getDatabaseUrl(), null, null)
                .locations("classpath:db/migration")
                .load();

        flyway.migrate();
    }
}
