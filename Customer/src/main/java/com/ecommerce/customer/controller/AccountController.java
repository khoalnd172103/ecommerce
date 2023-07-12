package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class AccountController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    public AccountController(CustomerService theCustomerService) {
        this.customerService = theCustomerService;
    }

    @GetMapping("/account-home")
    public String accountHome(Model model, Principal principal) {
        Customer customer = customerService.findByUserName(principal.getName());

        model.addAttribute("customer", customer);
        model.addAttribute("title", "My account");

        return "account-home";
    }

    @PostMapping("/update-info")
    public String updateInfo(@ModelAttribute("customer") Customer customer,
                             Model model,
                             Principal principal) {
        Customer customerSaved = customerService.findByUserName(principal.getName());

        customerSaved = customerService.updateInfo(customer);

        model.addAttribute("customer", customerSaved);
        model.addAttribute("title", "My account");

        return "redirect:/account-home";
    }
}
