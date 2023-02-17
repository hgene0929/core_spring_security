package security.io.coreSpringSecurity.aopSecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import security.io.coreSpringSecurity.domain.dto.AccountDto;
import security.io.coreSpringSecurity.domain.entity.Account;
import security.io.coreSpringSecurity.service.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AopSecurityController {

    private final UserService userService;

    /*
    * 인가처리 DB 연동 비교 코드
    * (1) URL 방식
    * (2) Method 방식
    * */

    @GetMapping("/mypage") //(1)
    public String myPage(@AuthenticationPrincipal Account account, Authentication authentication, Principal principal) throws Exception {
        return "user/mypage";
    }

    @GetMapping("/order")
    @Secured("ROLE_USER") //(2)
    public String order() {
        userService.order();
        return "user/mypage";
    }

    @GetMapping("/preAuthorize")
    @PreAuthorize("hasRole('ROLE_USER') and #dto.username == principal.username")
    public String preAuthorize(AccountDto dto, Model model, Principal principal) {
        model.addAttribute("method", "Success @PreAuthorize");
        return "aop/method";
    }
}
