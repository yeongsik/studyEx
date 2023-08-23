package com.studyex.member.controller;

import com.studyex.global.error.code.MemberErrorCode;
import com.studyex.global.error.exception.RestApiException;
import com.studyex.member.dto.*;
import com.studyex.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest memberReq) {
        memberService.signUp(memberReq);
        return ResponseEntity.ok().build();
    }

    // 회원 정보 조회 ( 마이페이지 )
    @GetMapping("/members/{id}")
    public ResponseEntity<?> getMemberInfo(@PathVariable Long id) {
        FindMemberResponse findMember = memberService.findMemberById(id);
        return ResponseEntity.ok(findMember);
    }

    @PatchMapping("/members/{id}")
    public ResponseEntity<Void> editMemberInfo(@PathVariable Long id, @RequestBody @Valid EditMemberRequest editMemberRequest) {
        memberService.editMember(id, editMemberRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/members/{id}/changePwd")
    public ResponseEntity<Void> changePwd(@PathVariable Long id, @RequestBody @Valid EditPwdRequest editPwdRequest) {
        memberService.changePwd(id, editPwdRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id, @RequestBody @Valid DeleteMemberRequest deleteMemberRequest) {
        memberService.deleteMember(id, deleteMemberRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/members/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpSession session) {
        session.setAttribute("loginUser", memberService.login(loginRequest));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        if (session.getAttribute("loginUser") != null) {
            session.removeAttribute("loginUser");
            return ResponseEntity.ok().build();
        }

        throw new RestApiException(MemberErrorCode.NOT_LOGIN_MEMBER);
    }


}
