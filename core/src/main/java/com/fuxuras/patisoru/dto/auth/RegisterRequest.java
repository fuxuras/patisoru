package com.fuxuras.patisoru.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Lütfen emailinizi giriniz")
    @Email(message = "Lütfen geçerli bir email formatı giriniz")
    private String email;

    @NotBlank(message = "Lütfen şifre giriniz")
    @Size(min = 8, message = "Lütfen en az 8 karakter uzunluğunda bir şifre giriniz")
    private String password;

    @NotBlank(message = "Lütfen tam isminizi giriniz")
    private String fullName;
}
