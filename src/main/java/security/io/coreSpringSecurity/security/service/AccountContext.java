package security.io.coreSpringSecurity.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import security.io.coreSpringSecurity.domain.Account;

import java.util.Collection;

/*
* UserDetails 타입의 스프링 시큐리티에서 참조할 사용자 인증객체
* */

public class AccountContext extends User {

    private final Account account;

    public Account getAccount() {
        return account;
    }

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
    }
}
