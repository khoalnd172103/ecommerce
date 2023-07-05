package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.AdminDTO;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private Logger logger = Logger.getLogger(getClass().getName());

    private AdminService adminService;

    @Autowired
    public RegistrationController(AdminService adminService) {
        this.adminService = adminService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/showRegistrationForm")
    public String registerForm(Model theModel) {

        theModel.addAttribute("adminDTO", new AdminDTO());
        theModel.addAttribute("title", "Register Page");

        return "register";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute("adminDTO") AdminDTO adminDTO,
            BindingResult theBindingResult,
            HttpSession session, Model theModel) {

        session.removeAttribute("registrationError");
        String userName = adminDTO.getUserName();
        //logger.info("Processing registration form for: " + userName);

        // form validation
        if (theBindingResult.hasErrors()){
            return "register";
        }

        // check the database if user already exists
        Admin existing = adminService.findByUserName(userName);
        if (existing != null){
            theModel.addAttribute("adminDTO", adminDTO);
            theModel.addAttribute("registrationError", "User name already exists.");
            //session.setAttribute("registrationError", "User name already exists.");

            System.out.println("User name already exists.");
            return "register";
        }

        // check repeat password
        if (!adminDTO.getPassword().equals(adminDTO.getRepeatPassword())) {
            theModel.addAttribute("adminDTO", adminDTO);
            theModel.addAttribute("registrationError", "Incorrect repeat password");
            //session.setAttribute("registrationError", "Incorrect repeat password");

            System.out.println("Incorrect repeat password");
            return "register";
        }

        // create user account and store in the database
        adminService.save(adminDTO);

        System.out.println("Successfully create new admin");

        // place user in the web http session for later use
        session.setAttribute("admin", adminDTO);

        theModel.addAttribute("title", "Registration Confirmation Page");
        return "registration-confirmation";
    }
}
