package com.rumata.routes.manager;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MariaDBContainer;

public class MariaDBContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private MariaDBContainer mariaDB;

    MariaDBContainerInitializer() {
        mariaDB = new MariaDBContainer()
            .withDatabaseName("routes")
            .withUsername("routes")
            .withPassword("change.me");
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

        mariaDB.start();

        TestPropertyValues
            .of("spring.datasource.url=" + mariaDB.getJdbcUrl(),
                "spring.datasource.username=" + mariaDB.getUsername(),
                "spring.datasource.password=" + mariaDB.getPassword())
            .applyTo(configurableApplicationContext.getEnvironment());

    }
}