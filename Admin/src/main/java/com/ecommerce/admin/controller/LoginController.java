package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.AdminDTO;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private AdminService adminService;

    @Autowired
    public LoginController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("adminDTO", new AdminDTO());
        model.addAttribute("title", "Forgot Password Page");
        return "forgot-password";
    }

    @RequestMapping("/index")
    public String home(Model model) {
        model.addAttribute("title", "Admin Home Page");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        return "index";
    }

    @PostMapping("/checkUserNameForResetPassword")
    public String checkUserNameForResetPassword(
            @Valid @ModelAttribute("adminDTO") AdminDTO adminDTO,
            BindingResult theBindingResult,
            Model model) {
        Admin admin = adminService.findByUserName(adminDTO.getUserName());

        if (admin == null) {
            model.addAttribute("adminDTO", adminDTO);
            model.addAttribute("title", "Forgot Password Page");
            model.addAttribute("resetPassError", "Incorrect username, try again!");

            System.out.println("Incorrect username, try again!");
            return "forgot-password";
        }

        model.addAttribute("title", "Reset Password Page");
        model.addAttribute("adminDTO", adminDTO);
        return "reset-password";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(
            @Valid @ModelAttribute("adminDTO") AdminDTO adminDTO,
            BindingResult theBindingResult,
            Model model) {

        // check repeat password
        if (!adminDTO.getPassword().equals(adminDTO.getRepeatPassword())) {
            model.addAttribute("adminDTO", adminDTO);
            model.addAttribute("resetPassError", "Incorrect repeat password");

            System.out.println("Incorrect repeat password");
            return "reset-password";
        }

        System.out.println(adminDTO);

        adminDTO.setPassword(adminDTO.getPassword());

        System.out.println(adminDTO);

        adminService.save(adminDTO);
        System.out.println("Reset successfully");
//        model.addAttribute("title", "Reset Password Page");
//        model.addAttribute("adminDTO", adminDTO);
        return "login";
    }

}

