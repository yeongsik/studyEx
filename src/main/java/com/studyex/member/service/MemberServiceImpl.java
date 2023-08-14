package com.studyex.member.service;


import com.studyex.member.dto.SignUpRequest;
import com.studyex.member.entity.Member;
import com.studyex.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;

    public Member signUp(SignUpRequest memberReq) {
        // 이메일 중복 확인

        // 비밀번호와 비밀번호 확인 동일한지 체크

        Member member = Member.of(memberReq);

        return memberRepository.save(member);
    }

}
