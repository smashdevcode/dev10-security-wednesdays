package learn.openidconnect.security;

import learn.openidconnect.config.AppProperties;
import learn.openidconnect.filters.SpaWebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomOidcUserService customOidcUserService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private AppProperties appProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                        .ignoringAntMatchers("/api/**").and()
                .authorizeRequests(a -> a
                        .antMatchers("/", "/index.html", "/error", "/static/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login()
                        .userInfoEndpoint().oidcUserService(customOidcUserService).userService(customOAuth2UserService)
                        .and()
                        .defaultSuccessUrl(appProperties.getAuthenticationSuccessUrl(), true)
                        .failureUrl(appProperties.getAuthenticationFailureUrl())
                .and()
                .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }
}
