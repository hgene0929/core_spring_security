package security.io.coreSpringSecurity.service;

import lombok.Setter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.RequestMatcher;
import security.io.coreSpringSecurity.repository.ResourceRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/*
* 데이터 계층으로부터 자원을 가져와 맵을 생성한다
* */
public class SecurityResourceService {

    private ResourceRepository resourceRepository;

    public SecurityResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }


    /*public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();

        레포지토리를 통해 자원정보 추출해오기
        List<Resources> resourcesList = resourceRepository.findAllResources();

        추출해온 자원에 권한정보 매핑하기
        resourcesList.forEach(re -> {
            List<ConfigAttribute> configAttributeList = new ArrayList<>();
            re.getRoleSet().forEach(ro -> {
                configAttributeList.add(new SecurityConfig(ro.getRoleName()));
                result.put(new AnPathRequestMatcher(re.getResourceName()), configAttributeList);
            });
        });

        return result;
    }*/
}
