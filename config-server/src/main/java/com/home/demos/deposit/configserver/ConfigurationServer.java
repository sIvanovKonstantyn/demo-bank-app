package com.home.demos.deposit.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigurationServer {

    public static void main(String[] arguments) {
        SpringApplication.run(ConfigurationServer.class, arguments);
    }
}
