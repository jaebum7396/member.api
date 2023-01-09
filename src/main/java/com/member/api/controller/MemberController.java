package com.member.api.controller;

import com.member.api.model.LoginRequest;
import com.member.api.model.ResponseResult;
import com.member.api.model.SignRequest;
import com.member.api.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "MemberController")
@Tag(name = "MemberController", description = "회원가입, 로그인, 유저정보")
@RestController
public class MemberController {
    @Autowired MemberService memberService;
    @PostMapping(value = "/member/signup")
    @Operation(summary="회원가입", description="회원 가입 API")
    @ApiResponses({
        @ApiResponse(code = 200, message="ok",response = ResponseResult.class),
        @ApiResponse(code = 400, message="잘못된 요청",response = ResponseResult.class),
        @ApiResponse(code = 500, message="서버 에러",response = ResponseResult.class)
    })
    public ResponseEntity signup(@RequestBody SignRequest signRequest) throws Exception {
        return memberService.signup(signRequest);
    }
    @PostMapping(value = "/member/login")
    @Operation(summary="로그인", description="가입한 회원을 로그인 하는 API")
    @ApiResponses({
        @ApiResponse(code = 200, message="ok",response = ResponseResult.class),
        @ApiResponse(code = 400, message="잘못된 요청",response = ResponseResult.class),
        @ApiResponse(code = 500, message="서버 에러",response = ResponseResult.class)
    })
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) throws Exception {
        return memberService.login(loginRequest);
    }
    @GetMapping(value = "/member/me")
    @Operation(summary="내 정보 보기", description="가입한 회원 정보를 가져오는 API(로그인 후 인증정보 - jwt token 필수)")
    @ApiResponses({
        @ApiResponse(code = 200, message="ok",response = ResponseResult.class),
        @ApiResponse(code = 400, message="잘못된 요청",response = ResponseResult.class),
        @ApiResponse(code = 500, message="서버 에러",response = ResponseResult.class)
    })
    public ResponseEntity getMyInfo() {
        return memberService.getMyInfo();
    }
}