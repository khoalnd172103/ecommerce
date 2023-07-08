package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    public HomeController(ProductService theProductService, CategoryService theCategoryService) {
        this.productService = theProductService;
        this.categoryService = theCategoryService;
    }

    @Autowired
    private HttpServletRequest request;

    @ModelAttribute("baseUrl")
    public String getBaseUrl() {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }

    @RequestMapping("/index")
    public String home(Model model) {
        List<Product> products = productService.findAllByIsActivated();
        List<Category> categories = categoryService.findAllByIsActivated();

        model.addAttribute("title", "Home Page");
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "index";
    }
}
