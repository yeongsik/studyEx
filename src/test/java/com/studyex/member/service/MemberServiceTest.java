package com.studyex.member.service;

import com.studyex.global.error.code.MemberErrorCode;
import com.studyex.global.error.exception.RestApiException;
import com.studyex.member.dto.*;
import com.studyex.member.entity.Member;
import com.studyex.member.entity.MemberType;
import com.studyex.member.fixture.MemberFixture;
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
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void deleteMemberAll() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void successSignUp() {
        memberService.signUp(MemberFixture.SUCCESS_SIGN_UP_REQ);
        FindMemberResponse findMember = memberService.findMemberByEmail(MemberFixture.SUCCESS_SIGN_UP_REQ.getEmail());
        assertEquals(MemberFixture.SUCCESS_SIGN_UP_REQ.getEmail(), findMember.getEmail());
        assertEquals(MemberFixture.SUCCESS_SIGN_UP_REQ.getName(), findMember.getName());
        assertEquals(MemberFixture.SUCCESS_SIGN_UP_REQ.getPhoneNumber(), findMember.getPhoneNumber());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void failSignUpBecauseDuplicateEmail() {
        memberService.signUp(MemberFixture.SUCCESS_SIGN_UP_REQ);

        assertThrows(RestApiException.class, () -> {
            memberService.signUp(MemberFixture.SUCCESS_SIGN_UP_REQ);
        }, MemberErrorCode.DUPLICATE_EMAIL.getMessage());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 - 비밀번호 확인 불일치")
    void failSignUpBecauseNotSamePwdAndPwdConfirm() {
        assertThrows(RestApiException.class, () -> {
            memberService.signUp(MemberFixture.NOT_SAME_PWD_PWD_CONFIRM_SIGN_UP_REQ);
        }, MemberErrorCode.NOT_SAME_PASSWORD_PASSWORD_CONFIRM.getMessage());
    }

    // 회원 정보 조회
    @Test
    @DisplayName("회원정보 조회(ID) - 성공")
    void successFindMemberById() {
        // before
        Member req = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        Member save = memberRepository.save(req);
        // when
        FindMemberResponse findMember = memberService.findMemberById(save.getId());

        // then
        assertEquals(req.getEmail(), findMember.getEmail());
        assertEquals(req.getName(), findMember.getName());
        assertEquals(req.getPhoneNumber(), findMember.getPhoneNumber());
        assertEquals(req.getMemberType().name(), findMember.getMemberType().name());
    }

    @Test
    @DisplayName("회원정보 조회(ID) - 실패")
    void failFindMemberById() {
        // before
        Member req = Member.builder()
                .email("test@gmail.com")
                .name("테스트 네임")
                .password("test12345")
                .phoneNumber("01012345678")
                .memberType(MemberType.CUSTOMER)
                .build();

        memberRepository.save(req);
        // when
        assertThrows(RestApiException.class, () -> {
            memberService.findMemberById(0L);
        }, MemberErrorCode.EMPTY_MEMBER.getMessage());
    }

    @Test
    @DisplayName("회원정보 수정 성공")
    void successEditMember() {
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

        //when
        memberService.editMember(save.getId(), req);

        //then
        FindMemberResponse findMember = memberService.findMemberById(save.getId());

        assertNotEquals(member.getPhoneNumber(), findMember.getPhoneNumber());
        assertEquals(member.getEmail(), findMember.getEmail());
        assertEquals(member.getName(), findMember.getName());
        assertEquals(member.getMemberType().name(), findMember.getMemberType().name());
        assertEquals(member.getPassword(), findMember.getPassword());
    }

    @Test
    @DisplayName("회원 정보 수정 실패 - 존재하지 않는 유저 ")
    void failEditMemberBecauseNotFoundMember() {

        // before
        EditMemberRequest req = EditMemberRequest.builder()
                .phoneNumber("01012345678")
                .build();

        // when
        assertThrows(RestApiException.class, () -> {
            memberService.editMember(0L, req);
        }, MemberErrorCode.EMPTY_MEMBER.getMessage());
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void successEditPwd() {
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

        // when
        memberService.changePwd(save.getId(), req);


        // then
        FindMemberResponse findMember = memberService.findMemberById(save.getId());
        assertNotEquals(save.getPassword(), findMember.getPassword());
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 현재 비밀번호 미일치")
    void failEditPwdBecauseNotSameCurPwd() {
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

        // when
        assertThrows(RestApiException.class, () -> {
            memberService.changePwd(save.getId(), req);
        }, MemberErrorCode.NOT_VALID_PWD.getMessage());
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 새 비밀번호와 비밀번호 확인 미일치")
    void failEditPwdBecauseNotSameNewPwdAndPwdConfirm() {
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
                .curPassword("test12345")
                .newPassword("test23456")
                .passwordConfirm("test23456789")
                .build();

        // when
        assertThrows(RestApiException.class, () -> {
            memberService.changePwd(save.getId(), req);
        }, MemberErrorCode.NOT_SAME_PASSWORD_PASSWORD_CONFIRM.getMessage());

    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 존재하지 않는 회원")
    void failEditPwdBecauseNotFoundMember() {
        // before
        EditPwdRequest req = EditPwdRequest.builder()
                .curPassword("test123")
                .newPassword("test23456")
                .passwordConfirm("test23456")
                .build();

        // when
        assertThrows(RestApiException.class, () -> {
            memberService.changePwd(0L, req);
        }, MemberErrorCode.EMPTY_MEMBER.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void successDeleteMember() {

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

        // when
        memberService.deleteMember(save.getId(), deleteMemberRequest);

        // then
        assertThrows(RestApiException.class, () -> {
            memberService.findMemberById(save.getId());
        }, MemberErrorCode.EMPTY_MEMBER.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 조회되지 않는 회원")
    void failDeleteMemberBecauseEmptyMember() {

        // before
        DeleteMemberRequest deleteMemberRequest = DeleteMemberRequest.builder()
                .password("test12345")
                .build();

        // when
        assertThrows(RestApiException.class, () -> {
            memberService.deleteMember(0L, deleteMemberRequest);
        }, MemberErrorCode.EMPTY_MEMBER.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 틀린 비밀번호")
    void failDeleteMemberBecauseNotValidatePwd() {

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

        // when
        assertThrows(RestApiException.class, () -> {
            memberService.deleteMember(save.getId(), deleteMemberRequest);
        }, MemberErrorCode.NOT_VALID_PWD.getMessage());
    }

    @Test
    @DisplayName("로그인 성공")
    void successLogin() {

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

        // when
        LoginMemberInfo response = memberService.login(loginRequest);

        // then
        assertEquals(save.getId(), response.getId());
        assertEquals(save.getEmail(), response.getEmail());
        assertEquals(save.getName(), response.getName());
        assertEquals(save.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(save.getMemberType().name(), response.getMemberType().name());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 회원")
    void failLoginBecauseEmptyMember() {

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

        // when
        assertThrows(RestApiException.class, () -> {
            memberService.login(loginRequest);
        }, MemberErrorCode.EMPTY_MEMBER.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void failLoginBecauseNotValidatePwd() {

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

        // when
        assertThrows(RestApiException.class, () -> {
            memberService.login(loginRequest);
        }, MemberErrorCode.NOT_VALID_PWD.getMessage());
    }
}