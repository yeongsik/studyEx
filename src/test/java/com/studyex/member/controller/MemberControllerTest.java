package com.studyex.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyex.member.dto.SignUpRequest;
import com.studyex.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원가입 - 성공")
    void successSignUp() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .email("test@gmail.com")
                .name("테스트네임")
                .password("test12345")
                .passwordConfirm("test12345")
                .phoneNumber("01000000000")
                .build();

        String json = objectMapper.writeValueAsString(req);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // 회원 가입 실패 , 이메일 중복
    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void failSignUpBecauseDuplicateEmail() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .email("test@gmail.com")
                .name("테스트네임")
                .password("test12345")
                .passwordConfirm("test12345")
                .phoneNumber("01000000000")
                .build();

        memberService.signUp(req);

        SignUpRequest req2 = SignUpRequest.builder()
                .email("test@gmail.com")
                .name("테스트네임2")
                .password("test12345")
                .passwordConfirm("test12345")
                .phoneNumber("01000000001")
                .build();

        String json = objectMapper.writeValueAsString(req2);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    // 회원 가입 실패 , 비밀번호 비밀번호 확인 불일치
    @Test
    @DisplayName("회원가입 실패 - 비밀번호 비밀번호 확인 불일치")
    void failSignUpBecauseNotSamePwdAndPwdConfirm() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .email("test@gmail.com")
                .name("테스트닉네임")
                .password("test12345")
                .passwordConfirm("test12345789")
                .phoneNumber("01000000000")
                .build();

        String json = objectMapper.writeValueAsString(req);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    // 회원 가입 실패 - 이메일 미입력



    // 회원 가입 실패 - 이메일 패턴이 아닌 경우

    // 회원 가입 실패 - 닉네임 미입력

    // 회원 가입 실패 - 비밀번호 미입력

    // 회원 가입 실패 - 비밀번호 확인 미입력

    // 회원 가입 실패 - 핸드폰 번호 미입력

    // 회원 가입 실패 - 전체 미입력


}
