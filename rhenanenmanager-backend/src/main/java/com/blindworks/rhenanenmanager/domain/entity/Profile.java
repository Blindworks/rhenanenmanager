package com.blindworks.rhenanenmanager.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Profile entity representing member profiles.
 * Maps to the 'profile' table in the database.
 */
@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    private String middlename;

    private String title;

    @Column(nullable = false)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "birth_place")
    private String birthPlace;

    @Column(nullable = false)
    private Boolean deceased;

    @Column(name = "death_date")
    private LocalDate deathDate;

    @Column(name = "death_place")
    private String deathPlace;

    @Column(name = "marriage_date")
    private LocalDate marriageDate;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "private_address_id")
    private Address privateAddress;

    @ManyToOne
    @JoinColumn(name = "parents_address_id")
    private Address parentsAddress;

    @ManyToOne
    @JoinColumn(name = "private_contact_id")
    private Contact privateContact;

    @ManyToOne
    @JoinColumn(name = "business_contact_id")
    private Contact businessContact;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @Column(name = "email_verification_failed_date")
    private LocalDateTime emailVerificationFailedDate;

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
        if (deceased == null) {
            deceased = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }
}
