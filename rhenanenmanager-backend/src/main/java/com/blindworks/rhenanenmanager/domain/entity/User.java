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

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(name = "activated", nullable = false)
    private Boolean activated;

    @Column(name = "account_locked", nullable = false)
    private Boolean accountLocked;

    @Column(name = "account_locked_date")
    private LocalDateTime accountLockedDate;

    @Column(name = "failed_logins", nullable = false)
    private Integer failedLogins;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "password_expire_date")
    private LocalDateTime passwordExpireDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
        updated = LocalDateTime.now();
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
