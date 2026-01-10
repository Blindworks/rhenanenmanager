package com.blindworks.rhenanenmanager.domain.repository;

import com.blindworks.rhenanenmanager.domain.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Connection entity.
 * Provides CRUD operations and custom queries for member connections.
 */
@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    /**
     * Find all connections where the given profile is the source (from).
     * For example, all Leibfüchse of a Leibbursch.
     */
    List<Connection> findByFromProfileId(Long profileId);

    /**
     * Find all connections where the given profile is the target (to).
     * For example, the Leibbursch of a Leibfuchs.
     */
    List<Connection> findByToProfileId(Long profileId);

    /**
     * Find all connections involving a profile (either as source or target).
     * This gives the complete network of connections for a member.
     */
    @Query("SELECT c FROM Connection c WHERE c.fromProfile.id = :profileId OR c.toProfile.id = :profileId")
    List<Connection> findByProfileId(@Param("profileId") Long profileId);

    /**
     * Find connections by relation type.
     * For example, all Leibbursch-Leibfuchs relationships.
     */
    List<Connection> findByRelationType(String relationType);

    /**
     * Find connections by relation type for a specific profile (as source).
     */
    List<Connection> findByFromProfileIdAndRelationType(Long profileId, String relationType);

    /**
     * Find connections by relation type for a specific profile (as target).
     */
    List<Connection> findByToProfileIdAndRelationType(Long profileId, String relationType);

    /**
     * Find active connections for a profile (no end date or end date in the future).
     */
    @Query("SELECT c FROM Connection c WHERE " +
           "(c.fromProfile.id = :profileId OR c.toProfile.id = :profileId) " +
           "AND (c.endDate IS NULL OR c.endDate > CURRENT_DATE)")
    List<Connection> findActiveConnectionsByProfileId(@Param("profileId") Long profileId);

    /**
     * Find active connections of a specific type for a profile.
     */
    @Query("SELECT c FROM Connection c WHERE " +
           "(c.fromProfile.id = :profileId OR c.toProfile.id = :profileId) " +
           "AND c.relationType = :relationType " +
           "AND (c.endDate IS NULL OR c.endDate > CURRENT_DATE)")
    List<Connection> findActiveConnectionsByProfileIdAndType(
        @Param("profileId") Long profileId,
        @Param("relationType") String relationType
    );

    /**
     * Find connections between two specific profiles.
     * Checks both directions (A->B and B->A).
     */
    @Query("SELECT c FROM Connection c WHERE " +
           "(c.fromProfile.id = :profileId1 AND c.toProfile.id = :profileId2) OR " +
           "(c.fromProfile.id = :profileId2 AND c.toProfile.id = :profileId1)")
    List<Connection> findConnectionsBetweenProfiles(
        @Param("profileId1") Long profileId1,
        @Param("profileId2") Long profileId2
    );

    /**
     * Check if a connection exists between two profiles of a specific type.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Connection c WHERE " +
           "c.fromProfile.id = :fromProfileId AND c.toProfile.id = :toProfileId " +
           "AND c.relationType = :relationType")
    boolean existsByFromProfileIdAndToProfileIdAndRelationType(
        @Param("fromProfileId") Long fromProfileId,
        @Param("toProfileId") Long toProfileId,
        @Param("relationType") String relationType
    );

    /**
     * Get all distinct relation types in use.
     */
    @Query("SELECT DISTINCT c.relationType FROM Connection c ORDER BY c.relationType")
    List<String> findDistinctRelationTypes();

    /**
     * Find all active connections (no end date or end date in the future).
     */
    @Query("SELECT c FROM Connection c WHERE c.endDate IS NULL OR c.endDate > CURRENT_DATE")
    List<Connection> findAllActiveConnections();
}
