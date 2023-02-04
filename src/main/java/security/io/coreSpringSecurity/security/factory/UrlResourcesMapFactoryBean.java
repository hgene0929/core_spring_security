package security.io.coreSpringSecurity.security.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import security.io.coreSpringSecurity.service.SecurityResourceService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/*
* RequestMap 정보에 DB 연동
* - UrlResourcesMapFactoryBean : DB로부터 얻은 권한/자원 정보를 ResourceMap이라는 빈으로 생성하여 UrlFilterInvocationMetadataSource에 전달한다
* */
public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    /*
    * securityResourceService : DB로부터 가져온 자원
    * resourceMap : DB로부터 가져온 자원을 통해 생성한 권한/자원 Map 형태의 정보객체
    * */
    private SecurityResourceService securityResourceService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap;

    public void setSecurityResourceService(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    /*
    * DB로부터 자원을 가져와 resourceMap 생성
    * */
    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        if (Objects.isNull(resourceMap)) {
            init();
        }
        return resourceMap;
    }

    private void init() {
        /*
        * DB로부터 가져온 자원/권한 정보로 매핑된 ResourceMap 객체
        * resourceMap = securityResourceService.getResourceList();
        * */
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
