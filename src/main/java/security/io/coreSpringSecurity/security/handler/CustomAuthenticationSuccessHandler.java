package security.io.coreSpringSecurity.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/*
* SimpleUrlAuthenticationSuccessHandler를 상속받아 내부 추상 메서드인 onAuthenticationSuccess() 메서드 내부에 인증성공 이후의 후속작업을 구현
* */

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /*
    * 특정 URL로의 접근 요청시 인증되지 않아 로그인 페이지로 자동적으로 넘어간 경우,
    * 로그인 이후 바로 직전에 접근을 시도했던 페이지로 리다이렉트 시켜줄 것이다다
   * */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        setDefaultTargetUrl("/");

        SavedRequest savedRequest = requestCache.getRequest(request, response); //인증 직전 사용자가 요청했던 요청
        if (Objects.nonNull(savedRequest)) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }
        redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
    }
}
