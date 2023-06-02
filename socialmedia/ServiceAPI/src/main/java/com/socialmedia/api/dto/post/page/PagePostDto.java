package com.socialmedia.api.dto.post.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.socialmedia.api.dto.user.page.PageUserDto;
import com.socialmedia.api.utils.DateUtil;
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
public class PagePostDto {
    private UUID post_id;
    private String title;
    private String postText;
    private String image;
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private LocalDateTime publicationDateTime;
    private PageUserDto user;
}
