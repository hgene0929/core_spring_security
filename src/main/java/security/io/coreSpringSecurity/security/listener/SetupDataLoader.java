package security.io.coreSpringSecurity.security.listener;

import lombok.RequiredArgsConstructor;
import security.io.coreSpringSecurity.domain.entity.AccessIp;
import security.io.coreSpringSecurity.repository.AccessIpRepository;

import java.util.Objects;

@RequiredArgsConstructor
public class SetupDataLoader {

    private final AccessIpRepository accessIpRepository;

    /*private void setupAccessIpData() {
        accessIpRepository.findByIpAddress("0:0:0:0:0:0:0:1");
        if (Objects.isNull(byIpAddress)) {
            AccessIp accessIp = AccessIp.builder()
                    .ipAddress("0:0:0:0:0:0:0:1")
                    .build();
            accessIpRepository.save(accessIp);
        }
    }*/
}
