package com.socialmedia.api.dto.subscriber;

import com.socialmedia.api.dto.user.page.PageUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageSubscriberDto {
    private PageUserDto follower;
    private PageUserDto following;
}
