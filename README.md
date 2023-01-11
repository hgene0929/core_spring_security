# core_spring_security

[프로젝트 기본 구성]
- 의존성 설정, 환경설정, UI 화면 구성, 기본 CRUD 기능
- 스프링 시큐리티 보안 기능을 점진적으로 구현 및 완성

[활용 기술스택]
- spring boot, spring MVC, spring data jpa, spring security 활용
- DB : Postgresql server

[실습 내용]
1. WebIgnore 설정
  - js/css/image 파일 등 보안 필터를 적용할 필요가 없는 리소스를 설정
  - permitAll() vs WebIgnore
    - permitAll() : 해당 경로는 모두 통과하긴 하지만 보안필터를 거쳐야 한다
    - WebIgnore   : 애초에 보안필터 자체를 거치지 않도록 설정한다 

2. PasswordEncoder
   - 비밀번호를 안전하게 암호화 하도록 기능을 제공
   - PasswordEncoderFactories.createDelegatingPasswordEncoder()을 통해 여러개의 PasswordEncoder 유형을 선언한 뒤,
     상황에 맞게 선택해서 사용할 수 있도록 지원한다
   - 암호화 포맷 : 기본 포맷은 Bcrypt 방식으로, 알고리즘 종류는 bcrypt, noop, pbkdf2, scrypt, sha256이 있다
   - 인터페이스 : encode(password)는 패스워드를 암호화하고, matches(raw, encoded)는 패스워드를 비교한다

3. DB 연동 인증 처리
    - CustomUserDetailsService 
      - DB로부터 사용자를 직접 조회하여, 권한정보가 포함된 UserDetails 타입으로 전달한다
      - 조회한 사용자 객체가 비어있다면 예외처리를, 존재한다면 UserDetails 타입의 객체를 생성하여 SecurityContext에 담아둔다
      - 해당 기능을 구현하는 메서드는 loadByUsername()이다
    - UserDetails
      - Spring Security에서 사용자 권한을 인식하는 타입이다
      - UserDetails 인터페이스를 상속받아 사용자가 직접 정의한 클래스를 사용할 수도 있고, Spring Security에서 이미 정의해둔 User 클래스를 사용할 수도 있다