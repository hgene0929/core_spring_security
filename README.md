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
    - DetailsService 구현
      - DB로부터 사용자를 직접 조회하여, 권한정보가 포함된 UserDetails 타입으로 전달한다
      - 조회한 사용자 객체가 비어있다면 예외처리를, 존재한다면 UserDetails 타입의 객체를 생성하여 SecurityContext에 담아둔다
      - 해당 기능을 구현하는 메서드는 loadByUsername()이다
    - UserDetails 구현
      - Spring Security에서 사용자 권한을 인식하는 타입이다
      - UserDetails 인터페이스를 상속받아 사용자가 직접 정의한 클래스를 사용할 수도 있고, Spring Security에서 이미 정의해둔 User 클래스를 사용할 수도 있다
    - AuthenticationProvider 구현
      - UserDetailsService가 최종적으로 반환하는 UserDetails 타입의 사용자 인증 객체을 받아와 추가적인 인증 프로세스를 수행한다
      - authenticate() : 
        - 검증을 위한 구현 메서드(입력받은 사용자정보와 DB의 사용자 정보를 비교함으로써 해당 메서드에서 모든 인증 프로세스가 진행된다)
        - 인증 성공시 결과로 UsernamePasswordAuthenticationToken을 생성하여 반환한다
      - supports() : 매개변수로 전달되는 인증객체의 타입과 해당 클래스가 사용하고자 하는 인증객체의 타입이 일치한지 여부를 판별한다

4. 커스텀 로그인 페이지 생성(Form 인증방식)
   - Form 인증방식으로 설정할 경우, 아무런 커스텀도 하지 않는다면 스프링 시큐리티에서 제공하는 로그인 페이지가 나타난다
   - 시큐리티 설정파일에서 해당 로그인 페이지를 커스텀할 수 있다
   - loginPage() : 로그인 페이지 접근 url 를 통해 로그인 페이지를 커스텀한다
   - loginProcessingUrl() : 로그인 과정을 처리하는 rest api url을 커스텀한다
   - defaultSuccessUrl() : 로그인 성공시 이동할 페이지를 커스텀한다
   - 기본적으로 로그인 페이지를 커스텀할 경우 해당 경로는 모두에게 접근허용 해주어야 한다

5. 로그아웃 및 화면 보안 처리
   - 로그아웃 방법 : 
     - form 태그를 사용해서 POST 로 요청하는 방법
     - a 태그를 사용해서 GET 으로 요청하는 방법(이때, SecurityContextLogoutHandler을 활용한다)
   - 인증 여부에 따라 로그인/로그아웃을 분기하여 템플릿 엔진을 통해 표현할 수 있다(해당 프로젝트에서는 Thymleaf 사용)

6. 인증 부가 기능
   - WebAuthenticationDetails
     - 인증 과정 중 전달된 데이터를 Authentication 클래스의 details 속성에 저장한다
     - 사용자가 추가적으로 전달하는 파라미터를 받아와 전달하는데, 이때 remoteAddress와 SessionId는 스프링 시큐리티가 알아서 처리한다
   - AuthenticationDetailsSource
     - WebAuthenticationDetails 객체를 생성한다

