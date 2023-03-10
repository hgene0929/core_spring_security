package security.io.coreSpringSecurity.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.io.coreSpringSecurity.security.common.AjaxLoginAuthenticationEntryPoint;
import security.io.coreSpringSecurity.security.filter.AjaxLoginProcessingFilter;
import security.io.coreSpringSecurity.security.handler.AjaxAccessDeniedHandler;
import security.io.coreSpringSecurity.security.handler.AjaxAuthenticationFailureHandler;
import security.io.coreSpringSecurity.security.handler.AjaxAuthenticationSuccessHandler;
import security.io.coreSpringSecurity.security.provider.AjaxAuthenticationProvider;

/*
* Ajax 인증용 SecurityConfig 설정파일, @Order를 통해 우선순위를 주었다
* */

@RequiredArgsConstructor
@Configuration
@Order(0)
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AjaxAuthenticationProvider ajaxAuthenticationProvider;
    private final AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
    private final AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
                .accessDeniedHandler(new AjaxAccessDeniedHandler());

        /*
        * DSL 객체를 apply() 를 통해 적용
        * */
        customConfigurerAjax(http);
    }

    private void customConfigurerAjax(HttpSecurity http) throws Exception {
        http
                .apply(new AjaxLoginConfigure<>())
                .setSuccessHandlerAjax(ajaxAuthenticationSuccessHandler)
                .setFailureHandlerAjax(ajaxAuthenticationFailureHandler)
                .setAuthenthicationManager(authenticationManagerBean())
                .createLoginProcessingUrlMatcher("/api/login");
    }

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        /*
         * 커스텀한 Ajax 인증용 필터를 usernameauthenticationfilter 이전에 추가할 수 있도록 의존성 주입
         * */
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter =  new AjaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler);
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler);
        return ajaxLoginProcessingFilter;
    }
}
