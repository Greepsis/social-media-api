package com.socialmedia.api.dto.auth;

import lombok.Data;

@Data
public class AuthenticationResponseDto {
    private String email;
    private String token;
}
