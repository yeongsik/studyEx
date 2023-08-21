package com.studyex.member.service;

import com.studyex.member.dto.FindMemberResponse;
import com.studyex.member.dto.SignUpRequest;
import com.studyex.member.entity.Member;

public interface MemberService {

    void signUp(SignUpRequest req);

    FindMemberResponse findMemberByEmail(String mail);
}
