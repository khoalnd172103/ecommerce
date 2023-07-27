package com.ecommerce.customer.config;

import com.ecommerce.customer.controller.CustomOAuth2User;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.io.IOException;

@Configuration
public class CustomerConfiguration {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    public CustomerConfiguration(CustomerService theCustomerService, ProductService theProductService, CategoryService theCategoryService) {
        this.productService = theProductService;
        this.categoryService = theCategoryService;
        this.customerService = theCustomerService;
    }

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
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/index")
                        .permitAll()
                        .successHandler(new AuthenticationSuccessHandler() {

                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                                Authentication authentication) throws IOException, ServletException {
                                System.out.println("AuthenticationSuccessHandler invoked");

                                DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

                                if (authentication instanceof OAuth2AuthenticationToken) {
                                    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                                    String email = oauthToken.getPrincipal().getAttribute("email");
                                    System.out.println("User email: " + email);

                                    // Fetch the customer using the email
                                    Customer customer = customerService.findByUserName(email);
                                    if (customer == null) {
                                        // Handle the case where the customer is not found in the database
                                        // For example, you could redirect the user to a registration page or show an error message
                                        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
                                        redirectAttributes.addFlashAttribute("registrationError", "Can not find your account, please register.");
                                        redirectStrategy.sendRedirect(request, response, "/suggest");
                                    } else {
                                        // Set the customer in the session
                                        HttpSession session = request.getSession();
                                        session.setAttribute("customer", customer);

                                        // Fetch the shopping cart
                                        ShoppingCart shoppingCart = customer.getShoppingCart();
                                        if (shoppingCart != null) {
                                            session.setAttribute("totalItems", shoppingCart.getTotalItem());
                                        }
                                        // Redirect the user to the desired URL after successful login
                                        redirectStrategy.sendRedirect(request, response, "/index");
                                    }
                                }
                            }
                        })
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
                )
        ;
        return http.build();
    }
}
