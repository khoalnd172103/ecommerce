package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public OrderController(CustomerService theCustomerService,
                           OrderService theOrderService) {
        this.customerService = theCustomerService;
        this.orderService = theOrderService;
    }

    @GetMapping("/order")
    public String order(Principal principal, Model model, HttpSession session) {
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
        } else {
            return "redirect:/login";
        }

        Customer customer = customerService.findByUserName(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingCart();

        if (shoppingCart != null) {
            session.setAttribute("totalItems", shoppingCart.getTotalItem());
        } else {
            session.setAttribute("totalItems", 0);
        }

        List<Order> orderList = customer.getOrderList();
        model.addAttribute("orderList", orderList);
        model.addAttribute("size", orderList.size());
        model.addAttribute("title", "Your Orders");

        return "orders";
    }

    @GetMapping("/save-order")
    public String saveOrder(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
        } else {
            return "redirect:/login";
        }

        Customer customer = customerService.findByUserName(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingCart();
        orderService.saveOrder(shoppingCart);
        //cartItemRepository.deleteCartItemById(shoppingCart.getId());
        shoppingCartRepository.delete(shoppingCart);

        return "redirect:/order";
    }
}
