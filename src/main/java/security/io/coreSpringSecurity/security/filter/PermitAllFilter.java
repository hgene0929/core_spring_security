package security.io.coreSpringSecurity.security.filter;

import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PermitAllFilter extends FilterSecurityInterceptor {

    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";
    private boolean observeOncePerRequest = true;
    private List<RequestMatcher> permitAllRequestMatchers = new ArrayList<>();

    /*
    * 생성자를 통해 인증/인가 과정이 필요없는 요청을 받아온다
    * */
    public PermitAllFilter(String...permitAllResources) {
        for (String permitAllResource : permitAllResources) {
            permitAllRequestMatchers.add(new AntPathRequestMatcher(permitAllResource));
        }
    }

    /*
    * 생성자를 통해 받아온 인증/인가 과정이 필요없는 요청과 현재 요청이 일치하면 return null을 통해 인가처리 과정으로 가지 X
    * */
    @Override
    protected InterceptorStatusToken beforeInvocation(Object object) {
        boolean permitAll = false;
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (RequestMatcher permitAllRequestMatcher : permitAllRequestMatchers) {
            if (permitAllRequestMatcher.matches(request)) {
                permitAll = true;
                break;
            }
        }
        if (permitAll) {
            return null;
        }
        return super.beforeInvocation(object);
    }

    /*
    * invoke() 메서드를 통해 인가처리 과정으로 넘어가버림 -> 이전에 permitAll 설정확인 프로세스가 필요하다
    * */
    public void invoke(FilterInvocation filterInvocation) throws IOException, ServletException {
        if (isApplied(filterInvocation) && this.observeOncePerRequest) {
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
            return;
        }

        if (filterInvocation.getRequest() != null && this.observeOncePerRequest) {
            filterInvocation.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
        }

        InterceptorStatusToken token = super.beforeInvocation(filterInvocation);
        try {
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
        }
        finally {
            super.finallyInvocation(token);
        }
        super.afterInvocation(token, null);
    }

    private boolean isApplied(FilterInvocation filterInvocation) {
        return (filterInvocation.getRequest() != null)
                && (filterInvocation.getRequest().getAttribute(FILTER_APPLIED) != null);
    }
}