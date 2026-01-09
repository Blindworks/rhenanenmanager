package com.blindworks.rhenanenmanager.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating ArticleEntry.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEntryRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String subtitle;
    private String alternativeAuthor;
    private String category;
    private String text;
    private Integer year;
    private Integer month;
    private Integer page;
    private LocalDate date;
}
