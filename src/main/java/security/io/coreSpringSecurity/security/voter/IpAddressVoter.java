package security.io.coreSpringSecurity.security.voter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import security.io.coreSpringSecurity.service.SecurityResourceService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class IpAddressVoter implements AccessDecisionVoter<Object> {

    private final SecurityResourceService securityResourceService;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        /*
        * 사용자의 IP 주소값이 포함되어 있는 정보를 반환받는다
        * 이떄 반환타입은 WebAuthenticationDetails으로 캐스팅해주어야 한다
        * */
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = details.getRemoteAddress();

        /*
        * DB에 저장된 승인가능한 IP정보를 모두 가져와서 기본값 거부상태에서 시작해 요청받은 IP와 비교
        * 허용시 -> ACCESS_ABSTAIN | 거부시 -> 예외처리
        * */
        List<String> accessIpList = securityResourceService.getAccessIpList();
        int result = ACCESS_DENIED;
        for (String ipAddress : accessIpList) {
            if (remoteAddress.equals(ipAddress)) {
                return ACCESS_ABSTAIN;
            }
        }
        if (result == ACCESS_DENIED) {
            throw new AccessDeniedException("Invalid IpAddress");
        }
        return result;
    }
}
