package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService theProductService, CategoryService theCategoryService) {
        this.productService = theProductService;
        this.categoryService = theCategoryService;
    }

    @RequestMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.findAllByIsActivated();
        List<Category> categories = categoryService.findAllByIsActivated();

        model.addAttribute("title", "Products");
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "products";
    }

    @GetMapping("/product-detail")
    public String productDetail(@RequestParam("productId") Long id, Model model) {
        Product product = productService.findById(id);

        List<Product> relatedProducts = productService.findAllByCategoryId(product.getCategory().getId());

        model.addAttribute("title", product.getName() + " | Ray Shop");
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);

        return "product-detail";
    }

    @GetMapping("/product-category")
    public String productsByCategory(@RequestParam("categoryId") Long id, Model model) {
        Category category = categoryService.findById(id);

        List<Product> products = productService.findAllByCategoryId(id);
        List<Category> categories = categoryService.findAllByIsActivated();

        model.addAttribute("title", category.getName() + " | Ray Shop");
        model.addAttribute("result", products);
        model.addAttribute("categories", categories);
        model.addAttribute("category",category);

        return "search-result";
    }
}
