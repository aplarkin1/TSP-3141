package com.gpsworkers.gathr.configuration;

import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requiresChannel()
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure();

        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/images/**", "/login", "/proto/**").permitAll().anyRequest().anonymous()
                .antMatchers("/home", "/loginSuccess", "/info", "/api/**", "/account", "/groups", "/chat").permitAll().anyRequest().authenticated()
                .and().oauth2Login().loginPage("/login").defaultSuccessUrl("/loginSuccess", true);

        http.csrf().disable(); //todo: fix this , this should not be disabled
    }

    public void configure(WebSecurity webSec) throws Exception {
        webSec.ignoring().antMatchers("/css/**", "/js/**", "/images/**");
    }
}
