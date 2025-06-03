package com.fuxuras.patisoru.repositories;

import com.fuxuras.patisoru.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    @Query("SELECT vt from VerificationToken vt where vt.user.email = :email")
    Optional<VerificationToken> findByUserEmail(String email);
}
