package com.kaiocosta.industriamonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IndustriaMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndustriaMonitorApplication.class, args);
    }
}
