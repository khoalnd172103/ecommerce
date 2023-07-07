package com.ecommerce.library.repository;

import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p")
    Page<Product> pageProduct(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% OR p.description LIKE %?1%")
    Page<Product> searchProduct(String keyword, Pageable pageable);

}
