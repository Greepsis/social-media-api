package com.socialmedia.serviceimpl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscribers")
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID subscriber_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower_id", referencedColumnName = "user_id")
    private User follower;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "following_id", referencedColumnName = "user_id")
    private User following;
}
