package com.studyex.member.service;

import com.studyex.member.dto.SignUpRequest;
import com.studyex.member.dto.FindMemberResponse;
import com.studyex.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberServiceImpl memberService;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void deleteMemberAll() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void successSignUp() {

        SignUpRequest memberReq = SignUpRequest.builder()
                .email("test@gmail.com")
                .name("테스트 닉네임")
                .password("test1234")
                .passwordConfirm("test1234")
                .phoneNumber("01012345678")
                .build();

        memberService.signUp(memberReq);
        FindMemberResponse findMember = memberService.findMemberByEmail("test@gmail.com");

        assertEquals(memberReq.getEmail(), findMember.getEmail());
        assertEquals(memberReq.getName(), findMember.getName());
        assertEquals(memberReq.getPhoneNumber(), findMember.getPhoneNumber());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void failSignUpBecauseDuplicateEmail() {

        SignUpRequest memberReq1 = SignUpRequest.builder()
                .email("test@gmail.com")
                .name("테스트 닉네임")
                .password("test1234")
                .passwordConfirm("test1234")
                .phoneNumber("01012345678")
                .build();
        memberService.signUp(memberReq1);

        SignUpRequest memberReq2 = SignUpRequest.builder()
                .email("test@gmail.com")
                .name("테스트 닉네임2")
                .password("test1234")
                .passwordConfirm("test1234")
                .phoneNumber("01012345679")
                .build();

        assertThrows(RuntimeException.class, () -> {
            memberService.signUp(memberReq2);
        }, "중복된 이메일입니다.");
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 - 비밀번호 확인 불일치")
    void failSignUpBecauseNotSamePwdAndPwdConfirm() {
        SignUpRequest memberReq = SignUpRequest.builder()
                .email("test@gmail.com")
                .name("테스트 닉네임")
                .password("test1234")
                .passwordConfirm("test12345678")
                .phoneNumber("01012345678")
                .build();

        assertThrows(RuntimeException.class, () -> {
            memberService.signUp(memberReq);
        }, "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }
}