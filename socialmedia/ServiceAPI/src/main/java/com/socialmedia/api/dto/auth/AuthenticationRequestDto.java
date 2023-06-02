package com.socialmedia.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AuthenticationRequestDto {
    @Email(message = "Please, enter correct email")
    private String email;
    @NotBlank
    private String password;
}
