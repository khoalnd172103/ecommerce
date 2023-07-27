package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDTO;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private ImageUpload imageUpload;

    @Autowired
    public ProductServiceImpl(ProductRepository thepProductRepository) {
        this.productRepository = thepProductRepository;
    }

    @Override
    public List<ProductDTO> findAll() {
        List<ProductDTO> productDTOList = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setCostPrice(product.getCostPrice());
            productDTO.setSalePrice(product.getSalePrice());
            productDTO.setCurrentQuantity(product.getCurrentQuantity());
            productDTO.setCategory(product.getCategory());
            productDTO.setImage(product.getImage());
            productDTO.setActivated(product.isActivated());
            productDTO.setDeleted(product.isDeleted());
            productDTOList.add(productDTO);
        }

        return productDTOList;
    }

    @Override
    public Product save(Product productDTO) {
        Product product = new Product();
        try {
            product.setImage(productDTO.getImage());
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setCurrentQuantity(productDTO.getCurrentQuantity());
            product.setCostPrice(productDTO.getCostPrice());
            product.setCategory(productDTO.getCategory());
            product.setDeleted(false);
            product.setActivated(true);
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(MultipartFile multipartFile, Product product) {
        Product productUpdate = productRepository.findById(product.getId()).get();

        if (!multipartFile.isEmpty()) {
            String fileName = ImageUpload.handleFileUpload(multipartFile);
            productUpdate.setImage(fileName);
            String uploadDir = "image-product/" + productUpdate.getId();
            ImageUpload.uploadImage(uploadDir, multipartFile);
        }

        productUpdate.setId(product.getId());
        productUpdate.setName(product.getName());
        productUpdate.setDescription(product.getDescription());
        productUpdate.setCurrentQuantity(product.getCurrentQuantity());
        productUpdate.setCostPrice(product.getCostPrice());
        productUpdate.setCategory(product.getCategory());
        productUpdate.setDeleted(false);
        productUpdate.setActivated(true);

        return productRepository.save(productUpdate);
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> result = productRepository.findById(id);

        Product product = null;

        if (result.isPresent()) {
            product = result.get();
        }

        return product;
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id).get();

        product.setDeleted(true);
        product.setActivated(false);

        productRepository.save(product);

    }

    @Override
    public void enableById(Long id) {
        Product product = productRepository.findById(id).get();

        product.setDeleted(false);
        product.setActivated(true);

        productRepository.save(product);
    }

    @Override
    public Page<Product> pageProduct(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 9, Sort.by("costPrice").ascending());
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> pageProductDesending(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 9, Sort.by("costPrice").descending());
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchProduct(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 9, Sort.by("costPrice"));
        Page<Product> result = productRepository.searchProduct(keyword, pageable);
        return result;
    }

    @Override
    public List<Product> findAllByIsActivated() {
        return productRepository.findAllByIsActivatedTrue();
    }

    @Override
    public List<Product> findAllByCategoryId(Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }
}
