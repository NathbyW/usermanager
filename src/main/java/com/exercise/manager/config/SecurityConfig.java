package com.exercise.manager.config;

import com.exercise.manager.filter.RoleFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author wby
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public FilterRegistrationBean<RoleFilter> roleFilter() {
        FilterRegistrationBean<RoleFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RoleFilter());

        // Specify the URL patterns to which the filter should apply
        registrationBean.addUrlPatterns("/admin/*", "/user/*");
        return registrationBean;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
