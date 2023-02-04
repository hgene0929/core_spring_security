package security.io.coreSpringSecurity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.io.coreSpringSecurity.domain.entity.Account;
import security.io.coreSpringSecurity.repository.UserRepository;
import security.io.coreSpringSecurity.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void createUser(Account account) {
        userRepository.save(account);
    }
}
