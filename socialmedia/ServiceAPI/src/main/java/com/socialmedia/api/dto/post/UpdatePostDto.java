package com.socialmedia.api.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostDto {
    @NotBlank(message = "Invalid Title: Title cannot be empty")
    private String title;
    @NotBlank(message = "Invalid Post Text: Post Text cannot be empty")
    private String postText;
    private String image;
}
