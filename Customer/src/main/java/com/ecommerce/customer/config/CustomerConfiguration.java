package com.ecommerce.customer.config;

import com.ecommerce.library.service.AdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CustomerConfiguration {

    //bcrypt bean definition
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    //authenticationProvider bean definition
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider(AdminService adminService) {
//        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//        auth.setUserDetailsService(adminService); //set the custom user details service
//        auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
//        return auth;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/index/**").permitAll()
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/image-product/**").permitAll()
                                .anyRequest().authenticated()
                );

        return http.build();
    }
}
