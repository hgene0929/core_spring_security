package security.io.coreSpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.io.coreSpringSecurity.domain.Account;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
}
