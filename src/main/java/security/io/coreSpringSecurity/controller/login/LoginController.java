package security.io.coreSpringSecurity.controller.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import security.io.coreSpringSecurity.domain.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
public class LoginController {

    /*
    * 로그인 실패시, 핸들러에서 넘겨준 에러관련 파라미터를 뷰로 넘겨주기 위함
    * */
    @GetMapping("/login")
    public String login(String error, String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "login";
    }

    /*
    * SecurityContextLogoutHandler을 활용한 GET 방식 로그아웃
    * */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login";
    }

    /*
    * 인가예외 발생시 AccessDeniedHandler로부터 파라미터를 받아와 인가예외 페이지로 이동시킨다
   * */
    @GetMapping("/denied")
    public String accessDenied(String exception, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        model.addAttribute("username", account.getUsername());
        model.addAttribute("exception", exception);
        return "user/login/denied";
    }
}
