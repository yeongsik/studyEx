package com.studyex.post.entity;

import javax.persistence.*;

@Entity
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Path;

    private String viewName;

    private String saveName;

    private Integer size;

    private String extension;
}
