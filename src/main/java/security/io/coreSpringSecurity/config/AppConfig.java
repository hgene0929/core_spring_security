package security.io.coreSpringSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import security.io.coreSpringSecurity.repository.ResourceRepository;
import security.io.coreSpringSecurity.service.SecurityResourceService;

/*
* 팩토리 빈 생성
* */
@Configuration
public class AppConfig {

    @Bean
    public SecurityResourceService securityResourceService(ResourceRepository resourceRepository) {
        return new SecurityResourceService(resourceRepository);
    }
}
