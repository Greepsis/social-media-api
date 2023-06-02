package com.socialmedia.serviceimpl.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.socialmedia.api.utils.DateUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id")
    private UUID post_id;
    @NotBlank
    @Column(name = "title")
    private String title;
    @NotBlank
    @Column(name = "post_text")
    private String postText;
    @Column(name = "image")
    private String image;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    @Column(name = "publication_date_time")
    private LocalDateTime publicationDateTime;


}
