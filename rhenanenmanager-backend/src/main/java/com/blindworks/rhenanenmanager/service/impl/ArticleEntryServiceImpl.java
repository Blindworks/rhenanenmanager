package com.blindworks.rhenanenmanager.service.impl;

import com.blindworks.rhenanenmanager.domain.dto.request.ArticleEntryRequest;
import com.blindworks.rhenanenmanager.domain.dto.response.ArticleEntryResponse;
import com.blindworks.rhenanenmanager.domain.entity.ArticleEntry;
import com.blindworks.rhenanenmanager.domain.repository.ArticleEntryRepository;
import com.blindworks.rhenanenmanager.service.ArticleEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ArticleEntryService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ArticleEntryServiceImpl implements ArticleEntryService {

    private final ArticleEntryRepository articleEntryRepository;

    @Override
    public Page<ArticleEntryResponse> getAllArticles(Pageable pageable) {
        log.debug("Fetching all articles with pagination: {}", pageable);
        return articleEntryRepository.findAllByOrderByYearDescMonthDesc(pageable)
                .map(this::convertToResponse);
    }

    @Override
    public ArticleEntryResponse getArticleById(Long id) {
        log.debug("Fetching article by ID: {}", id);
        return articleEntryRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new RuntimeException("Article not found with ID: " + id));
    }

    @Override
    public List<ArticleEntryResponse> getArticlesByYear(Integer year) {
        log.debug("Fetching articles by year: {}", year);
        return articleEntryRepository.findByYearOrderByMonthDesc(year).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ArticleEntryResponse> getArticlesByCategory(String category, Pageable pageable) {
        log.debug("Fetching articles by category: {} with pagination: {}", category, pageable);
        return articleEntryRepository.findByCategoryOrderByYearDescMonthDesc(category, pageable)
                .map(this::convertToResponse);
    }

    @Override
    public Page<ArticleEntryResponse> searchArticles(String keyword, Pageable pageable) {
        log.debug("Searching articles with keyword: {} with pagination: {}", keyword, pageable);
        return articleEntryRepository.searchArticles(keyword, pageable)
                .map(this::convertToResponse);
    }

    @Override
    public List<String> getAllCategories() {
        log.debug("Fetching all distinct categories");
        return articleEntryRepository.findDistinctCategories();
    }

    @Override
    public List<Integer> getAllYears() {
        log.debug("Fetching all distinct years");
        return articleEntryRepository.findDistinctYears();
    }

    @Override
    @Transactional
    public ArticleEntryResponse createArticle(ArticleEntryRequest request) {
        log.debug("Creating new article with title: {}", request.getTitle());

        ArticleEntry entity = ArticleEntry.builder()
                .title(request.getTitle())
                .subtitle(request.getSubtitle())
                .alternativeAuthor(request.getAlternativeAuthor())
                .category(request.getCategory())
                .text(request.getText())
                .year(request.getYear())
                .month(request.getMonth())
                .page(request.getPage())
                .date(request.getDate())
                .created(LocalDateTime.now())
                .createdBy("system") // TODO: Get from SecurityContext
                .build();

        ArticleEntry savedEntity = articleEntryRepository.save(entity);
        log.info("Article created with ID: {}", savedEntity.getId());
        return convertToResponse(savedEntity);
    }

    @Override
    @Transactional
    public ArticleEntryResponse updateArticle(Long id, ArticleEntryRequest request) {
        log.debug("Updating article with ID: {}", id);

        ArticleEntry entity = articleEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with ID: " + id));

        entity.setTitle(request.getTitle());
        entity.setSubtitle(request.getSubtitle());
        entity.setAlternativeAuthor(request.getAlternativeAuthor());
        entity.setCategory(request.getCategory());
        entity.setText(request.getText());
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setPage(request.getPage());
        entity.setDate(request.getDate());
        entity.setModified(LocalDateTime.now());
        entity.setModifiedBy("system"); // TODO: Get from SecurityContext

        ArticleEntry savedEntity = articleEntryRepository.save(entity);
        log.info("Article updated with ID: {}", savedEntity.getId());
        return convertToResponse(savedEntity);
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        log.debug("Deleting article with ID: {}", id);

        if (!articleEntryRepository.existsById(id)) {
            throw new RuntimeException("Article not found with ID: " + id);
        }

        articleEntryRepository.deleteById(id);
        log.info("Article deleted with ID: {}", id);
    }

    /**
     * Convert ArticleEntry entity to ArticleEntryResponse DTO.
     */
    private ArticleEntryResponse convertToResponse(ArticleEntry entity) {
        return ArticleEntryResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .subtitle(entity.getSubtitle())
                .alternativeAuthor(entity.getAlternativeAuthor())
                .category(entity.getCategory())
                .text(entity.getText())
                .year(entity.getYear())
                .month(entity.getMonth())
                .page(entity.getPage())
                .date(entity.getDate())
                .created(entity.getCreated())
                .createdBy(entity.getCreatedBy())
                .modified(entity.getModified())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}
