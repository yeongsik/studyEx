package com.studyex.post.entity;

import com.studyex.member.entity.Member;
import com.studyex.post.dto.WritePostRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private PostType type;

    @ManyToMany
    private List<Member> workers;

    @ManyToOne
    private Member writer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Member firstChecker;

    @Enumerated(EnumType.STRING)
    private PostState state;

    private String contents;

    @OneToMany
    private List<PostFile> postFiles;

    @Builder
    public Post(Long id, String title, PostType type, List<Member> workers, Member writer, Member firstChecker, PostState state, String contents, List<PostFile> postFiles) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.workers = workers;
        this.writer = writer;
        this.firstChecker = firstChecker;
        this.state = state;
        this.contents = contents;
        this.postFiles = postFiles;
    }

    public static Post of(WritePostRequest writePostRequest) {
        return Post.builder()
                .title(writePostRequest.getTitle())
                .type(writePostRequest.getType())
                .writer(writePostRequest.getWriter())
                .state(PostState.BEFORE_PROCESS)
                .contents(writePostRequest.getContents())
                .build();
    }

    public void updateFirstChecker(Member member) {
        this.firstChecker = member;
        this.state = PostState.ING_PROCESS;
    }
}
