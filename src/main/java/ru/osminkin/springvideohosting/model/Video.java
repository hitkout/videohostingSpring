package ru.osminkin.springvideohosting.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "filename")
    private String filename;
    @Column(name = "video_title")
    private String videoTitle;
    @Column(name = "video_description")
    private String videoDescription;
    @Column(name = "likes")
    private Long likes;
    @Column(name = "dislikes")
    private Long dislikes;
    @JoinColumn(name = "add_date")
    private Timestamp addDate;
}
