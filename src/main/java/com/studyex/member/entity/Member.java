package com.studyex.member.entity;

import com.studyex.member.dto.SignUpRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickName;

    private String password;

    private String phoneNumber;

    @Builder
    public Member(String email, String nickName, String password, String phoneNumber) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public static Member of(SignUpRequest memberReq) {

        return Member.builder()
                .email(memberReq.getEmail())
                .nickName(memberReq.getNickName())
                .password(memberReq.getPassword())
                .phoneNumber(memberReq.getPhoneNumber())
                .build();
    }
}
