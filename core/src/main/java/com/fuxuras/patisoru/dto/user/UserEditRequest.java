package com.fuxuras.patisoru.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEditRequest {
//    @NotBlank(message = "Email alanı boş olamaz")
//    @Email(message = "Geçerli bir email formatı giriniz")
//    private String email;
    
    @NotBlank(message = "İsim alanı boş olamaz")
    @Size(min = 2, max = 100, message = "İsim 2-100 karakter arasında olmalıdır")
    private String fullName;
}
