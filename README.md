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

7. 인증 핸들러
   - AuthenticationSuccessHandler : 인증 성공 핸들러
     - Form 로그인 방식으로 인증에 성공한 후, 커스텀한 핸들러를 호출하여 후속작업을 할 수 있도록 설정파일에 api를 통해 설정한다
     - AuthenticationSuccessHandler 인터페이스를 상속받아 구현이 가능하다
     - 뿐만 아니라 스프링에서 미리 구현해둔 구현체인 SimpleUrlAuthenticationSuccessHandler를 상속받아 커스텀한 클래스 내부에 후속작업에 대한 구현 코드를 작성한다
   - AuthenticationFailureHandler : 인증 실패 핸들러
     - 인증 성공 핸들러와 마찬가지의 방식으로 인증에 실패했을 경우의 후속작업을 처리하기 위한 핸들러
     - 내부 추상메서드의 매개변수로 전달되는 예외를 처리하는 등의 작업을 한다

8. 인증 거부 처리
   - 인증을 시도하는 과정에서 발생한 예외는 인증 실패 필터에서 처리하기 때문에 인증 실패 핸들러를 통해 후속작업을 구현할 수 있다
   - 반면, 인증 자체는 성공하였으나 이후에 특정 자원 접근 요청시 해당 자원에 접근가능한 권한이 아닌 경우라면 인가예외가 발생한다
   - 인가예외는 인증필터가 처리하지 않기 때문에 예외가 발생해도 인증 실패 핸들러에서 처리가 불가능하다
   - 따라서 이러한 예외에 대해 FilterSecurityInterceptor가 인가 예외를 발생시킨다
   - 이에 인가예외에 대한 후속처리는 AccessDeniedHandler 에 구현해야 한다

9. ajax 인증
   - Form 인증 방식과 유사한 필터 구조와 기능을 가진다
   - 다만, Form 인증은 비동기 방식인데 반해 Ajax 인증은 동기 방식이다
   - Ajax 인증 흐름
     - AjaxAuthenticationFilter를 통해 AjaxAuthenticationToken(인증객체)를 생성 후 사용자의 인증정보를 담아 보낸다
     - AuthenticationManager는 필터로부터 인증객체를 전달받아 다시 AjaxAuthenticationProvider(실제 인증처리 담당자)에 인증처리를 위임한다
     - 인증성공/실패시 각각 성공/실패 Handler를 호출하여 처리할 수 있도록 구성할 수 있다
     - 인증처리 후, FilterSecurityInterceptor에 의해 인가처리를 한다
   - 헤더 설정 : 전송방식이 Ajax인지의 여부를 위한 헤더설정
     - Ajax 인지 여부를 판별할 때 살펴보는 부분(AjaxLoginProcessingFilter 내부의 isAjax() 메서드)에서 X-Requested-With가 XMLHttpRequest인지 확인하기 때문
     ```
     xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
     ```

10. DSL
    - 특정한 도메인을 적용하는데 특화된 컴퓨터 언어
    - AbstractHttpConfigurer :
      - 스프링 시큐리티 초기화 설정 클래스
      - 필터, 핸들러, 메서드, 속성 등을 한 곳에 정의하여 처리할 수 있는 편리함 제공
      - init() : 초기화
      - configurer() : 설정
      - HttpSecurity의 apply() 메서드를 활용하여 매개변수로 구현해둔 커스텀 DSL클래스 객체를 반환하여 사용

11. CSRF
    - 헤더 설정 : 직접 ajax 방식으로 요청할 경우 토큰을 생성하고 직접 넣어주어야 한다
      ```
      <meta id="_csrf" name="_csrf" th:content="${_csrf.token}"/>
      <meta id="_csrf_header" name="_csrf_header" th:content="${_csrf.headerName}"/>
      
      var csrfHeader = $('meta[name="_csrf_header"]').attr('content')
      var csrfToken = $('meta[name="_csrf"]').attr('content')
      xhr.setRequestHeader(csrfHeader, csrfToken);
      ```
      
12. 인가(Authorization) 프로세스
    - 개요
      - DB와 연동하여 자원 및 권한을 설정하고 제어함으로써 동적 권한 관리가 가능하도록 한다
      - 현재 프로젝트에 구현된 인가방식
        (1) config의 인가설정 방식(Ex. antMatcher().hasRole()) 사용 X
        - 설정파일을 통한 인가설정은 동적권한 관리가 불가능하기 때문 -> DB와 인가권한 설정을 연동해야 한다
        (2) 관리자 시스템 구축
        - 회원관리 : 회원에 권한을 부여하는 기능 구축
        - 권한관리 : 권한을 생성하고 삭제하는 기능 구축
        - 자원관리 : 자원에 권한을 매핑하는 기능 구축
        (3) 권한 계층 구현 방식(Spring Security 지원)
        - URL : URL 요청 시 인가 처리(해당 URL에 매핑된 권한정보에 따른 권한처리)
        - Method : 메서드 호출 시 인가 처리(Method, Pointcut 타입을 통해 구현)
        
    - http.antMatchers("/user).access("hasRole('USER')") 단계
      - 현재 접근한 사용자 인증정보는 인증단계에서 저장한 Authentication 객체로부터 추출
      - FilterInvocation : 사용자가 어떤 URL을 통해 접근했는지 알아낸다
        - antMatchers() 내부의 접근 URL은 request, response, chain을 FilterInvocation의 생성자에 넘겨 저장하고 
          입력했던 요청정보를 알아낼 수 있다
      - List<ConfigAttribute> : 설정파일에 저장된 자원별 권한정보를 Map 형태(URL, hasRole('ROLE'))로 저장해둔 리스트

    - DB 연동(SecurityMetadataSource)
      - getAttributes() : 구현을 통해 권한정보 Map 형태로 초기화하는 작업 진행
      - 해당 인터페이스를 상속받아 FilterInvocationSecurityMetadataSource(URL 방식), MethodSecurityMetadataSource(Method 방식) 구현

13. FilterInvocationSecurityMetadataSource : 인가 프로세스 DB 연동(URL 접근 방식)
    - FilterInvocationSecurityMetadataSource 구현
      - 사용자가 접근하고자 하는 URL 자원에 대한 권한 정보 추출
      - AccessDecisionManager에게 전달하여 인가처리 수행
      - DB로부터 자원 및 권한 정보를 매핑하여 맵으로 관리
      - 사용자의 매 요청마다 요청정보에 매핑된 권한 정보 확인
    - RequestMap 정보에 DB 연동
      - UrlResourcesMapFactoryBean : DB로부터 얻은 권한/자원 정보를 ResourceMap이라는 빈으로 생성하여 UrlFilterInvocationMetadataSource에 전달한다
      - 
    