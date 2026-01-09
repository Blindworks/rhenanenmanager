package com.blindworks.rhenanenmanager.controller;

import com.blindworks.rhenanenmanager.domain.dto.request.ArticleEntryRequest;
import com.blindworks.rhenanenmanager.domain.dto.response.ArticleEntryResponse;
import com.blindworks.rhenanenmanager.service.ArticleEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for ArticleEntry operations.
 * Provides endpoints for the Rhenanenruf Glossar feature.
 */
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Article Entries", description = "Rhenanenruf Glossar API")
public class ArticleEntryController {

    private final ArticleEntryService articleEntryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @Operation(summary = "Get all articles", description = "Get all articles with pagination")
    public ResponseEntity<Page<ArticleEntryResponse>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/articles - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleEntryResponse> articles = articleEntryService.getAllArticles(pageable);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @Operation(summary = "Get article by ID", description = "Get a specific article by its ID")
    public ResponseEntity<ArticleEntryResponse> getArticleById(@PathVariable Long id) {
        log.info("GET /api/articles/{}", id);
        ArticleEntryResponse article = articleEntryService.getArticleById(id);
        return ResponseEntity.ok(article);
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @Operation(summary = "Get articles by year", description = "Get all articles from a specific year")
    public ResponseEntity<List<ArticleEntryResponse>> getArticlesByYear(@PathVariable Integer year) {
        log.info("GET /api/articles/year/{}", year);
        List<ArticleEntryResponse> articles = articleEntryService.getArticlesByYear(year);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @Operation(summary = "Get articles by category", description = "Get all articles from a specific category")
    public ResponseEntity<Page<ArticleEntryResponse>> getArticlesByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/articles/category/{} - page: {}, size: {}", category, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleEntryResponse> articles = articleEntryService.getArticlesByCategory(category, pageable);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @Operation(summary = "Search articles", description = "Search articles by keyword in title or text")
    public ResponseEntity<Page<ArticleEntryResponse>> searchArticles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/articles/search?keyword={} - page: {}, size: {}", keyword, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleEntryResponse> articles = articleEntryService.searchArticles(keyword, pageable);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @Operation(summary = "Get all categories", description = "Get all distinct categories")
    public ResponseEntity<List<String>> getAllCategories() {
        log.info("GET /api/articles/categories");
        List<String> categories = articleEntryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/years")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @Operation(summary = "Get all years", description = "Get all distinct years")
    public ResponseEntity<List<Integer>> getAllYears() {
        log.info("GET /api/articles/years");
        List<Integer> years = articleEntryService.getAllYears();
        return ResponseEntity.ok(years);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create article", description = "Create a new article entry")
    public ResponseEntity<ArticleEntryResponse> createArticle(@Valid @RequestBody ArticleEntryRequest request) {
        log.info("POST /api/articles - title: {}", request.getTitle());
        ArticleEntryResponse article = articleEntryService.createArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update article", description = "Update an existing article entry")
    public ResponseEntity<ArticleEntryResponse> updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody ArticleEntryRequest request) {
        log.info("PUT /api/articles/{} - title: {}", id, request.getTitle());
        ArticleEntryResponse article = articleEntryService.updateArticle(id, request);
        return ResponseEntity.ok(article);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete article", description = "Delete an article entry by ID")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        log.info("DELETE /api/articles/{}", id);
        articleEntryService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
