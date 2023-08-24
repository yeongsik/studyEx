package com.studyex.post.dto;

import com.studyex.member.dto.LoginMemberInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewPostRequest {

    private Long postId;
    private LoginMemberInfo loginMemberInfo;


}
