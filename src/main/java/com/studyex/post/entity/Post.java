package com.studyex.post.entity;

import com.studyex.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String type;

    @OneToMany
    private List<Member> worker;

    @OneToOne
    private Member customer;

    @OneToOne
    private Member firstChecker;

    @Enumerated(EnumType.STRING)
    private PostState state;

    private String contents;

    @Builder
    public Post(Long id, String title, String type, List<Member> worker, Member customer, Member firstChecker, PostState state, String contents) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.worker = worker;
        this.customer = customer;
        this.firstChecker = firstChecker;
        this.state = state;
        this.contents = contents;
    }
}
