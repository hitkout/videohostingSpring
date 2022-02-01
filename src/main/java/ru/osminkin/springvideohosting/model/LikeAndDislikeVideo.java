package ru.osminkin.springvideohosting.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "like_and_dislike_video")
public class LikeAndDislikeVideo {
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