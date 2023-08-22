package com.studyex.member.service;


import com.studyex.global.error.code.MemberErrorCode;
import com.studyex.global.error.exception.RestApiException;
import com.studyex.member.dto.SignUpRequest;
import com.studyex.member.dto.FindMemberResponse;
import com.studyex.member.entity.Member;
import com.studyex.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
}
