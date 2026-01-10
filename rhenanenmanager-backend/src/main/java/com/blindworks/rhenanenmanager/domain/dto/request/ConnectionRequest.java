package com.blindworks.rhenanenmanager.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating Connection.
 * Used when establishing or modifying relationships between Corps members.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequest {

    /**
     * ID of the profile from which the relationship originates.
     */
    @NotNull(message = "From profile ID is required")
    private Long fromProfileId;

    /**
     * ID of the profile to which the relationship points.
     */
    @NotNull(message = "To profile ID is required")
    private Long toProfileId;

    /**
     * Type of relationship (e.g., LEIBBURSCH, MENTOR, SPONSOR, PEER).
     */
    @NotBlank(message = "Relation type is required")
    private String relationType;

    /**
     * Date when the relationship began.
     */
    private LocalDate startDate;

    /**
     * Date when the relationship ended (null if ongoing).
     */
    private LocalDate endDate;

    /**
     * Optional description or notes about the relationship.
     */
    private String description;

    /**
     * Indicates if this is a bidirectional relationship.
     * Default: false (unidirectional).
     */
    @Builder.Default
    private Boolean bidirectional = false;
}
