package com.socialmedia.api.dto.message.page;

import com.socialmedia.api.dto.user.page.PageUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageMessageDto {
    private UUID message_id;
    private PageUserDto sender;
    private PageUserDto recipient;
    private String textMessage;
    private LocalDateTime publicationDateTime;
}
