package com.ecommerce.customer.controller;

import com.ecommerce.customer.controller.CustomOAuth2User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        System.out.println("CustomOAuth2UserService invoked");

        // Extract email from the user attributes
        String email = (String) user.getAttributes().get("email");

        // Create and return the CustomOAuth2User instance
        return new CustomOAuth2User(user.getAuthorities(), user.getAttributes(), "email");
    }
}
