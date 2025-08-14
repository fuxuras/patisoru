package com.fuxuras.patisoru.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {
    @Size(max = 64, message = "64 karakterden kısa bir başlık giriniz")
    private String title;
    @Size(max = 512, message = "512 karakterden kısa bir metin giriniz")
    private String text;
}
