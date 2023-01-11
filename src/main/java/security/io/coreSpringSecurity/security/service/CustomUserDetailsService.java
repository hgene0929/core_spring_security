package security.io.coreSpringSecurity.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.io.coreSpringSecurity.domain.Account;
import security.io.coreSpringSecurity.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
* UserSetailsService : 로그인시 사용자 인증 정보를 DB로부터 조회하여 UserDetails 타입으로 컨텍스트에 저장
* */

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = userRepository.findByUsername(username);
        if (optionalAccount.isEmpty()) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        AccountContext accountContext = new AccountContext(optionalAccount.get(), roles);

        return accountContext;
    }
}
