package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDTO;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    public RegisterController(CustomerService theCustomerService) {
        this.customerService = theCustomerService;
    }


    @GetMapping("/register")
    public String register(Model model) {
        CustomerDTO customerDTO = new CustomerDTO();

        model.addAttribute("title", "Registration");
        model.addAttribute("customerDTO", customerDTO);

        return "register";
    }

    @PostMapping("/processRegister")
    public String processRegister(@Valid @ModelAttribute("customerDTO") CustomerDTO customerDTO,
                                  BindingResult bindingResult,
                                  HttpSession session, Model model) {

        if(bindingResult.hasErrors()) {
            return "register";
        }

        Customer customer = customerService.findByUserName(customerDTO.getUserName());

        //check exist
        if (customer != null) {
            model.addAttribute("customerDTO", customerDTO);
            model.addAttribute("registrationError", "User name is already registered.");
            return "register";
        }

        //check repeat password
        if(!customerDTO.getPassword().equals(customerDTO.getRepeatPassword())) {
            model.addAttribute("customerDTO", customerDTO);
            model.addAttribute("registrationError", "Incorrect repeat password");
            return "register";
        }

        customer = customerService.save(customerDTO);

        System.out.println("Create new customer successfully");

        model.addAttribute("registrationSuccess", "Register successfully");

        return "register";
    }
}
