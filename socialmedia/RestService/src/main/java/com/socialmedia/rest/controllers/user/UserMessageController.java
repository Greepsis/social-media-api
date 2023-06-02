package com.socialmedia.rest.controllers.user;

import com.socialmedia.api.dto.ResponseDto;
import com.socialmedia.api.dto.friend.PageFriendDto;
import com.socialmedia.api.dto.message.MessageDto;
import com.socialmedia.api.dto.message.page.PageMessageDto;
import com.socialmedia.api.dto.post.PostDto;
import com.socialmedia.api.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/socialmedia/message")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Message Controller", description = "Allows you send,delete,view message")
public class UserMessageController {
    private final MessageService messageService;

    @PostMapping("/{friendEmail}")
    @Operation(
            summary = "Allows you send message to your friend." +
                    " Need put in message dto your friendRequest_id(friend_id from Friend.class)"
    )
    public ResponseEntity<?> sendMessage(@RequestBody @Valid MessageDto messageDto,
                                         @PathVariable String friendEmail){
        messageService.sendMessage(messageDto,friendEmail);
        return new ResponseEntity<>(new ResponseDto<>(true, "Message has been send."), HttpStatus.OK);
    }

    @DeleteMapping("/{message_id}")
    @Operation(
            summary = "Allows you delete your message"
    )
    public ResponseEntity<?> deleteMessage(@PathVariable UUID message_id){
        messageService.deleteMessage(message_id);
        return new ResponseEntity<>(new ResponseDto<>(true,"Message has been deleted"),
                HttpStatus.OK);
    }

    @GetMapping("/my/message")
    @Operation(
            summary = "Allows you to get all your message "
    )
    public ResponseEntity<?> getMyMessage(@PageableDefault(sort = "publication_date_time", direction = Sort.Direction.DESC,
            page = 0, size = 10) Pageable pageable){
        final Page<PageMessageDto> page = messageService.getAllMyMessage(pageable);
        return ResponseEntity.ok(new ResponseDto<>(true, page));
    }
}
