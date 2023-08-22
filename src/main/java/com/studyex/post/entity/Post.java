package com.studyex.post.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String type;

    private String worker;

    private String firstChecker;

    private String state;

    private String contents;

    @Builder
    public Post(Long id, String title, String type, String worker, String firstChecker, String state, String contents) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.worker = worker;
        this.firstChecker = firstChecker;
        this.state = state;
        this.contents = contents;
    }
}
