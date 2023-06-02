package com.socialmedia.api.dto.user;

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
public class UserDto {
    @NotBlank(message = "Invalid name: Name cannot be empty")
    @Size(min = 3, max = 30, message = "Invalid name: Must be of 3 - 30 characters")
    private String name;
    @Email(message = "Invalid email: Please,try again")
    @NotBlank(message ="Invalid email: Email cannot be empty")
    private String email;
    @NotBlank(message = "Invalid password: Password cannot be empty")
    private String password;
}
