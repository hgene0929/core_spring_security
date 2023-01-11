package security.io.coreSpringSecurity.security.common;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/*
* WebAuthenticationDetails 를 상속받아와 인증과정에서 사용자로부터 받아온 추가 정보를 파라미터 형태로 Authentication 인증객체의 내부 속성인 details에 저장
* */

public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

    private String secretKey;

    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        secretKey = request.getParameter("secret_key");
    }

    public String getSecretKey() {
        return secretKey;
    }
}
