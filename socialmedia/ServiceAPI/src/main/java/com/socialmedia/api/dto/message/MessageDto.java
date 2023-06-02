package com.socialmedia.api.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    @NotBlank(message = "Invalid Text Message: Text message cannot be empty")
    private String textMessage;
    @NotNull(message = "Invalid Friend Id: Friend Id cannot be empty")
    private UUID friendRequest_id;
}
