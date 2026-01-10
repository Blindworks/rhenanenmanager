package com.blindworks.rhenanenmanager.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Connection entity representing relationships between Corps members.
 * Tracks various types of connections like Leibbursch-Leibfuchs relationships,
 * mentorship, sponsorship, and other Corps-specific associations.
 *
 * Maps to the 'connection' table in the database.
 */
@Entity
@Table(name = "connection", indexes = {
    @Index(name = "idx_connection_from_profile", columnList = "from_profile_id"),
    @Index(name = "idx_connection_to_profile", columnList = "to_profile_id"),
    @Index(name = "idx_connection_relation_type", columnList = "relation_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Connection extends BaseEntity {

    /**
     * The profile from which the relationship originates.
     * For example, in a Leibbursch-Leibfuchs relationship, this would be the Leibbursch.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_profile_id", nullable = false)
    private Profile fromProfile;

    /**
     * The profile to which the relationship points.
     * For example, in a Leibbursch-Leibfuchs relationship, this would be the Leibfuchs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_profile_id", nullable = false)
    private Profile toProfile;

    /**
     * Type of relationship between the two members.
     * Common types:
     * - LEIBBURSCH: Leibbursch-Leibfuchs relationship (mentor to mentee)
     * - MENTOR: General mentorship
     * - SPONSOR: Sponsorship relationship
     * - PEER: Peer connection (same cohort)
     * - OTHER: Custom relationship type
     */
    @Column(name = "relation_type", nullable = false, length = 50)
    private String relationType;

    /**
     * Date when the relationship began.
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * Date when the relationship ended (null if ongoing).
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Optional description or notes about the relationship.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Indicates if this is a bidirectional relationship.
     * For example, peer connections are bidirectional (both members are peers).
     * Leibbursch-Leibfuchs is unidirectional (flows from Leibbursch to Leibfuchs).
     */
    @Column(name = "bidirectional", nullable = false)
    @Builder.Default
    private Boolean bidirectional = false;

    /**
     * User who created this connection record.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    /**
     * User who last updated this connection record.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id")
    private User updatedBy;

    /**
     * Business logic validation: A connection must have different from/to profiles.
     */
    @PrePersist
    @PreUpdate
    protected void validate() {
        if (fromProfile != null && toProfile != null &&
            fromProfile.getId().equals(toProfile.getId())) {
            throw new IllegalArgumentException("A connection cannot link a profile to itself");
        }
    }

    /**
     * Check if the connection is currently active (no end date or end date in the future).
     */
    public boolean isActive() {
        return endDate == null || endDate.isAfter(LocalDate.now());
    }
}
