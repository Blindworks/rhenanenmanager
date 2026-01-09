package com.blindworks.rhenanenmanager.service;

import com.blindworks.rhenanenmanager.domain.dto.request.ArticleEntryRequest;
import com.blindworks.rhenanenmanager.domain.dto.response.ArticleEntryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for ArticleEntry operations.
 */
public interface ArticleEntryService {

    /**
     * Get all articles with pagination.
     */
    Page<ArticleEntryResponse> getAllArticles(Pageable pageable);

    /**
     * Get article by ID.
     */
    ArticleEntryResponse getArticleById(Long id);

    /**
     * Get articles by year.
     */
    List<ArticleEntryResponse> getArticlesByYear(Integer year);

    /**
     * Get articles by category with pagination.
     */
    Page<ArticleEntryResponse> getArticlesByCategory(String category, Pageable pageable);

    /**
     * Search articles by keyword.
     */
    Page<ArticleEntryResponse> searchArticles(String keyword, Pageable pageable);

    /**
     * Get all distinct categories.
     */
    List<String> getAllCategories();

    /**
     * Get all distinct years.
     */
    List<Integer> getAllYears();

    /**
     * Create a new article.
     */
    ArticleEntryResponse createArticle(ArticleEntryRequest request);

    /**
     * Update an existing article.
     */
    ArticleEntryResponse updateArticle(Long id, ArticleEntryRequest request);

    /**
     * Delete an article by ID.
     */
    void deleteArticle(Long id);
}
