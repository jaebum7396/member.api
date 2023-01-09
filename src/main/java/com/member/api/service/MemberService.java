package com.member.api.service;

import com.member.api.jwt.JwtProvider;
import com.member.api.model.*;
import com.member.api.repository.AvailableUserRepository;
import com.member.api.repository.MemberRepository;
import com.member.api.utils.AES128Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JwtProvider jwtProvider;
    @Autowired AvailableUserRepository availableUserRepository;
    private final AES128Util aes128Util = new AES128Util();
    public ResponseEntity signup(SignRequest signupRequest) throws Exception {
        ResponseResult responseResult;
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {
            //중복 아이디 검사
            if (duplicateIdValidate(signupRequest)) {
                throw new BadCredentialsException("중복된 아이디입니다.");
            }
            //가입 가능한 유저 검사
            if (!signupValidate(signupRequest)) {
                new BadCredentialsException("가입 가능 목록에 없는 유저입니다.");
            }
            MemberEntity memberEntity = MemberEntity.builder()
                    .userId(signupRequest.getUserId())
                    .password(passwordEncoder.encode(signupRequest.getPassword()))
                    .name(signupRequest.getName())
                    .regNo(aes128Util.encrypt(signupRequest.getRegNo())) //주민번호 암호화
                    .build();
            //ROLE 설정
            memberEntity.setRoles(Collections.singletonList(AuthEntity.builder().name("ROLE_USER").build()));

            memberRepository.save(memberEntity);

            resultMap.put("userId", memberEntity.getUserId());
            resultMap.put("name", memberEntity.getName());
            resultMap.put("roles", memberEntity.getRoles());

            System.out.println("가입 성공");
            responseResult = ResponseResult.builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK)
                    .message("가입 성공")
                    .result(resultMap).build();
            return ResponseEntity.ok().body(responseResult);
        }catch(BadCredentialsException be){
            System.out.println(be.getMessage());
            responseResult = ResponseResult.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST)
                    .message(be.getMessage())
                    .result(resultMap).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult = ResponseResult.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("서버쪽 오류가 발생했습니다. 관리자에게 문의하십시오")
                    .result(resultMap).build();
            return ResponseEntity.internalServerError().body(responseResult);
        }
    }
    public ResponseEntity login(LoginRequest loginRequest) throws Exception {
        ResponseResult responseResult;
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try{
            MemberEntity memberEntity = memberRepository.findByUserId(loginRequest.getUserId()).orElseThrow(() ->
                new BadCredentialsException(loginRequest.getUserId()+": 아이디가 존재하지 않습니다."));
            if (!passwordEncoder.matches(loginRequest.getPassword(), memberEntity.getPassword())) {
                throw new BadCredentialsException("잘못된 비밀번호입니다.");
            }
            resultMap.put("userId", memberEntity.getUserId());
            resultMap.put("name", memberEntity.getName());
            resultMap.put("roles", memberEntity.getRoles());
            resultMap.put("token", jwtProvider.createToken(memberEntity.getUserId(), memberEntity.getRoles()));

            responseResult = ResponseResult.builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK)
                    .message("로그인 성공")
                    .result(resultMap).build();
            return ResponseEntity.ok().body(responseResult);
        }catch(BadCredentialsException be){
            System.out.println(be.getMessage());
            responseResult = ResponseResult.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST)
                    .message(be.getMessage())
                    .result(resultMap).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseResult);
        }catch(Exception e){
            e.printStackTrace();
            responseResult = ResponseResult.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("서버쪽 오류가 발생했습니다. 관리자에게 문의하십시오")
                    .result(resultMap).build();
            return ResponseEntity.internalServerError().body(responseResult);
        }
    }
    public boolean loginPasswordValidate(SignRequest loginRequest, MemberEntity memberEntity) {
        boolean check = passwordEncoder.matches(loginRequest.getPassword(), memberEntity.getPassword());
        return check;
    }
    public ResponseEntity getMyInfo(){
        ResponseResult responseResult;
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try{
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getName() == null) {
                throw new BadCredentialsException("토큰 인증에 실패하였습니다.");
            }
            System.out.println(authentication.getName());
            MemberEntity memberEntity = memberRepository.findByUserId(authentication.getName()).get();
            resultMap.put("userId", memberEntity.getUserId());
            resultMap.put("password", memberEntity.getPassword());
            resultMap.put("name", memberEntity.getName());
            resultMap.put("regNo", aes128Util.decrypt(memberEntity.getRegNo()));

            responseResult = ResponseResult.builder()
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK)
                    .message("유저 정보 요청 성공")
                    .result(resultMap).build();

            return ResponseEntity.ok().body(responseResult);
        }catch(BadCredentialsException be){
            responseResult = ResponseResult.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST)
                    .message(be.getMessage())
                    .result(resultMap).build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseResult);
        }catch(Exception e){
            e.printStackTrace();
            responseResult = ResponseResult.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("서버쪽 오류가 발생했습니다. 관리자에게 문의하십시오")
                    .result(resultMap).build();

            return ResponseEntity.internalServerError().body(responseResult);
        }
    }
    public boolean signupValidate(SignRequest signupRequest) {
        boolean check = availableUserRepository.findByNameAndRegNo(signupRequest.getName(), signupRequest.getRegNo()).isPresent();
        return check;
    }
    //중복된 아이디인지 검증
    public boolean duplicateIdValidate(SignRequest signupRequest) {
        boolean check = memberRepository.findByUserId(signupRequest.getUserId()).isPresent();
        return check;
    }
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByName(name).orElseThrow(
            () -> new UsernameNotFoundException("Invalid authentication!")
        );
        return new CustomUserDetails(memberEntity);
    }
}