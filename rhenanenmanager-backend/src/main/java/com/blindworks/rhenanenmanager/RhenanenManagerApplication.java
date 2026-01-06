package com.blindworks.rhenanenmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot Application class for RhenanenManager.
 *
 * This application provides a REST API for managing fraternity/corps members,
 * events, documents, and related organizational data.
 *
 * @author Blindworks
 * @version 1.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class RhenanenManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RhenanenManagerApplication.class, args);
    }
}
