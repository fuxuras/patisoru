package com.fuxuras.patisoru.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.repository.cdi.Eager;

@Data
public class LoginRequest {
    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Lütfen geçerli bir email giriniz.")
    private String email;

    @NotBlank(message = "Şifreniz boş olamaz")
    private String password;
}
