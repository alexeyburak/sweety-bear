package by.bsuir.sweetybear.config.security;

import by.bsuir.sweetybear.service.impl.CustomOauth2UserServiceImpl;
import by.bsuir.sweetybear.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final CustomOauth2UserServiceImpl oauth2UserService;
    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;

    public WebSecurityConfig(CustomUserDetailsServiceImpl userDetailsService,
                             CustomOauth2UserServiceImpl oauth2UserService,
                             @Lazy Oauth2LoginSuccessHandler oauth2LoginSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.oauth2UserService = oauth2UserService;
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/oauth2/**")
                    .permitAll()
                    .antMatchers("/", "/registration", "/css/**", "/js/**", "/images/**", "/activate/*", "/forgot_password", "/reset_password/*", "/reset_password")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .failureForwardUrl("/login/error")
                    .permitAll()
                .and()
                    .oauth2Login()
                    .loginPage("/login")
                    .permitAll()
                    .userInfoEndpoint()
                    .userService(oauth2UserService)
                .and()
                    .successHandler(oauth2LoginSuccessHandler)
                .and()
                    .logout()
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
