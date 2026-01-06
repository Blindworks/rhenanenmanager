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

    private String firstname;

    private String middlename;

    private String lastname;

    private String email;

    private String title;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "birth_place")
    private String birthPlace;

    @Column(name = "death_date")
    private LocalDate deathDate;

    @Column(name = "death_place")
    private String deathPlace;

    private Boolean dead;

    @Column(name = "marriage_date")
    private LocalDate marriageDate;

    @Column(name = "picture_link")
    private String pictureLink;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(name = "national_service")
    private String nationalService;

    @Column(name = "honorary_appointments", columnDefinition = "TEXT")
    private String honoraryAppointments;

    @Column(name = "life_stations", columnDefinition = "TEXT")
    private String lifeStations;

    private String status;

    private Boolean quited;

    @Column(name = "number")
    private String number;

    @Column(name = "reception_date")
    private LocalDate receptionDate;

    @Column(name = "acception_date")
    private LocalDate acceptionDate;

    @Column(name = "philistrierung_date")
    private LocalDate philistrierungDate;

    @Column(name = "quit_date")
    private LocalDate quitDate;

    private LocalDateTime created;

    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
        if (dead == null) {
            dead = false;
        }
        if (quited == null) {
            quited = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }
}
