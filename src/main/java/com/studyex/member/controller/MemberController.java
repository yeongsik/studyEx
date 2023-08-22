package com.studyex.member.controller;

import com.studyex.member.dto.SignUpRequest;
import com.studyex.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest memberReq) {
        memberService.signUp(memberReq);
        return ResponseEntity.ok().build();
    }

    // 회원 정보 조회 ( 마이페이지 )

    // 회원 탈퇴

    // 회원 정보 수정


}
