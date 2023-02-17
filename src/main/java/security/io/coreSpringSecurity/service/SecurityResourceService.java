package security.io.coreSpringSecurity.service;

import lombok.Setter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.RequestMatcher;
import security.io.coreSpringSecurity.repository.AccessIpRepository;
import security.io.coreSpringSecurity.repository.ResourceRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/*
* 데이터 계층으로부터 자원을 가져와 맵을 생성한다
* */
public class SecurityResourceService {

    private ResourceRepository resourceRepository;
    private AccessIpRepository accessIpRepository;

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

    /*
    * IpVoter에서 비교를 위해 DB에 저장된 승인가능한 Ip정보를 모두 가져온다
    * */
    public List<String> getAccessIpList() {
        return accessIpRepository.findAll().stream().map(accessIp -> accessIp.getIpAddress()).collect(Collectors.toList());
    }
}
