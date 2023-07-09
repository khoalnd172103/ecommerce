package com.ecommerce.customer.config;

import com.ecommerce.library.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
public class CustomerConfiguration {

    //bcrypt bean definition
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //authenticationProvider bean definition
    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomerService customerService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(customerService); //set the custom user details service
        auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/index/**").permitAll()
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/image-product/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/index")
                                .permitAll()
                                .successHandler(new SavedRequestAwareAuthenticationSuccessHandler() {
                                                    @Override
                                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                                                        Authentication authentication) throws IOException, ServletException {
                                                        // run custom logics upon successful login
                                                        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                                                        String username = userDetails.getUsername();
                                                        System.out.println("The user " + username + " has logged in.");

                                                        DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
                                                        redirectStrategy.sendRedirect(request, response, "/index");
                                                    }
                                                }
                                )
                )
                .logout(logout -> logout.permitAll()
                );
        return http.build();
    }
}
