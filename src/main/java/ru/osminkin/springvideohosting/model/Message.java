package ru.osminkin.springvideohosting.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "posts")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @Column(name = "tag")
    private String tag;
    @Column(name = "filename")
    private String filename;
}
