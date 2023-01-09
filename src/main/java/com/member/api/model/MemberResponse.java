package com.member.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private String userId;
    private String password;
    private String name;
    private String regNo;
    private List<AuthEntity> roles = new ArrayList<AuthEntity>();
    private String token;
    public MemberResponse(MemberEntity memberEntity) {
        this.userId = memberEntity.getUserId();
        this.name = memberEntity.getName();
        this.regNo = memberEntity.getRegNo();
        this.roles = memberEntity.getRoles();
    }
}