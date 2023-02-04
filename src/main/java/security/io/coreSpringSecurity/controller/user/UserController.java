package security.io.coreSpringSecurity.controller.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import security.io.coreSpringSecurity.domain.entity.Account;
import security.io.coreSpringSecurity.domain.dto.AccountDto;
import security.io.coreSpringSecurity.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    /*
    * PasswordEncoder 객체를 의존성 주입받아 사용
    * */
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @GetMapping("/mypage")
    public String myPage() {
        return "user/mypage";
    }

    @GetMapping("/users")
    public String createUser() {
        return "user/login/register";
    }

    @PostMapping("/users")
    public String createUser(AccountDto dto) {
        /*
        * ModelMapper : 객체의 내부 메서드 map() 을 통해 dto의 소스를 엔티티 등으로 매핑해준다
        * */
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(dto, Account.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        userService.createUser(account);
        return "redirect:/";
    }
}
