package com.fuxuras.patisoru.entities;

import com.fuxuras.patisoru.entities.abstracts.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken extends BaseEntity {

    @OneToOne
    private User user;

    private String token;

    private LocalDateTime expiryDate;

    private static final int EXPIRATION_HOURS = 24;

    public void createVerificationToken(){
        SecureRandom random = new SecureRandom();
        this.token = new BigInteger(28, random).toString(32);
        this.expiryDate = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
    }
}
