package com.studyex.post.dto;

import com.studyex.member.entity.Member;
import com.studyex.post.entity.PostState;
import com.studyex.post.entity.PostType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class WritePostRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "분류를 선택해주세요.")
    private PostType type;

    @NotBlank(message = "로그인이 필요합니다.")
    private Member writer;

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    @Builder
    public WritePostRequest(String title, PostType type, Member writer, String contents) {
        this.title = title;
        this.type = type;
        this.writer = writer;
        this.contents = contents;
    }
}
