package com.blindworks.rhenanenmanager.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "corps_member_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorpsMemberData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    private Profile profile;

    @Column(name = "member_number")
    private String memberNumber;

    @Column(name = "corps_list_number")
    private Integer corpsListNumber;

    @Column(name = "old_picture_number")
    private Integer oldPictureNumber;

    @Column(name = "reception_number")
    private Integer receptionNumber;

    @Column(name = "reception_date")
    private LocalDate receptionDate;

    @Column(name = "acception_date")
    private LocalDate acceptionDate;

    @Column(name = "philistrierung_date")
    private LocalDate philistrierungDate;

    @Column(name = "ehrenbursche_date")
    private LocalDate ehrenburscheDate;

    @Column(name = "quit_date")
    private LocalDate quitDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(nullable = false)
    private Boolean quited;

    @Column(name = "quit_type")
    private String quitType;

    @Column(name = "klammer_chargen")
    private String klammerChargen;

    @Column(name = "number_of_mensuren", nullable = false)
    private Integer numberOfMensuren;

    @Column(name = "number_of_reinigungen", nullable = false)
    private Integer numberOfReinigungen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leib_bursch_id")
    private Profile leibBursch;

    @Column(name = "corps_notes", columnDefinition = "TEXT")
    private String corpsNotes;
}
