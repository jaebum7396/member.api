package com.member.api.common;

import com.member.api.model.SignRequest;

public final class Constants {
    public static final SignRequest MEMBER_GILDONG_TESTSET = SignRequest.builder()
            .userId("gildong0824")
            .password("1234")
            .name("홍길동")
            .regNo("860824-1655067").build();
    public static final SignRequest MEMBER_DOOLI_TESTSET = SignRequest.builder()
            .userId("hoi1108")
            .password("1234")
            .name("김둘리")
            .regNo("921108-1582815").build();
    public static final SignRequest MEMBER_MAZINGA_TESTSET = SignRequest.builder()
            .userId("ironarm8806")
            .password("1234")
            .name("마징가")
            .regNo("880601-2455115").build();
    public static final SignRequest MEMBER_VEGETA_TESTSET = SignRequest.builder()
            .userId("prince0411")
            .password("1234")
            .name("베지터")
            .regNo("910411-1656115").build();
    public static final SignRequest MEMBER_GOKU_TESTSET = SignRequest.builder()
            .userId("jobless0326")
            .password("1234")
            .name("손오공")
            .regNo("820326-2715701").build();
}
