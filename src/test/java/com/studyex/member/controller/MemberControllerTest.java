package com.studyex.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyex.global.error.code.CommonErrorCode;
import com.studyex.global.error.code.MemberErrorCode;
import com.studyex.global.error.exception.RestApiException;
import com.studyex.member.dto.*;
import com.studyex.member.entity.Member;
import com.studyex.member.entity.MemberType;
import com.studyex.member.fixture.MemberFixture;
import com.studyex.member.repository.MemberRepository;
import com.studyex.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void clearMember() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void successSignUp() throws Exception {
        String json = objectMapper.writeValueAsString(MemberFixture.SUCCESS_SIGN_UP_REQ);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void failSignUpBecauseDuplicateEmail() throws Exception {
        memberService.signUp(MemberFixture.SUCCESS_SIGN_UP_REQ);
        String json = objectMapper.writeValueAsString(MemberFixture.SUCCESS_SIGN_UP_REQ);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.DUPLICATE_EMAIL.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.DUPLICATE_EMAIL.getMessage()));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 비밀번호 확인 불일치")
    void failSignUpBecauseNotSamePwdAndPwdConfirm() throws Exception {
        String json = objectMapper.writeValueAsString(MemberFixture.NOT_SAME_PWD_PWD_CONFIRM_SIGN_UP_REQ);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.NOT_SAME_PASSWORD_PASSWORD_CONFIRM.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.NOT_SAME_PASSWORD_PASSWORD_CONFIRM.getMessage()));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 미입력")
    void failSignUpBecauseEmptyEmail() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .name("테스트 이름")
                .password("test12345")
                .passwordConfirm("test12345")
                .phoneNumber("01012345678")
                .build();

        String json = objectMapper.writeValueAsString(req);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("email"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("이메일을 입력해주세요."));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 패턴이 아닌 경우")
    void failSignUpBecauseNotEmailPattern() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .email("testEmail")
                .name("테스트 이름")
                .password("test12345")
                .passwordConfirm("test12345")
                .phoneNumber("01012345678")
                .build();

        String json = objectMapper.writeValueAsString(req);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("email"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("이메일 형식이 아닙니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - 이름 미입력")
    void failSignUpBecauseEmptyName() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .email("test@gmail")
                .password("test12345")
                .passwordConfirm("test12345")
                .phoneNumber("01012345678")
                .build();

        String json = objectMapper.writeValueAsString(req);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("이름을 입력해주세요."));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 미입력")
    void failSignUpBecauseEmptyPassword() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .email("test@gmail")
                .name("테스트이름")
                .passwordConfirm("test12345")
                .phoneNumber("01012345678")
                .build();

        String json = objectMapper.writeValueAsString(req);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("password"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("비밀번호를 입력해주세요."));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 미입력")
    void failSignUpBecauseEmptyPasswordConfirm() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .email("test@gmail")
                .name("테스트이름")
                .password("test12345")
                .phoneNumber("01012345678")
                .build();

        String json = objectMapper.writeValueAsString(req);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("passwordConfirm"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("비밀번호 확인을 입력해주세요."));
    }

    @Test
    @DisplayName("회원가입 실패 - 핸드폰 번호 미입력")
    void failSignUpBecauseEmptyPhoneNumber() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .email("test@gmail")
                .name("테스트이름")
                .password("test12345")
                .passwordConfirm("test12345")
                .build();

        String json = objectMapper.writeValueAsString(req);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("phoneNumber"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("핸드폰 번호를 입력해주세요."));
    }

    @Test
    @DisplayName("회원가입 실패 - 잘못된 핸드폰 패턴")
    void failSignUpBecauseNotValidPhoneNumberPattern() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .email("test@gmail")
                .name("테스트이름")
                .password("test12345")
                .passwordConfirm("test12345")
                .phoneNumber("01045676789780")
                .build();

        String json = objectMapper.writeValueAsString(req);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("phoneNumber"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("잘못된 형식 입니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - 전체 미입력")
    void failSignUpBecauseEmptyMemberReq() throws Exception {
        SignUpRequest req = SignUpRequest.builder()
                .build();

        String json = objectMapper.writeValueAsString(req);
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field", containsInAnyOrder("email", "name", "password", "passwordConfirm", "phoneNumber")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message", containsInAnyOrder("이메일을 입력해주세요.", "이름을 입력해주세요.", "비밀번호를 입력해주세요.", "비밀번호 확인을 입력해주세요.", "핸드폰 번호를 입력해주세요.")));
    }

    @Test
    @DisplayName("회원정보 조회(ID) 성공")
    void successFindMemberById() throws Exception {
        // before
        Member req = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(req);

        // expected
        mockMvc.perform(get("/members/" + save.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(save.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(save.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(save.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(save.getPhoneNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.memberType").value(save.getMemberType().name()));
    }

    @Test
    @DisplayName("회원정보 조회(ID) 실패")
    void failFindMemberById() throws Exception {
        // before
        Member req = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        memberRepository.save(req);

        // expected
        mockMvc.perform(get("/members/0"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.EMPTY_MEMBER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.EMPTY_MEMBER.getMessage()));
    }

    @Test
    @DisplayName("회원정보 수정 성공")
    void successEditMember() throws Exception {
        //before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        EditMemberRequest req = EditMemberRequest.builder()
                .phoneNumber("01056781234")
                .build();

        String json = objectMapper.writeValueAsString(req);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/members/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @DisplayName("회원정보 수정 실패 - 존재하지 않은 회원")
    void failEditMemberBecauseNotFoundMember() throws Exception {

        // before
        EditMemberRequest req = EditMemberRequest.builder()
                .phoneNumber("01012345678")
                .build();

        String json = objectMapper.writeValueAsString(req);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/members/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.EMPTY_MEMBER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.EMPTY_MEMBER.getMessage()));
    }

    @Test
    @DisplayName("회원정보 수정 실패 - 핸드폰 번호 미입력")
    void failEditMemberBecauseEmptyPhoneNumber() throws Exception {

        // before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        EditMemberRequest req = EditMemberRequest.builder()
                .build();

        String json = objectMapper.writeValueAsString(req);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/members/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("phoneNumber"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("핸드폰 번호를 입력해주세요."));
    }

    @Test
    @DisplayName("회원정보 수정 실패 - 잘못된 핸드폰 패턴")
    void failEditMemberBecausePhoneNumberPattern() throws Exception {

        // before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        EditMemberRequest req = EditMemberRequest.builder()
                .phoneNumber("0101234567878978")
                .build();

        String json = objectMapper.writeValueAsString(req);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/members/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("phoneNumber"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("잘못된 형식 입니다."));
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void successEditPwd() throws Exception {
        // before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        EditPwdRequest req = EditPwdRequest.builder()
                .curPassword(save.getPassword())
                .newPassword("test23456")
                .passwordConfirm("test23456")
                .build();

        String json = objectMapper.writeValueAsString(req);

        // expected
        mockMvc.perform(patch("/members/" + save.getId() + "/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 현재 비밀번호 미일치")
    void failEditPwdBecauseNotSameCurPwd() throws Exception {
        // before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        EditPwdRequest req = EditPwdRequest.builder()
                .curPassword("test123")
                .newPassword("test23456")
                .passwordConfirm("test23456")
                .build();

        String json = objectMapper.writeValueAsString(req);

        // expected
        mockMvc.perform(patch("/members/" + save.getId() + "/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.NOT_VALID_PWD.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.NOT_VALID_PWD.getMessage()));
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 새 비밀번호와 비밀번호 확인 미일치")
    void failEditPwdBecauseNotSameNewPwdAndPwdConfirm() throws Exception {
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        EditPwdRequest req = EditPwdRequest.builder()
                .curPassword("test12345")
                .newPassword("test23456")
                .passwordConfirm("test23456789")
                .build();

        String json = objectMapper.writeValueAsString(req);

        // expected
        mockMvc.perform(patch("/members/" + save.getId() + "/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.NOT_SAME_PASSWORD_PASSWORD_CONFIRM.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.NOT_SAME_PASSWORD_PASSWORD_CONFIRM.getMessage()));

    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 전체 미 입력")
    void failEditPwdBecauseEmptyAll() throws Exception {
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        EditPwdRequest req = EditPwdRequest.builder()
                .build();

        String json = objectMapper.writeValueAsString(req);

        // expected
        mockMvc.perform(patch("/members/" + save.getId() + "/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field", containsInAnyOrder("curPassword", "newPassword", "passwordConfirm")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message", containsInAnyOrder("현재 비밀번호를 입력해주세요.", "새 비밀번호를 입력해주세요.", "비밀번호 확인을 입력해주세요.")));
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 존재하지 않는 회원")
    void failEditPwdBecauseNotFoundMember() throws Exception {
        // before
        EditPwdRequest req = EditPwdRequest.builder()
                .curPassword("test123")
                .newPassword("test23456")
                .passwordConfirm("test23456")
                .build();

        String json = objectMapper.writeValueAsString(req);

        // expected
        mockMvc.perform(patch("/members/0/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.EMPTY_MEMBER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.EMPTY_MEMBER.getMessage()));
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void successDeleteMember() throws Exception {

        // before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);
        DeleteMemberRequest deleteMemberRequest = DeleteMemberRequest.builder()
                .password("test12345")
                .build();

        String json = objectMapper.writeValueAsString(deleteMemberRequest);

        // expected
        mockMvc.perform(delete("/members/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 조회되지 않는 회원")
    void failDeleteMemberBecauseEmptyMember() throws Exception {

        // before
        DeleteMemberRequest deleteMemberRequest = DeleteMemberRequest.builder()
                .password("test12345")
                .build();

        String json = objectMapper.writeValueAsString(deleteMemberRequest);

        // expected
        mockMvc.perform(delete("/members/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.EMPTY_MEMBER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.EMPTY_MEMBER.getMessage()));
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 틀린 비밀번호")
    void failDeleteMemberBecauseNotValidatePwd() throws Exception {

        // before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);
        DeleteMemberRequest deleteMemberRequest = DeleteMemberRequest.builder()
                .password("test12345789")
                .build();

        String json = objectMapper.writeValueAsString(deleteMemberRequest);

        // expected
        mockMvc.perform(delete("/members/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.NOT_VALID_PWD.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.NOT_VALID_PWD.getMessage()));
    }

    @Test
    @DisplayName("회원탈퇴 실패 - 비밀번호 미 입력")
    void failDeleteMemberBecauseEmptyPwd() throws Exception {

        // before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);
        DeleteMemberRequest deleteMemberRequest = DeleteMemberRequest.builder()
                .password("")
                .build();

        String json = objectMapper.writeValueAsString(deleteMemberRequest);

        // expected
        mockMvc.perform(delete("/members/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("password"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("비밀번호를 입력해주세요."));

    }

    @Test
    @DisplayName("로그인 성공")
    void successLogin() throws Exception {

        //before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        LoginRequest loginRequest = LoginRequest.builder()
                .email(save.getEmail())
                .password(save.getPassword())
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);
        MockHttpSession session = new MockHttpSession();

        // expected
        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .session(session))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");

        assertEquals(member.getEmail(), loginUser.getEmail());
        assertEquals(member.getName(), loginUser.getName());
        assertEquals(member.getMemberType().name(), loginUser.getMemberType().name());
        assertEquals(member.getPhoneNumber(), loginUser.getPhoneNumber());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 회원")
    void failLoginBecauseEmptyMember() throws Exception {

        //before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test2@gmail.com")
                .password(save.getPassword())
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);

        // expected
        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.EMPTY_MEMBER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.EMPTY_MEMBER.getMessage()));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void failLoginBecauseNotValidatePwd() throws Exception {

        //before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        LoginRequest loginRequest = LoginRequest.builder()
                .email(save.getEmail())
                .password("test123432543")
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);

        // expected
        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.NOT_VALID_PWD.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.NOT_VALID_PWD.getMessage()));
    }

    @Test
    @DisplayName("로그인 실패 - 이메일, 비밀번호 미 입력")
    void failLoginBecauseBlankEmailAndPwd() throws Exception {

        //before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        memberRepository.save(member);

        LoginRequest loginRequest = LoginRequest.builder()
                .email("")
                .password("")
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);

        // expected
        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CommonErrorCode.INVALID_PARAMETER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field", containsInAnyOrder("email", "password")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message", containsInAnyOrder("이메일을 입력해주세요.", "비밀번호를 입력해주세요.")));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void successLogout() throws Exception {

        //before
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(member);

        LoginRequest loginRequest = LoginRequest.builder()
                .email(save.getEmail())
                .password(save.getPassword())
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .session(session))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertNotNull(session.getAttribute("loginUser"));

        // expected
        mockMvc.perform(get("/members/logout")
                        .session(session))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertNull(session.getAttribute("loginUser"));
    }

    @Test
    @DisplayName("로그아웃 실패")
    void failLogout() throws Exception {

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/members/logout")
                        .session(session))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(MemberErrorCode.NOT_LOGIN_MEMBER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(MemberErrorCode.NOT_LOGIN_MEMBER.getMessage()));
    }
}
