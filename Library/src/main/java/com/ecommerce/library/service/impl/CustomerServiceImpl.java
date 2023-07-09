package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomerDTO;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Role;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl (RoleRepository theRoleRepository, CustomerRepository theCustomerRepository) {
        this.roleRepository = theRoleRepository;
        this.customerRepository = theCustomerRepository;
    }

    @Override
    public Customer findByUserName(String userName) {
        return customerRepository.findByUserName(userName);
    }

    @Override
    public Customer save(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setUserName(customerDTO.getUserName());
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));

        return customerRepository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUserName(userName);

        if (customer == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(customer.getRoles());

        return new org.springframework.security.core.userdetails.User(customer.getUserName(), customer.getPassword(),
                authorities);
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role tempRole : roles) {
            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
            authorities.add(tempAuthority);
        }

        return authorities;
    }
}
