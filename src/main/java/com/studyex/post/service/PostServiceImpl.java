package com.studyex.post.service;

import com.studyex.global.error.code.PostErrorCode;
import com.studyex.global.error.exception.RestApiException;
import com.studyex.member.dto.LoginMemberInfo;
import com.studyex.member.entity.Member;
import com.studyex.member.entity.MemberType;
import com.studyex.member.service.MemberService;
import com.studyex.post.dto.ViewPostRequest;
import com.studyex.post.dto.ViewPostResponse;
import com.studyex.post.dto.WritePostRequest;
import com.studyex.post.entity.Post;
import com.studyex.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;


    @Override
    public void createPost(WritePostRequest writePostRequest) {
        postRepository.save(Post.of(writePostRequest));
    }

    @Override
    @Transactional
    public ViewPostResponse getPostView(ViewPostRequest viewPostRequest) {
        Post findPost = postRepository.findById(viewPostRequest.getPostId()).orElseThrow(
                () -> new RestApiException(PostErrorCode.EMPTY_POST)
        );

        // findPost의 firstChecker가 없는 경우 && 로그인 유저가 WORKER일 경우 해당
        LoginMemberInfo loginMember = viewPostRequest.getLoginMemberInfo();
        if (findPost.getFirstChecker() == null && !loginMember.getMemberType().equals(MemberType.CUSTOMER)) {
            findPost.updateFirstChecker(Member.of(loginMember));
        }
        return ViewPostResponse.of(findPost);
    }

}
