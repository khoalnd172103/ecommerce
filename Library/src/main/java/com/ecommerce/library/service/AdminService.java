package com.ecommerce.library.service;

import com.ecommerce.library.dto.AdminDTO;
import com.ecommerce.library.model.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface AdminService extends UserDetailsService {
    public Admin findByUserName(String userName);

    public Admin save(AdminDTO adminDTO);
}
