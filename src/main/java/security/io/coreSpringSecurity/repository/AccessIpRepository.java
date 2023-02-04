package security.io.coreSpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.io.coreSpringSecurity.domain.entity.AccessIp;

public interface AccessIpRepository extends JpaRepository<AccessIp, Long> {
    AccessIp findByIpAddress(String ipAddress);
}
