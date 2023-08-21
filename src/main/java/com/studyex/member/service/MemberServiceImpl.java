package com.studyex.member.service;


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
            throw new RuntimeException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        if (isDuplicateEmail(memberReq.getEmail())) {
            throw new RuntimeException("중복된 이메일입니다.");
        }

        if (isDuplicateNickName(memberReq.getNickName())) {
            throw new RuntimeException("중복된 닉네임입니다.");
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

    private boolean isDuplicateNickName(String nickName) {
        FindMemberResponse findMember = findMemberByNickName(nickName);
        return !"empty".equals(findMember.getEmail());
    }

    public FindMemberResponse findMemberByEmail(String mail) {
        Member findMember = memberRepository.findByEmail(mail)
                .orElse(Member.builder()
                        .id(0L)
                        .email("empty")
                        .nickName("empty")
                        .password("empty")
                        .phoneNumber("empty")
                        .build());

        return FindMemberResponse.of(findMember);
    }

    public FindMemberResponse findMemberByNickName(String mail) {
        Member findMember = memberRepository.findByNickName(mail)
                .orElse(Member.builder()
                        .id(0L)
                        .email("empty")
                        .nickName("empty")
                        .password("empty")
                        .phoneNumber("empty")
                        .build());

        return FindMemberResponse.of(findMember);
    }
}
