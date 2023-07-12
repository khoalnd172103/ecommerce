package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDTO;
import com.ecommerce.library.model.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomerService extends UserDetailsService {
    public Customer findByUserName(String userName);

    public Customer save(CustomerDTO customerDTO);

    public Customer updateInfo(Customer customer);
}
