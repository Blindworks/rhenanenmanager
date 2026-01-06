package com.blindworks.rhenanenmanager.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Role entity for user authorization.
 * Maps to the 'role' table in the database.
 */
@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "roles_permissions",
        joinColumns = @JoinColumn(name = "Role_id")
    )
    @Column(name = "permissions")
    private List<String> permissions;
}
