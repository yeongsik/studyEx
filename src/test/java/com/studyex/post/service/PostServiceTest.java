package com.studyex.post.service;

import com.studyex.global.error.code.PostErrorCode;
import com.studyex.global.error.exception.RestApiException;
import com.studyex.member.dto.LoginMemberInfo;
import com.studyex.member.entity.Member;
import com.studyex.member.entity.MemberType;
import com.studyex.member.repository.MemberRepository;
import com.studyex.post.dto.ViewPostRequest;
import com.studyex.post.dto.ViewPostResponse;
import com.studyex.post.dto.WritePostRequest;
import com.studyex.post.entity.Post;
import com.studyex.post.entity.PostState;
import com.studyex.post.entity.PostType;
import com.studyex.post.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PostServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @AfterEach
    void clear() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }
    @Test
    @DisplayName("게시글 작성 성공")
    void successCreatePost() {

        // before
        Member member = Member.builder()
                .email("test@gmail")
                .name("테스트네임")
                .memberType(MemberType.CUSTOMER)
                .password("test12345")
                .phoneNumber("01012345678")
                .build();

        memberRepository.save(member);


        WritePostRequest writePostRequest = WritePostRequest.builder()
                .title("테스트 게시글")
                .type(PostType.DEVELOPMENT)
                .writer(member)
                .contents("테스트 내용")
                .build();

        // when
        assertDoesNotThrow(() -> postService.createPost(writePostRequest));

    }

    // 게시글 작성 실패 - 권한 ( 유저가 아닌 경우 ) - 컨트롤러

    // 게시글 작성 실패 - 제목 길이 초과 - 컨트롤러

    // 게시글 작성 실패 - 내용 길이 초과 - 컨트롤러

    // 게시글 조회 성공
    @Test
    @DisplayName("게시글 조회 성공")
    void successGetPostView() {

        // before
        Member member = Member.builder()
                .email("test@gmail")
                .name("테스트네임")
                .memberType(MemberType.CUSTOMER)
                .password("test12345")
                .phoneNumber("01012345678")
                .build();

        memberRepository.save(member);


        Post post = Post.builder()
                .title("테스트 게시글")
                .type(PostType.DEVELOPMENT)
                .writer(member)
                .state(PostState.BEFORE_PROCESS)
                .contents("테스트 내용")
                .build();

        Post savedPost = postRepository.save(post);
        ViewPostRequest request = ViewPostRequest.builder()
                .postId(savedPost.getId())
                .loginMemberInfo(LoginMemberInfo.of(member))
                .build();

        // when
        ViewPostResponse postViewResponse = postService.getPostView(request);

        // then
        assertEquals(savedPost.getId() , postViewResponse.getId());
        assertEquals(savedPost.getTitle() , postViewResponse.getTitle());
        assertEquals(savedPost.getWriter() , postViewResponse.getWriter());
        assertEquals(savedPost.getType() , postViewResponse.getType());
        assertEquals(savedPost.getState() , postViewResponse.getState());
        assertEquals(savedPost.getContents() , postViewResponse.getContents());
    }

    @Test
    @DisplayName("게시글 조회 성공 - 회원이 고객이 아닐 때 최초 발견자 업데이트 및 처리중으로 상태 변경")
    void successGetPostViewWhenLoginUserIsNotCustomer() {

        // before
        Member member = Member.builder()
                .email("test@gmail")
                .name("테스트네임")
                .memberType(MemberType.CUSTOMER)
                .password("test12345")
                .phoneNumber("01012345678")
                .build();

        Member worker = Member.builder()
                .email("test2@gmail")
                .name("테스트네임")
                .memberType(MemberType.WORKER)
                .password("test12345")
                .phoneNumber("01012345678")
                .build();

        memberRepository.save(member);
        memberRepository.save(worker);

        Post post = Post.builder()
                .title("테스트 게시글")
                .type(PostType.DEVELOPMENT)
                .writer(member)
                .state(PostState.BEFORE_PROCESS)
                .contents("테스트 내용")
                .build();

        Post savedPost = postRepository.save(post);
        ViewPostRequest request = ViewPostRequest.builder()
                .postId(savedPost.getId())
                .loginMemberInfo(LoginMemberInfo.of(worker))
                .build();

        // when
        ViewPostResponse postViewResponse = postService.getPostView(request);

        // then
        assertEquals(savedPost.getId() , postViewResponse.getId());
        assertEquals(savedPost.getTitle() , postViewResponse.getTitle());
        assertEquals(savedPost.getWriter() , postViewResponse.getWriter());
        assertEquals(savedPost.getType() , postViewResponse.getType());
        assertEquals(savedPost.getContents() , postViewResponse.getContents());
        assertEquals(PostState.ING_PROCESS , postViewResponse.getState());
        assertEquals(worker.getMemberType(),postViewResponse.getFirstChecker().getMemberType());
    }

    // 게시글 조회 실패 - 허용되지 않은 사용자 (고객은 해당 게시글 작성자만 볼 수 있음) - 컨트롤러

    // 게시글 조회 실패 - 없는 게시글

    @Test
    @DisplayName("게시글 조회 실패 - 존재하지 않는 게시글")
    void failGetPostViewBecauseEmptyPost() {

        // before
        Member member = Member.builder()
                .email("test@gmail")
                .name("테스트네임")
                .memberType(MemberType.CUSTOMER)
                .password("test12345")
                .phoneNumber("01012345678")
                .build();

        memberRepository.save(member);

        ViewPostRequest request = ViewPostRequest.builder()
                .postId(0L)
                .loginMemberInfo(LoginMemberInfo.of(member))
                .build();

        // when
        assertThrows(RestApiException.class, () -> {
            postService.getPostView(request);
        }, PostErrorCode.EMPTY_POST.getMessage());
    }

    // 게시글 수정 성공

    // 게시글 수정 실패 - 허용되지 않은 사용자 (고객은 해당 게시글 작성자만 볼 수 있음) - 컨트롤러

    // 게시글 수정 실패 - 제목 길이 초과

    // 게시글 수정 실패 - 내용 길이 초과

    // 게시글 삭제 성공

    // 게시글 삭제 실패 - 허용 되지 않은 사용자

    // 게시글 삭제 실패 - 없는 게시글

}