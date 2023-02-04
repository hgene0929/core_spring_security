package security.io.coreSpringSecurity.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import security.io.coreSpringSecurity.domain.dto.AccountDto;
import security.io.coreSpringSecurity.security.token.AjaxAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/*
* AbstractAuthenticationProcessingFilter를 상속받아 Ajax(비동기) 인증 방식을 구현하기 위한 필터
* */

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public AjaxLoginProcessingFilter() {
        /*
        * AntPathRequestMatcher의 매개변수와 요청 url이 일치할 떄만 해당 필터가 작동하도록 직접 설정
        * */
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        /*
        * 해당 요청이 ajax 방식일 경우에만 이 필터가 작동하도록 직접 설정
        * */
        if(!isAjax(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        /*
        * 검증을 통과한(ajax이며, /api/login url) 요청으로부터 dto를 추출한 뒤 올바른 값인지 확인(빈값이면 안됨)
        * */
        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
        if (Objects.isNull(accountDto.getUsername()) || Objects.isNull(accountDto.getPassword())) {
            throw new IllegalStateException("Username or Password is Empty");
        }

        /*
        * 올바른 정보를 담은 인증객체(토큰)를 생성한 뒤, AjaxAuthenticationManager에 전달
        * */
        AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());
        return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
    }

    /*
    * Ajax 인지 여부를 판별할 때 살펴보는 부분
    * */
    private boolean isAjax(HttpServletRequest request) {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-with"))) {
            return true;
        }
        return false;
    }
}
