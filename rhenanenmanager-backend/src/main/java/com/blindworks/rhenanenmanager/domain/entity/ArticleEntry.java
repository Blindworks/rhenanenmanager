package com.blindworks.rhenanenmanager.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing an article entry in the Rhenanenruf (Corps journal).
 * Maps to the 'article_entry' table in the current database.
 */
@Entity
@Table(name = "article_entry")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", columnDefinition = "LONGTEXT")
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "alternativeAuthor")
    private String alternativeAuthor;

    @Column(name = "category")
    private String category;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "page")
    private Integer page;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "modified")
    private LocalDateTime modified;

    @Column(name = "modifiedBy")
    private String modifiedBy;

    @Column(name = "created_by_id")
    private Long createdById;

    @Column(name = "modified_by_id")
    private Long modifiedById;
}
