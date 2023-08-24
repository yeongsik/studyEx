package com.studyex.post.dto;

import com.studyex.member.entity.Member;
import com.studyex.post.entity.Post;
import com.studyex.post.entity.PostState;
import com.studyex.post.entity.PostType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ViewPostResponse {

    private Long id;

    private String title;

    private String contents;

    private Member writer;

    private PostType type;

    private PostState state;

    private Member firstChecker;

    private List<Member> workers;

    @Builder
    public ViewPostResponse(Long id, String title, String contents, Member writer, PostType type, PostState state, Member firstChecker, List<Member> workers) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.type = type;
        this.state = state;
        this.firstChecker = firstChecker;
        this.workers = workers;
    }

    public static ViewPostResponse of(Post post) {
        return ViewPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .writer(post.getWriter())
                .type(post.getType())
                .state(post.getState())
                .firstChecker(post.getFirstChecker())
                .workers(post.getWorkers())
                .build();
    }
}
