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
                .antMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll().anyRequest().anonymous()
                .antMatchers("/home", "/loginSuccess", "/info", "/api/**").permitAll().anyRequest().authenticated()                
                .and().oauth2Login().loginPage("/login").defaultSuccessUrl("/loginSuccess", true);
        http.csrf().disable();
    }
    
    public void configure(WebSecurity webSec) throws Exception {
        webSec.ignoring().antMatchers("/css/**", "/js/**", "/images/**");
    }
    
}
