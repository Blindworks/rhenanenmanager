package com.blindworks.rhenanenmanager.service;

import com.blindworks.rhenanenmanager.domain.dto.request.ConnectionRequest;
import com.blindworks.rhenanenmanager.domain.dto.response.ConnectionResponse;

import java.util.List;

/**
 * Service interface for Connection operations.
 * Manages relationships between Corps members.
 */
public interface ConnectionService {

    /**
     * Create a new connection between two profiles.
     */
    ConnectionResponse createConnection(ConnectionRequest request);

    /**
     * Update an existing connection.
     */
    ConnectionResponse updateConnection(Long id, ConnectionRequest request);

    /**
     * Delete a connection by ID.
     */
    void deleteConnection(Long id);

    /**
     * Get a connection by ID.
     */
    ConnectionResponse getConnectionById(Long id);

    /**
     * Get detailed connection by ID (with full profile information).
     */
    ConnectionResponse.ConnectionDetailResponse getConnectionDetailById(Long id);

    /**
     * Get all connections for a specific profile (as source or target).
     */
    List<ConnectionResponse> getConnectionsForProfile(Long profileId);

    /**
     * Get detailed connections for a specific profile (with full profile information).
     */
    List<ConnectionResponse.ConnectionDetailResponse> getDetailedConnectionsForProfile(Long profileId);

    /**
     * Get all connections of a specific type.
     */
    List<ConnectionResponse> getConnectionsByType(String relationType);

    /**
     * Get all connections where the profile is the source (from).
     */
    List<ConnectionResponse> getConnectionsFrom(Long profileId);

    /**
     * Get all connections where the profile is the target (to).
     */
    List<ConnectionResponse> getConnectionsTo(Long profileId);

    /**
     * Get active connections for a profile.
     */
    List<ConnectionResponse> getActiveConnectionsForProfile(Long profileId);

    /**
     * Get active connections of a specific type for a profile.
     */
    List<ConnectionResponse> getActiveConnectionsByType(Long profileId, String relationType);

    /**
     * Get all available relation types.
     */
    List<String> getRelationTypes();

    /**
     * Check if a connection exists between two profiles.
     */
    boolean connectionExists(Long fromProfileId, Long toProfileId, String relationType);

    /**
     * Get all connections, optionally filtered by active status.
     */
    List<ConnectionResponse> getAllConnections(boolean activeOnly);
}
