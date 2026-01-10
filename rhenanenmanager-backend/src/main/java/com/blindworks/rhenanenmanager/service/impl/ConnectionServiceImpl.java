package com.blindworks.rhenanenmanager.service.impl;

import com.blindworks.rhenanenmanager.domain.dto.request.ConnectionRequest;
import com.blindworks.rhenanenmanager.domain.dto.response.ConnectionResponse;
import com.blindworks.rhenanenmanager.domain.entity.Connection;
import com.blindworks.rhenanenmanager.domain.entity.Profile;
import com.blindworks.rhenanenmanager.domain.repository.ConnectionRepository;
import com.blindworks.rhenanenmanager.domain.repository.ProfileRepository;
import com.blindworks.rhenanenmanager.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ConnectionService.
 * Handles business logic for Corps member connections.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public ConnectionResponse createConnection(ConnectionRequest request) {
        log.debug("Creating new connection: {} -> {} ({})",
                request.getFromProfileId(), request.getToProfileId(), request.getRelationType());

        // Validate that both profiles exist
        Profile fromProfile = profileRepository.findById(request.getFromProfileId())
                .orElseThrow(() -> new RuntimeException("From profile not found with ID: " + request.getFromProfileId()));

        Profile toProfile = profileRepository.findById(request.getToProfileId())
                .orElseThrow(() -> new RuntimeException("To profile not found with ID: " + request.getToProfileId()));

        // Validate that profiles are different
        if (fromProfile.getId().equals(toProfile.getId())) {
            throw new IllegalArgumentException("Cannot create a connection from a profile to itself");
        }

        // Check if connection already exists
        if (connectionRepository.existsByFromProfileIdAndToProfileIdAndRelationType(
                request.getFromProfileId(), request.getToProfileId(), request.getRelationType())) {
            throw new IllegalArgumentException("Connection already exists between these profiles with this type");
        }

        Connection entity = Connection.builder()
                .fromProfile(fromProfile)
                .toProfile(toProfile)
                .relationType(request.getRelationType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .description(request.getDescription())
                .bidirectional(request.getBidirectional() != null ? request.getBidirectional() : false)
                .build();

        Connection savedEntity = connectionRepository.save(entity);
        log.info("Connection created with ID: {}", savedEntity.getId());
        return convertToResponse(savedEntity);
    }

    @Override
    @Transactional
    public ConnectionResponse updateConnection(Long id, ConnectionRequest request) {
        log.debug("Updating connection with ID: {}", id);

        Connection entity = connectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Connection not found with ID: " + id));

        // Validate that both profiles exist
        Profile fromProfile = profileRepository.findById(request.getFromProfileId())
                .orElseThrow(() -> new RuntimeException("From profile not found with ID: " + request.getFromProfileId()));

        Profile toProfile = profileRepository.findById(request.getToProfileId())
                .orElseThrow(() -> new RuntimeException("To profile not found with ID: " + request.getToProfileId()));

        // Update entity
        entity.setFromProfile(fromProfile);
        entity.setToProfile(toProfile);
        entity.setRelationType(request.getRelationType());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setDescription(request.getDescription());
        entity.setBidirectional(request.getBidirectional() != null ? request.getBidirectional() : false);

        Connection savedEntity = connectionRepository.save(entity);
        log.info("Connection updated with ID: {}", savedEntity.getId());
        return convertToResponse(savedEntity);
    }

    @Override
    @Transactional
    public void deleteConnection(Long id) {
        log.debug("Deleting connection with ID: {}", id);

        if (!connectionRepository.existsById(id)) {
            throw new RuntimeException("Connection not found with ID: " + id);
        }

        connectionRepository.deleteById(id);
        log.info("Connection deleted with ID: {}", id);
    }

    @Override
    public ConnectionResponse getConnectionById(Long id) {
        log.debug("Fetching connection by ID: {}", id);
        return connectionRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new RuntimeException("Connection not found with ID: " + id));
    }

    @Override
    public ConnectionResponse.ConnectionDetailResponse getConnectionDetailById(Long id) {
        log.debug("Fetching detailed connection by ID: {}", id);
        return connectionRepository.findById(id)
                .map(this::convertToDetailResponse)
                .orElseThrow(() -> new RuntimeException("Connection not found with ID: " + id));
    }

    @Override
    public List<ConnectionResponse> getConnectionsForProfile(Long profileId) {
        log.debug("Fetching all connections for profile ID: {}", profileId);
        return connectionRepository.findByProfileId(profileId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConnectionResponse.ConnectionDetailResponse> getDetailedConnectionsForProfile(Long profileId) {
        log.debug("Fetching detailed connections for profile ID: {}", profileId);
        return connectionRepository.findByProfileId(profileId).stream()
                .map(this::convertToDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConnectionResponse> getConnectionsByType(String relationType) {
        log.debug("Fetching connections by type: {}", relationType);
        return connectionRepository.findByRelationType(relationType).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConnectionResponse> getConnectionsFrom(Long profileId) {
        log.debug("Fetching connections from profile ID: {}", profileId);
        return connectionRepository.findByFromProfileId(profileId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConnectionResponse> getConnectionsTo(Long profileId) {
        log.debug("Fetching connections to profile ID: {}", profileId);
        return connectionRepository.findByToProfileId(profileId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConnectionResponse> getActiveConnectionsForProfile(Long profileId) {
        log.debug("Fetching active connections for profile ID: {}", profileId);
        return connectionRepository.findActiveConnectionsByProfileId(profileId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConnectionResponse> getActiveConnectionsByType(Long profileId, String relationType) {
        log.debug("Fetching active connections for profile ID: {} and type: {}", profileId, relationType);
        return connectionRepository.findActiveConnectionsByProfileIdAndType(profileId, relationType).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRelationTypes() {
        log.debug("Fetching all distinct relation types");
        return connectionRepository.findDistinctRelationTypes();
    }

    @Override
    public boolean connectionExists(Long fromProfileId, Long toProfileId, String relationType) {
        log.debug("Checking if connection exists: {} -> {} ({})", fromProfileId, toProfileId, relationType);
        return connectionRepository.existsByFromProfileIdAndToProfileIdAndRelationType(
                fromProfileId, toProfileId, relationType);
    }

    /**
     * Convert Connection entity to ConnectionResponse DTO.
     */
    private ConnectionResponse convertToResponse(Connection entity) {
        return ConnectionResponse.builder()
                .id(entity.getId())
                .fromProfileId(entity.getFromProfile().getId())
                .fromProfileName(entity.getFromProfile().getFirstname() + " " + entity.getFromProfile().getLastname())
                .toProfileId(entity.getToProfile().getId())
                .toProfileName(entity.getToProfile().getFirstname() + " " + entity.getToProfile().getLastname())
                .relationType(entity.getRelationType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .description(entity.getDescription())
                .bidirectional(entity.getBidirectional())
                .active(entity.isActive())
                .created(entity.getCreated())
                .updated(entity.getUpdated())
                .build();
    }

    /**
     * Convert Connection entity to ConnectionDetailResponse DTO with full profile information.
     */
    private ConnectionResponse.ConnectionDetailResponse convertToDetailResponse(Connection entity) {
        return ConnectionResponse.ConnectionDetailResponse.builder()
                .id(entity.getId())
                .fromProfile(convertToProfileSummary(entity.getFromProfile()))
                .toProfile(convertToProfileSummary(entity.getToProfile()))
                .relationType(entity.getRelationType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .description(entity.getDescription())
                .bidirectional(entity.getBidirectional())
                .active(entity.isActive())
                .created(entity.getCreated())
                .updated(entity.getUpdated())
                .build();
    }

    /**
     * Convert Profile entity to ProfileSummary DTO.
     */
    private ConnectionResponse.ProfileSummary convertToProfileSummary(Profile profile) {
        return ConnectionResponse.ProfileSummary.builder()
                .id(profile.getId())
                .firstname(profile.getFirstname())
                .lastname(profile.getLastname())
                .email(profile.getEmail())
                .pictureUrl(profile.getPictureUrl())
                .build();
    }

    /**
     * Get all connections, optionally filtered by active status.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ConnectionResponse> getAllConnections(boolean activeOnly) {
        log.info("Getting all connections (activeOnly: {})", activeOnly);

        List<Connection> connections;
        if (activeOnly) {
            connections = connectionRepository.findAllActiveConnections();
            log.info("Found {} active connections", connections.size());
        } else {
            connections = connectionRepository.findAll();
            log.info("Found {} total connections", connections.size());
        }

        return connections.stream()
                .map(this::convertToResponse)
                .toList();
    }
}
