# member.api

### 설명 
JWT를 이용한 회원가입/로그인 API 입니다.

### 구현 방법 
Java11, Spring Boot2.7.7, JPA, 스프링 데이타 JPA, H2, Gradle, JUNIT

### Swagger 주소
http://localhost:8080/swagger-ui/

### 실행 방법
H2 데이터베이스 설치가 필요합니다.  
[DB 정보]  
![캡처](https://user-images.githubusercontent.com/38182229/209821159-523aadad-b6a4-4115-9e5b-e7d8c41bcb52.PNG)  
이후 그래들 빌드 후 Application 실행

#### 1. 회원 가입 API.
가입 가능한 유저 정보를 담고 있는 AvailableUserEntity 테이블 조회하여 존재할 시에 회원가입 진행  
, 존재하지 않을 시 400 코드를 반환하도록 구현  
요청한 아이디가 MemberEntity 테이블에 이미 가입되어 있는지 조회하여 존재하지 않을 시에 회원가입 진행  
, 이미 존재할 시에 400 코드를 반환하도록 구현  
이외 예측 불가능한 소스 에러일 시에 500코드를 반환하도록 구현   
spring security BcryptPasswordEncoder 이용하여 password 암호화 하여 저장하도록 구현    
AES128 이용하여 regNo(주민등록번호 ) 암호화 하여 저장하도록 구현  
  
구현 테이블 :   
  MemberEntity    
, AuthEntity(admin, user 등의 role 정보를 담고 있는 테이블 Member 테이블과 1:N)   
  
#### 2. 가입한 회원을 로그인 하는 API
가입한 회원정보 패러미터로 요청시, jwt token response 하도록 구현  
가입한 회원정보 없을 시 400에러 반환  
  
#### 3. 가입한 회원 정보를 가져오는 API
인증 토큰 이용하여 자기 정보만 볼 수 있도록 구현  
AES128 이용하여 regNo(주민등록번호) 복호화 하여 반환 하도록 구현  
인증 토큰 정보 유효하지 않을 시 400 코드 반환  
    
  
### 검증 결과
swagger 통해 api 요청시 요구사양대로 올바르게 작동 됨을 확인하였고  
Junit MemberTest 클래스를 통해 제약사항 및 기능 단위 테스트 확인  
