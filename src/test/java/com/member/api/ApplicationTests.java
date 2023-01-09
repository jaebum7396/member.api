package com.member.api;

import com.member.api.common.Constants;
import com.member.api.controller.MemberController;
import com.member.api.model.LoginRequest;
import com.member.api.model.ResponseResult;
import com.member.api.model.SignRequest;
import com.member.api.repository.AvailableUserRepository;
import com.member.api.repository.MemberRepository;
import com.member.api.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	MemberController memberController;
	@Autowired
	MemberService memberService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	AvailableUserRepository availableUserRepository;
	@LocalServerPort
	private int port;
	@Autowired private TestRestTemplate restTemplate;
	private final SignRequest testSetMember = Constants.MEMBER_DOOLI_TESTSET;//상수 설정된 테스트 멤버 정보
	@BeforeEach
	//각각 단위 테스트가 독립적으로 동작하기 위한 데이터 세팅
	void beforeDataSet() throws Exception {
		System.out.println("테스트 시작 전 기본 계정 데이터 세팅");
		try{
			SignRequest signupRequest = testSetMember;
			memberController.signup(signupRequest);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@AfterEach
	void resetAll() {
		memberRepository.deleteAll();
	}
	@Test
	public void signupTest() throws Exception {
		System.out.println("signupTest");
		//given
		SignRequest signupRequest = SignRequest.builder()
				.userId("gildong08242")
				.password("1234")
				.name("홍길동")
				.regNo("860824-1655068")
				.build();;
		//when
		ResponseEntity result = memberController.signup(signupRequest);

		//then
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
	@Test
	public void duplicateIdValidateTest() throws Exception {
		//gildong0824 아이디 중복체크
		SignRequest signupRequest = SignRequest.builder()
				.userId(testSetMember.getUserId())
				.name(testSetMember.getName())
				.regNo(testSetMember.getRegNo())
				.build();
		assertTrue(memberService.duplicateIdValidate(signupRequest));
	}
	@Test
	public void signupValidateTest() throws Exception {
		//주민번호가 다를때
		SignRequest signupRequest = new SignRequest();
		signupRequest.setName(testSetMember.getName());
		signupRequest.setRegNo("860824-1655069");
		memberService.signupValidate(signupRequest);
		assertFalse(memberService.signupValidate(signupRequest));
		//이름이 다를때
		signupRequest.setName("길동홍");
		signupRequest.setRegNo(testSetMember.getRegNo());
		memberService.signupValidate(signupRequest);
		assertFalse(memberService.signupValidate(signupRequest));
		//정보가 일치할때
		System.out.println(testSetMember.getName());
		System.out.println(testSetMember.getRegNo());

		signupRequest.setName(testSetMember.getName());
		signupRequest.setRegNo(testSetMember.getRegNo());
		assertTrue(memberService.signupValidate(signupRequest),"존재하는정보");
	}
	@Test
	public void loginTest() throws Exception {
		LoginRequest loginRequest = LoginRequest.builder()
				.userId(testSetMember.getUserId())
				.password(testSetMember.getPassword())
				.build();

		ResponseEntity result = memberController.login(loginRequest);

		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
	@Test
	public void getMyInfoTest() throws Exception {
		LoginRequest loginRequest = LoginRequest.builder()
				.userId(testSetMember.getUserId())
				.password(testSetMember.getPassword())
				.build();
		ResponseEntity result = memberController.login(loginRequest);
		String token = String.valueOf(((HashMap<String,Object>)(((ResponseResult)result.getBody()).getResult())).get("token"));

		String url = "http://localhost:"+port+"/member/me";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+ token);
		HttpEntity request = new HttpEntity(headers);
		result = restTemplate.exchange(url, HttpMethod.GET, request, ResponseResult.class);

		assertEquals(HttpStatus.OK, ((ResponseResult) result.getBody()).getStatus());
	}
}
