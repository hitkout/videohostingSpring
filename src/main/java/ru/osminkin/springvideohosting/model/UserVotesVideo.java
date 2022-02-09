package ru.osminkin.springvideohosting.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "user_votes_video")
public class UserVotesVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "video_id")
    private Video videoId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "like_or_dislike")
    private String likeOrDislike;
}