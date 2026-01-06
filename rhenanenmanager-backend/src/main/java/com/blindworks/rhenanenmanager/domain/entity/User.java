package com.blindworks.rhenanenmanager.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * User entity representing system users with authentication credentials.
 * Maps to the 'user' table in the database.
 */
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private String firstname;

    private String lastname;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "accountLocked")
    private Boolean accountLocked;

    @Column(name = "accountLockedDate")
    private LocalDateTime accountLockedDate;

    @Column(name = "failedLogins")
    private Integer failedLogins;

    @Column(name = "lastLogin")
    private LocalDateTime lastLogin;

    @Column(name = "passwordExpireDate")
    private LocalDateTime passwordExpireDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    private LocalDateTime created;

    private String createdBy;

    private LocalDateTime updated;

    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
        if (activated == null) {
            activated = false;
        }
        if (accountLocked == null) {
            accountLocked = false;
        }
        if (failedLogins == null) {
            failedLogins = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }
}
