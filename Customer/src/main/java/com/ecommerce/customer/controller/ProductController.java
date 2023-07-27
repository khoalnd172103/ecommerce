package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CustomerService customerService;

    @Autowired
    public ProductController(ProductService theProductService,
                             CategoryService theCategoryService,
                             CustomerService theCustomerService) {
        this.productService = theProductService;
        this.categoryService = theCategoryService;
        this.customerService = theCustomerService;
    }

    @RequestMapping("/products/asc/{pageNo}")
    public String products(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        checkPrincipal(principal, model);

        Page<Product> products = productService.pageProduct(pageNo);

        List<Product> productList = products.getContent();
        List<Category> categories = categoryService.findAllByIsActivated();

        model.addAttribute("title", "Products");
        model.addAttribute("size", products.getTotalElements());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", productList);
        model.addAttribute("categories", categories);
        model.addAttribute("sort", "asc");

        return "products";
    }

    @RequestMapping("/products/desc/{pageNo}")
    public String sortProductsDesc(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        checkPrincipal(principal, model);

        Page<Product> products = productService.pageProductDesending(pageNo);

        List<Product> productList = products.getContent();
        List<Category> categories = categoryService.findAllByIsActivated();

        model.addAttribute("title", "Products");
        model.addAttribute("size", products.getTotalElements());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", productList);
        model.addAttribute("categories", categories);
        model.addAttribute("sort", "desc");

        return "products";
    }

    @GetMapping("/product-detail")
    public String productDetail(@RequestParam("productId") Long id, Model model, Principal principal) {
        checkPrincipal(principal, model);

        Product product = productService.findById(id);

        List<Product> relatedProducts = productService.findAllByCategoryId(product.getCategory().getId());

        model.addAttribute("title", product.getName() + " | Ray Shop");
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);

        return "product-detail";
    }

    @GetMapping("/product-category")
    public String productsByCategory(@RequestParam("categoryId") Long id, Model model, Principal principal) {

        checkPrincipal(principal, model);

        Category category = categoryService.findById(id);

        List<Product> products = productService.findAllByCategoryId(id);
        List<Category> categories = categoryService.findAllByIsActivated();

        model.addAttribute("title", category.getName() + " | Ray Shop");
        model.addAttribute("result", products);
        model.addAttribute("categories", categories);
        model.addAttribute("category",category);

        return "search-result";
    }

    @GetMapping("/search-result/{pageNo}")
    public String searchProduct(@PathVariable("pageNo") int pageNo,
                                @RequestParam("keyword") String keyword,
                                Model model) {
        Page<Product> products = productService.searchProduct(pageNo, keyword);

        List<Product> result = products.getContent();
        List<Category> categories = categoryService.findAllByIsActivated();

        model.addAttribute("title", "Products");
        model.addAttribute("size", products.getTotalElements());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", result);
        model.addAttribute("categories", categories);
        model.addAttribute("sort", "desc");

        return "products";
    }

    private void checkPrincipal(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            Customer customer = customerService.findByUserName(username);
            model.addAttribute("customer", customer);
        }
    }
}
