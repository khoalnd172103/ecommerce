package com.ecommerce.library.repository;

import com.ecommerce.library.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUserName(String userName);
}
