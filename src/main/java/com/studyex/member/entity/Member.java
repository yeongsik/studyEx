package com.studyex.member.entity;

import com.studyex.member.dto.EditMemberRequest;
import com.studyex.member.dto.EditPwdRequest;
import com.studyex.member.dto.LoginMemberInfo;
import com.studyex.member.dto.SignUpRequest;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Builder
    public Member(Long id, String email, String name, String password, String phoneNumber, MemberType memberType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memberType = memberType;
    }

    public static Member of(SignUpRequest memberReq) {
        return Member.builder()
                .email(memberReq.getEmail())
                .name(memberReq.getName())
                .password(memberReq.getPassword())
                .phoneNumber(memberReq.getPhoneNumber())
                .memberType(MemberType.CUSTOMER)
                .build();
    }

    public static Member of(LoginMemberInfo memberReq) {
        return Member.builder()
                .email(memberReq.getEmail())
                .name(memberReq.getName())
                .phoneNumber(memberReq.getPhoneNumber())
                .memberType(memberReq.getMemberType())
                .build();
    }

    public void editMember(EditMemberRequest editMemberRequest) {
        this.phoneNumber = editMemberRequest.getPhoneNumber();
    }

    public void editPassword(EditPwdRequest editPwdRequest) {
        this.password = editPwdRequest.getNewPassword();
    }


    @Override
    public boolean equals(Object obj) {

        if (!obj.getClass().equals(Member.class)) {
            return false;
        }

        return this.toString().equals(obj.toString());
    }
}
