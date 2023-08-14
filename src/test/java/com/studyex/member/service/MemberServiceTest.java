package com.studyex.member.service;

import com.studyex.member.dto.SignUpRequest;
import com.studyex.member.entity.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberServiceImpl memberService;

    @Test
    @DisplayName("회원가입 성공")
    void successSignUp() {

        SignUpRequest memberReq = SignUpRequest.builder()
                .email("test@gmail.com")
                .nickName("테스트 닉네임")
                .password("test1234")
                .passwordConfirm("test1234")
                .phoneNumber("01012345678")
                .build();

        Member resultMember = memberService.signUp(memberReq);

        Assertions.assertEquals(memberReq.getEmail(), resultMember.getEmail());
    }

}