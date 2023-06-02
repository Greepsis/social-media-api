package com.socialmedia.api.dto.friend;

import com.socialmedia.api.dto.user.page.PageUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageFriendDto {
    private UUID friend_id;
    private PageUserDto sender;
    private PageUserDto receiver;
    private String status;
}
