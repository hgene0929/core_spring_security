package security.io.coreSpringSecurity.security.metadataSource;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import security.io.coreSpringSecurity.service.SecurityResourceService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/*
* UrlFilterInvocationSecurityMetadataSource :
* - 사용자가 접근하고자 하는 URL 자원에 대한 권한 정보 추출
* - AccessDecisionManager에게 전달하여 인가처리 수행
* - DB로부터 자원 및 권한 정보를 매핑하여 맵으로 관리
* - 사용자의 매 요청마다 요청정보에 매핑된 권한 정보 확인
* */
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    /*
    * RequestMap : 접근URL 과 권한정보목록을 저장한 맵형태의 정보
    * */
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();
    private SecurityResourceService securityResourceService;

    public UrlFilterInvocationSecurityMetadataSource(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    public UrlFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourcesMap) {
        this.requestMap = resourcesMap;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        /*
        * Object :
        * - getAttrubutes()는 URL접근 방식 뿐만 아니라 메서드 방식에도 호출되는 메서드이기 때문에 타입이 Object이고, 이를 구현방식에 따라 캐스팅해줄 필요가 있다
        * - 이후, getRequest() 메서드를 통해 사용자의 요청을 추출하여 요청 URL을 추출한다
        * */
        HttpServletRequest request = ((FilterInvocation) object).getRequest();

        /*
        * entry : RequestMap이 비어있지 않으면, 내부의 정보를 추출하여 요청 정보와 동일한 것을 찾는다
        * - matcher.matches(request) : 요청URL과 일치한 URL의 권한정보가 존재하면 해당 URL에 매핑된 권한정보 반환
        * */
        if (Objects.nonNull(requestMap)) {
            for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
                RequestMatcher matcher = entry.getKey();
                if (matcher.matches(request)) {
                    return entry.getValue();
                }
            }
        }

        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /*
    * 인가처리 실시간 반영하기용 메서드
    * */
    /*public void reload() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadMap = securityResourceService.getResourceList();
        Iterator<Map.Entry<RequestMatcher, List<ConfigAttribute>>> iterator = reloadMap.entrySet().iterator();

        requestMap.clear();

        while (iterator.hasNext()) {
            Map.Entry<RequestMatcher, List<ConfigAttribute>> entry = iterator.next();
            requestMap.put(entry.getKey(), entry.getValue());
        }
    }*/
}
