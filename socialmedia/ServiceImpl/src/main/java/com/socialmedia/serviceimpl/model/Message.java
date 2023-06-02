package com.socialmedia.serviceimpl.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.socialmedia.api.utils.DateUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID message_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_id", referencedColumnName = "user_id")
    private User recipient;
    @NotBlank
    private String textMessage;
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private LocalDateTime publicationDateTime;

}
