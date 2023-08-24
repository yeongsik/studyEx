package com.studyex.member.service;


import com.studyex.global.error.code.MemberErrorCode;
import com.studyex.global.error.exception.RestApiException;
import com.studyex.member.dto.*;
import com.studyex.member.entity.Member;
import com.studyex.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public void signUp(SignUpRequest memberReq) {
        // 이메일 중복 확인
        if (validateMemberRequest(memberReq)) {
            Member member = Member.of(memberReq);
            memberRepository.save(member);
        }
    }

    private boolean validateMemberRequest(SignUpRequest memberReq) {

        if (isNotSamePwdAndPwdConfirm(memberReq)) {
            throw new RestApiException(MemberErrorCode.NOT_SAME_PASSWORD_PASSWORD_CONFIRM);
        }

        if (isDuplicateEmail(memberReq.getEmail())) {
            throw new RestApiException(MemberErrorCode.DUPLICATE_EMAIL);
        }

        return true;
    }

    private boolean isDuplicateEmail(String email) {
        FindMemberResponse findMember = findMemberByEmail(email);

        return !"empty".equals(findMember.getEmail());
    }

    private boolean isNotSamePwdAndPwdConfirm(SignUpRequest memberReq) {
        return !memberReq.getPassword().equals(memberReq.getPasswordConfirm());
    }

    @Override
    public FindMemberResponse findMemberByEmail(String mail) {
        Member findMember = memberRepository.findByEmail(mail)
                .orElse(Member.builder()
                        .id(0L)
                        .email("empty")
                        .name("empty")
                        .password("empty")
                        .phoneNumber("empty")
                        .build());

        return FindMemberResponse.of(findMember);
    }

    @Override
    public FindMemberResponse findMemberById(Long id) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.EMPTY_MEMBER));
        return FindMemberResponse.of(findMember);
    }

    @Override
    @Transactional
    public void editMember(Long id, EditMemberRequest editMemberRequest) {
        Member findMember = memberRepository.findById(id).orElseThrow(
                () -> new RestApiException(MemberErrorCode.EMPTY_MEMBER)
        );
        findMember.editMember(editMemberRequest);
    }

    @Override
    @Transactional
    public void changePwd(Long id, EditPwdRequest editPwdRequest) {
        Member findMember = memberRepository.findById(id).orElseThrow(
                () -> new RestApiException(MemberErrorCode.EMPTY_MEMBER)
        );

        if (!findMember.getPassword().equals(editPwdRequest.getCurPassword())) {
            throw new RestApiException(MemberErrorCode.NOT_VALID_PWD);
        }

        if (!editPwdRequest.getNewPassword().equals(editPwdRequest.getPasswordConfirm())) {
            throw new RestApiException(MemberErrorCode.NOT_SAME_PASSWORD_PASSWORD_CONFIRM);
        }

        findMember.editPassword(editPwdRequest);
    }

    @Override
    @Transactional
    public void deleteMember(Long id, DeleteMemberRequest deleteMemberRequest) {
        Member findMember = memberRepository.findById(id).orElseThrow(
                () -> new RestApiException(MemberErrorCode.EMPTY_MEMBER)
        );

        if (!findMember.getPassword().equals(deleteMemberRequest.getPassword())) {
            throw new RestApiException(MemberErrorCode.NOT_VALID_PWD);
        }
        memberRepository.delete(findMember);
    }

    @Override
    public LoginMemberInfo login(LoginRequest loginRequest) {
        Member findMember = memberRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new RestApiException(MemberErrorCode.EMPTY_MEMBER)
        );

        if (!findMember.getPassword().equals(loginRequest.getPassword())) {
            throw new RestApiException(MemberErrorCode.NOT_VALID_PWD);
        }

        return LoginMemberInfo.of(findMember);
    }
}
