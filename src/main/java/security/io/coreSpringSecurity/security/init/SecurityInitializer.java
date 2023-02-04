package security.io.coreSpringSecurity.security.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;
import security.io.coreSpringSecurity.service.RoleHierarcyService;

/*
* 서비스를 통해 DB로부터 Role의 계층정보를 가져와 포맷팅한 결과를 적용시켜준다
* */
@Component
@RequiredArgsConstructor
public class SecurityInitializer implements ApplicationRunner {

    private final RoleHierarcyService roleHierarcyService;
    private final RoleHierarchyImpl roleHierarchy;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String allHierarchy = roleHierarcyService.findAllHierarchy();
        roleHierarchy.setHierarchy(allHierarchy);
    }
}
