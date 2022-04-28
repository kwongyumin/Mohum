package com.hanghae99.finalprooject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Post extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String content;

    @Column(nullable = false)
    private String deadline;

    @Enumerated(EnumType.STRING)
    private CurrentStatus currentStatus;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String region;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Img> imgList = new ArrayList<>();

    public Post(String title, String content, String deadline, CurrentStatus currentStatus, String region, String category, User user) {
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.currentStatus = currentStatus;
        this.region = region;
        this.category = category;
        this.user = user;
    }
}
