package com.blindworks.rhenanenmanager.config;

import com.blindworks.rhenanenmanager.domain.entity.Role;
import com.blindworks.rhenanenmanager.domain.entity.User;
import com.blindworks.rhenanenmanager.domain.repository.RoleRepository;
import com.blindworks.rhenanenmanager.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * Data initializer for development environment.
 * Creates default roles and test users if they don't exist.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Profile("dev")
    public CommandLineRunner initializeData() {
        return args -> {
            log.info("Initializing development data...");

            // Create roles if they don't exist
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role role = Role.builder()
                                .name("ROLE_ADMIN")
                                .description("Administrator with full access")
                                .build();
                        roleRepository.save(role);
                        log.info("Created ROLE_ADMIN");
                        return role;
                    });

            Role memberRole = roleRepository.findByName("ROLE_MEMBER")
                    .orElseGet(() -> {
                        Role role = Role.builder()
                                .name("ROLE_MEMBER")
                                .description("Regular member with basic access")
                                .build();
                        roleRepository.save(role);
                        log.info("Created ROLE_MEMBER");
                        return role;
                    });

            // Create admin user if doesn't exist
            if (!userRepository.findByUsername("admin").isPresent()) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("password"))
                        .email("admin@rhenanenmanager.de")
                        .firstname("Max")
                        .lastname("Mustermann")
                        .activated(true)
                        .accountLocked(false)
                        .failedLogins(0)
                        .role(adminRole)
                        .created(LocalDateTime.now())
                        .updated(LocalDateTime.now())
                        .build();
                userRepository.save(admin);
                log.info("Created test admin user: username=admin, password=password");
            }

            // Create member user if doesn't exist
            if (!userRepository.findByUsername("member").isPresent()) {
                User member = User.builder()
                        .username("member")
                        .password(passwordEncoder.encode("password"))
                        .email("member@rhenanenmanager.de")
                        .firstname("Hans")
                        .lastname("Schmidt")
                        .activated(true)
                        .accountLocked(false)
                        .failedLogins(0)
                        .role(memberRole)
                        .created(LocalDateTime.now())
                        .updated(LocalDateTime.now())
                        .build();
                userRepository.save(member);
                log.info("Created test member user: username=member, password=password");
            }

            log.info("Development data initialization completed!");
        };
    }
}
