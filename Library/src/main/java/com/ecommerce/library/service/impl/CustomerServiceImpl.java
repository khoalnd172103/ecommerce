package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomerDTO;
import com.ecommerce.library.exception.BaseException;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Role;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.MailService;
import com.ecommerce.library.utils.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import org.thymeleaf.util.ObjectUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    public CustomerServiceImpl(RoleRepository theRoleRepository, CustomerRepository theCustomerRepository) {
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
        //customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setImage("avatar.jpg");
        customer.setUserName(customerDTO.getUserName());
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateInfo(Customer customer) {
        Customer customerSaved = customerRepository.findByUserName(customer.getUserName());
        customerSaved.setFirstName(customer.getFirstName());
        customerSaved.setLastName(customer.getLastName());
        customerSaved.setAddress(customer.getAddress());
        customerSaved.setPhone(customer.getPhone());

        return customerRepository.save(customerSaved);
    }

    @Override
    public BaseResponse createCustomer(CustomerDTO customerDTO) {
        BaseResponse baseResponse = new BaseResponse();

        validateAccount(customerDTO);

        try {
            Customer customer = save(customerDTO);

            baseResponse.setCode(String.valueOf(HttpStatus.OK.value()));
            baseResponse.setMessage("Create successfully");
        } catch (Exception e) {
            baseResponse.setCode(String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
            baseResponse.setMessage("Service unavailable");
        }

        //saveData(List.of(customerDTO));

        //send mail
        //mailService.sendMailCreateCustomer(customerDTO);
        return baseResponse;
    }

    private void validateAccount(CustomerDTO customerDTO) {
        //validate null
        if (ObjectUtils.isEmpty(customerDTO)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Data must not empty");
        }

        //validate duplicate
        Customer customer = customerRepository.findByUserName(customerDTO.getUserName());

        if (!ObjectUtils.isEmpty(customer)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Username had existed");
        }

        //validate role
//        List<String> roles = roleRepository.findAll().stream().map(Role::getName).toList();
//        if (!roles.contains(customer.getRoles())) {
//            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid role");
//        }
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
