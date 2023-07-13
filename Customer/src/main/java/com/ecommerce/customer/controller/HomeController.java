package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    public HomeController(CustomerService theCustomerService, ProductService theProductService, CategoryService theCategoryService) {
        this.productService = theProductService;
        this.categoryService = theCategoryService;
        this.customerService = theCustomerService;
    }

    @Autowired
    private HttpServletRequest request;

    @ModelAttribute("baseUrl")
    public String getBaseUrl() {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }

    @RequestMapping("/index")
    public String home(Model model, Principal principal, HttpSession session) {
        List<Product> products = productService.findAllByIsActivated();
        List<Category> categories = categoryService.findAllByIsActivated();

        String username = "";
        Customer customer = null;

        if (principal != null) {
            username = principal.getName();
            customer = customerService.findByUserName(username);
            model.addAttribute("customer", customer);
            ShoppingCart shoppingCart = customer.getShoppingCart();
            if (shoppingCart != null) {
                session.setAttribute("totalItems", shoppingCart.getTotalItem());
            }
        }

        session.setAttribute("customer", customer);
        model.addAttribute("title", "Home Page");
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "index";
    }
}
