package com.kkoch.user.security;

import com.kkoch.user.api.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configurable
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;

    AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(memberService).passwordEncoder(bCryptPasswordEncoder);
        authenticationManager = authenticationManagerBuilder.build();

        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, memberService, env);

        http.csrf().disable();

        http.authorizeRequests()
            .antMatchers("/error/**", "/join", "/health-check").permitAll()
            .antMatchers("/**")
            .hasIpAddress("172.0.0.1")
            .and()
            .authenticationManager(authenticationManager)
            .addFilter(authenticationFilter);

        http.headers().frameOptions().disable();

        return http.build();
    }
}
