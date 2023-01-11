package security.io.coreSpringSecurity.security.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* SimpleUrlAuthenticationFailureHandler를 상속받아 Form 로그인 실패 후의 후속작업에 대한 처리
* */

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid Username or Password";
        if (exception instanceof BadCredentialsException) {
            //비밀번호가 일치하지 않는 경우
        } else if (exception instanceof InsufficientAuthenticationException) {
            errorMessage = "Invalid Secret Key";
        }

        /*
        * 인증 실패시 다시 로그인 페이지로 리다이렉트 시킨다
        * 이때, 파라미터를 통해 예외발생사실과 예외원인 등을 함께 보내어 사용자 화면에서 처리할 수 있도록 한다
        * */
        setDefaultFailureUrl("/login?error=true&exception"+errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
