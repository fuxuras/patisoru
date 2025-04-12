package com.fuxuras.patisoru.dto;

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
public class CommentCreateRequest {
    @NotBlank(message = "Yorum metni boş olamaz")
    @Size(min = 1, max = 256, message = "Yorum 1-300 karakter arasında olmalıdır")
    private String text;
}
