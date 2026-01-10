package com.blindworks.rhenanenmanager.controller;

import com.blindworks.rhenanenmanager.domain.dto.request.ConnectionRequest;
import com.blindworks.rhenanenmanager.domain.dto.response.ConnectionResponse;
import com.blindworks.rhenanenmanager.service.ConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Connection operations.
 * Provides endpoints for managing relationships between Corps members.
 */
@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Connections", description = "Corps Member Connection Management API")
public class ConnectionController {

    private final ConnectionService connectionService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get all connections",
        description = "Retrieve all connections, optionally filtered by active status"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connections retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ConnectionResponse>> getAllConnections(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        log.info("GET /api/connections?activeOnly={}", activeOnly);
        List<ConnectionResponse> responses = connectionService.getAllConnections(activeOnly);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Create a new connection",
        description = "Establish a new relationship between two Corps members"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Connection created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Profile not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ConnectionResponse> createConnection(@Valid @RequestBody ConnectionRequest request) {
        log.info("POST /api/connections - Creating connection: {} -> {} ({})",
                request.getFromProfileId(), request.getToProfileId(), request.getRelationType());
        ConnectionResponse response = connectionService.createConnection(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get connection by ID",
        description = "Retrieve a specific connection by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connection found"),
        @ApiResponse(responseCode = "404", description = "Connection not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ConnectionResponse> getConnectionById(@PathVariable Long id) {
        log.info("GET /api/connections/{}", id);
        ConnectionResponse response = connectionService.getConnectionById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/detail")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get detailed connection by ID",
        description = "Retrieve a specific connection with full profile information"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connection found"),
        @ApiResponse(responseCode = "404", description = "Connection not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ConnectionResponse.ConnectionDetailResponse> getConnectionDetailById(@PathVariable Long id) {
        log.info("GET /api/connections/{}/detail", id);
        ConnectionResponse.ConnectionDetailResponse response = connectionService.getConnectionDetailById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{profileId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get all connections for a profile",
        description = "Retrieve all connections where the profile is either source or target"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connections retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ConnectionResponse>> getConnectionsForProfile(@PathVariable Long profileId) {
        log.info("GET /api/connections/profile/{}", profileId);
        List<ConnectionResponse> responses = connectionService.getConnectionsForProfile(profileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/profile/{profileId}/detail")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get detailed connections for a profile",
        description = "Retrieve all connections for a profile with full profile information"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connections retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ConnectionResponse.ConnectionDetailResponse>> getDetailedConnectionsForProfile(
            @PathVariable Long profileId) {
        log.info("GET /api/connections/profile/{}/detail", profileId);
        List<ConnectionResponse.ConnectionDetailResponse> responses =
                connectionService.getDetailedConnectionsForProfile(profileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/profile/{profileId}/from")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get connections from a profile",
        description = "Retrieve all connections where the profile is the source (e.g., all Leibfüchse of a Leibbursch)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connections retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ConnectionResponse>> getConnectionsFrom(@PathVariable Long profileId) {
        log.info("GET /api/connections/profile/{}/from", profileId);
        List<ConnectionResponse> responses = connectionService.getConnectionsFrom(profileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/profile/{profileId}/to")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get connections to a profile",
        description = "Retrieve all connections where the profile is the target (e.g., the Leibbursch of a Leibfuchs)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connections retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ConnectionResponse>> getConnectionsTo(@PathVariable Long profileId) {
        log.info("GET /api/connections/profile/{}/to", profileId);
        List<ConnectionResponse> responses = connectionService.getConnectionsTo(profileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/profile/{profileId}/active")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get active connections for a profile",
        description = "Retrieve all active connections (no end date or end date in the future)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active connections retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ConnectionResponse>> getActiveConnectionsForProfile(@PathVariable Long profileId) {
        log.info("GET /api/connections/profile/{}/active", profileId);
        List<ConnectionResponse> responses = connectionService.getActiveConnectionsForProfile(profileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/type/{relationType}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get connections by type",
        description = "Retrieve all connections of a specific type (e.g., LEIBBURSCH, MENTOR)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connections retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ConnectionResponse>> getConnectionsByType(@PathVariable String relationType) {
        log.info("GET /api/connections/type/{}", relationType);
        List<ConnectionResponse> responses = connectionService.getConnectionsByType(relationType);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/profile/{profileId}/type/{relationType}/active")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get active connections by type for a profile",
        description = "Retrieve active connections of a specific type for a profile"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connections retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ConnectionResponse>> getActiveConnectionsByType(
            @PathVariable Long profileId,
            @PathVariable String relationType) {
        log.info("GET /api/connections/profile/{}/type/{}/active", profileId, relationType);
        List<ConnectionResponse> responses = connectionService.getActiveConnectionsByType(profileId, relationType);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/types")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Get all relation types",
        description = "Retrieve all distinct relation types in use"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Relation types retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<String>> getRelationTypes() {
        log.info("GET /api/connections/types");
        List<String> types = connectionService.getRelationTypes();
        return ResponseEntity.ok(types);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Update a connection",
        description = "Update an existing connection"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connection updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Connection or profile not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ConnectionResponse> updateConnection(
            @PathVariable Long id,
            @Valid @RequestBody ConnectionRequest request) {
        log.info("PUT /api/connections/{} - Updating connection", id);
        ConnectionResponse response = connectionService.updateConnection(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
        summary = "Delete a connection",
        description = "Delete a connection by ID. Only administrators can delete connections."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Connection deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Connection not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<Void> deleteConnection(@PathVariable Long id) {
        log.info("DELETE /api/connections/{}", id);
        connectionService.deleteConnection(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(
        summary = "Check if connection exists",
        description = "Check if a connection exists between two profiles with a specific type"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Boolean> connectionExists(
            @RequestParam Long fromProfileId,
            @RequestParam Long toProfileId,
            @RequestParam String relationType) {
        log.info("GET /api/connections/exists?fromProfileId={}&toProfileId={}&relationType={}",
                fromProfileId, toProfileId, relationType);
        boolean exists = connectionService.connectionExists(fromProfileId, toProfileId, relationType);
        return ResponseEntity.ok(exists);
    }
}
