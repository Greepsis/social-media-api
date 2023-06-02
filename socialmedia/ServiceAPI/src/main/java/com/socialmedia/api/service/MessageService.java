package com.socialmedia.api.service;

import com.socialmedia.api.dto.message.MessageDto;
import com.socialmedia.api.dto.message.page.PageMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MessageService {
    void sendMessage(MessageDto messageDto, String friendEmail);
    void deleteMessage(UUID message_id);
    Page<PageMessageDto> getAllMyMessage(Pageable pageable);
}
