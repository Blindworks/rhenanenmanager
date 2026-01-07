package com.blindworks.rhenanenmanager.domain.repository;

import com.blindworks.rhenanenmanager.domain.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByEmail(String email);

    // Page<Profile> findByStatus(String status, Pageable pageable); // Moved to
    // CorpsMemberData

    Page<Profile> findByLastnameContainingIgnoreCase(String lastname, Pageable pageable);
}
