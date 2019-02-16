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
                .antMatchers("/", "/home", "/css/**", "/js/**").permitAll().anyRequest().anonymous()
                .antMatchers("/secured", "/info").permitAll().anyRequest().authenticated()                
                .and().formLogin();

        // Commented out because we don't have a non-default login page as of yet

                /*.loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
                */
    }
    
    
    //TYhis is commented out because we haven't created our Mongo Conn
    /*
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
             User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
    */
}
