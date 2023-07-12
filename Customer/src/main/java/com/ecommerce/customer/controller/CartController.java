package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class CartController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductService productService;

    @Autowired
    public CartController(ProductService theProductService, CustomerService theCustomerService, ShoppingCartService theShoppingCartService) {
        this.productService = theProductService;
        this.customerService = theCustomerService;
        this.shoppingCartService = theShoppingCartService;
    }

    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session) {

        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
        } else {
            return "redirect:/login";
        }

        Customer customer = customerService.findByUserName(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingCart();

        if (shoppingCart == null) {
            model.addAttribute("check", "No item  in your cart");
            session.setAttribute("totalItems", 0);
        }

        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("title", "Shopping Cart");

        return "cart";
    }

    @GetMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("productId") String productId,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                                Principal principal,
                                Model model,
                                HttpSession session,
                                HttpServletRequest request) {
        Long id = Long.parseLong(productId);

        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
        } else {
            return "redirect:/login";
        }

        Product product = productService.findById(id);
        Customer customer = customerService.findByUserName(principal.getName());
        ShoppingCart shoppingCart = shoppingCartService.addItemToCart(product, quantity, customer);

        session.setAttribute("totalItems", shoppingCart.getTotalItem());
        model.addAttribute("shoppingCart", shoppingCart);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/update-cart")
    public String updateItemInCart(@RequestParam("productId") String productId,
                                   @RequestParam("quantity") String quantityString,
                                   Model model,
                                   HttpSession session,
                                   Principal principal) {

        Long id = Long.parseLong(productId);
        int quantity = Integer.parseInt(quantityString);

        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
        } else {
            return "redirect:/login";
        }

        Product product = productService.findById(id);
        Customer customer = customerService.findByUserName(principal.getName());
        ShoppingCart shoppingCart = shoppingCartService.updateItemInCart(product, quantity, customer);

        session.setAttribute("totalItems", shoppingCart.getTotalItem());
        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("title", "Shopping Cart");

        return "redirect:/cart";
    }

    @GetMapping("/delete-cart")
    public String deleteItemFromCart(@RequestParam("productId") String productId,
                                     Model model,
                                     HttpSession session,
                                     Principal principal) {
        Long id = Long.parseLong(productId);

        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
        } else {
            return "redirect:/login";
        }

        Product product = productService.findById(id);
        Customer customer = customerService.findByUserName(principal.getName());
        ShoppingCart shoppingCart = shoppingCartService.deleteItemFromCart(product, customer);

        session.setAttribute("totalItems", shoppingCart.getTotalItem());
        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("title", "Shopping Cart");

        return "redirect:/cart";
    }

    @GetMapping("/check-out")
    public String checkout(Model model,
                           Principal principal,
                           HttpSession session) {
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
        } else {
            return "redirect:/login";
        }

        Customer customer = customerService.findByUserName(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingCart();

        session.setAttribute("totalItems", shoppingCart.getTotalItem());
        model.addAttribute("customer", customer);
        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("title", "Check out");

        return "checkout";
    }
}
