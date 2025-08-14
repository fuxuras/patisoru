package com.fuxuras.patisoru.dto;

import lombok.Data;

@Data
public class EmailVerification {
    private String email;
    private String token;
}
