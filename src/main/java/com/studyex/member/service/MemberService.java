package com.studyex.member.service;

import com.studyex.member.dto.*;

public interface MemberService {

    void signUp(SignUpRequest req);

    FindMemberResponse findMemberByEmail(String mail);

    FindMemberResponse findMemberById(Long id);

    void editMember(Long id, EditMemberRequest req);

    void changePwd(Long id, EditPwdRequest req);

    void deleteMember(Long id, DeleteMemberRequest deleteMemberRequest);

    LoginResponse login(LoginRequest loginRequest);
}
