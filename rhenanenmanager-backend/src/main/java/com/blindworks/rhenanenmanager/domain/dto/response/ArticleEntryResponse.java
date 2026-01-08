package com.blindworks.rhenanenmanager.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for ArticleEntry.
 * Contains all article information for display in the frontend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEntryResponse {

    private Long id;
    private String title;
    private String subtitle;
    private String alternativeAuthor;
    private String category;
    private String text;
    private Integer year;
    private Integer month;
    private Integer page;
    private LocalDate date;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime modified;
    private String modifiedBy;

    /**
     * Get formatted date string (e.g., "Ausgabe 01/2024").
     */
    public String getFormattedIssue() {
        if (month != null && year != null) {
            return String.format("Ausgabe %02d/%d", month, year);
        } else if (year != null) {
            return "Ausgabe " + year;
        }
        return "Ausgabe unbekannt";
    }

    /**
     * Get short preview of text (first 200 characters).
     */
    public String getTextPreview() {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.length() > 200 ? text.substring(0, 200) + "..." : text;
    }
}
