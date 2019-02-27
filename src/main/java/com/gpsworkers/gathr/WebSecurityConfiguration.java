package com.gpsworkers.gathr;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home", "/css/**", "/js/**", "/images/**", "/login", "/api/**").permitAll().anyRequest().anonymous()
                .antMatchers("/loginSuccess", "/info").permitAll().anyRequest().authenticated()                
                .and().oauth2Login().loginPage("/login").defaultSuccessUrl("/loginSuccess", true);
    }
    
    public void configure(WebSecurity webSec) throws Exception {
        webSec.ignoring().antMatchers("/api/*");
    }
}
