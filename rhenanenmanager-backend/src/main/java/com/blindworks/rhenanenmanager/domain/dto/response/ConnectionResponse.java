package com.blindworks.rhenanenmanager.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for Connection.
 * Contains connection information with embedded profile data for display.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionResponse {

    private Long id;
    private Long fromProfileId;
    private String fromProfileName;
    private Long toProfileId;
    private String toProfileName;
    private String relationType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Boolean bidirectional;
    private Boolean active;
    private LocalDateTime created;
    private LocalDateTime updated;

    /**
     * Nested DTO for profile summary information.
     * Avoids exposing full Profile entity.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileSummary {
        private Long id;
        private String firstname;
        private String lastname;
        private String email;
        private String pictureUrl;

        /**
         * Get full name (firstname + lastname).
         */
        public String getFullName() {
            return firstname + " " + lastname;
        }
    }

    /**
     * Detailed response with full profile information.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConnectionDetailResponse {
        private Long id;
        private ProfileSummary fromProfile;
        private ProfileSummary toProfile;
        private String relationType;
        private LocalDate startDate;
        private LocalDate endDate;
        private String description;
        private Boolean bidirectional;
        private Boolean active;
        private LocalDateTime created;
        private LocalDateTime updated;
    }
}
