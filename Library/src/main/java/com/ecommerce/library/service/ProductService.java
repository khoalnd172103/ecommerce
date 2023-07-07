package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDTO;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();
    Product save(Product product);
    Product update(MultipartFile file, Product product);
    Product findById(Long id);
    void deleteById(Long id);
    void enableById(Long id);

    Page<Product> pageProduct(int pageNo);

    Page<Product> searchProduct(int pageNo, String keyword);
}
