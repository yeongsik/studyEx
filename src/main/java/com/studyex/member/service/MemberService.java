package com.studyex.member.service;

import com.studyex.member.dto.SignUpRequest;
import com.studyex.member.entity.Member;

public interface MemberService {

    Member signUp(SignUpRequest req);
}
