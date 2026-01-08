package com.blindworks.rhenanenmanager.domain.repository;

import com.blindworks.rhenanenmanager.domain.entity.ArticleEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ArticleEntry entity.
 * Provides CRUD operations and custom queries for Rhenanenruf articles.
 */
@Repository
public interface ArticleEntryRepository extends JpaRepository<ArticleEntry, Long> {

    /**
     * Find all articles ordered by year and month descending (newest first).
     */
    Page<ArticleEntry> findAllByOrderByYearDescMonthDesc(Pageable pageable);

    /**
     * Find articles by year.
     */
    List<ArticleEntry> findByYearOrderByMonthDesc(Integer year);

    /**
     * Find articles by category.
     */
    Page<ArticleEntry> findByCategoryOrderByYearDescMonthDesc(String category, Pageable pageable);

    /**
     * Search articles by title or text content.
     */
    @Query("SELECT a FROM ArticleEntry a WHERE " +
           "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.text) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.subtitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY a.year DESC, a.month DESC")
    Page<ArticleEntry> searchArticles(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Get all distinct categories.
     */
    @Query("SELECT DISTINCT a.category FROM ArticleEntry a WHERE a.category IS NOT NULL ORDER BY a.category")
    List<String> findDistinctCategories();

    /**
     * Get all distinct years.
     */
    @Query("SELECT DISTINCT a.year FROM ArticleEntry a WHERE a.year IS NOT NULL ORDER BY a.year DESC")
    List<Integer> findDistinctYears();
}
