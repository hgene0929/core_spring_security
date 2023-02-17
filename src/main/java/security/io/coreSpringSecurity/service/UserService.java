package security.io.coreSpringSecurity.service;

import security.io.coreSpringSecurity.domain.entity.Account;

public interface UserService {
    void createUser(Account account);
    void order();
}
