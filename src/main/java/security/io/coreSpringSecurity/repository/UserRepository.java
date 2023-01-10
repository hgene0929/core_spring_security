package security.io.coreSpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.io.coreSpringSecurity.domain.Account;

public interface UserRepository extends JpaRepository<Account, Long> {
}
