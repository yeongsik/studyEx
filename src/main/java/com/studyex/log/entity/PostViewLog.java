package com.studyex.log.entity;

import com.studyex.member.entity.Member;
import com.studyex.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class PostViewLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Member member;

    private LocalDateTime regDate;

    @Builder
    public PostViewLog(Long id, Post post, Member member, LocalDateTime regDate) {
        this.id = id;
        this.post = post;
        this.member = member;
        this.regDate = regDate;
    }
}
