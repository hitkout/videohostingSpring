package ru.osminkin.springvideohosting.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "subscriptions")
public class Subscriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower")
    private User follower;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follow_user")
    private User followToUser;
}
