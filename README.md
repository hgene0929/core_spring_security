# core_spring_security

[프로젝트 기본 구성]
- 의존성 설정, 환경설정, UI 화면 구성, 기본 CRUD 기능
- 스프링 시큐리티 보안 기능을 점진적으로 구현 및 완성

[활용 기술스택]
- spring boot, spring MVC, spring data jpa, spring security 활용
- DB : Postgresql server

[실습 내용]
1. 인증 프로세스 구현
- WebIgnore 설정
  - js/css/image 파일 등 보안 필터를 적용할 필요가 없는 리소스를 설정
  - permitAll() vs WebIgnore
    - permitAll() : 해당 경로는 모두 통과하긴 하지만 보안필터를 거쳐야 한다
    - WebIgnore   : 애초에 보안필터 자체를 거치지 않도록 설정한다 