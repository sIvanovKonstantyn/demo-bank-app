package com.home.demos.deposit.domain;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestPostgresqlContainer extends PostgreSQLContainer<TestPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:11.1";
    private static TestPostgresqlContainer container;

    private TestPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static TestPostgresqlContainer getInstance() {
        if (container == null) {
            container = new TestPostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DEPOSIT_DB_DATASOURCE_URL", container.getJdbcUrl());
        System.setProperty("DEPOSIT_DB_DATASOURCE_USER", container.getUsername());
        System.setProperty("DEPOSIT_DB_DATASOURCE_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}