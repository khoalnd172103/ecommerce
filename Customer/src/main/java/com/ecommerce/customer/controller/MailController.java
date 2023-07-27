package com.ecommerce.customer.controller;

import com.ecommerce.library.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("/send-text")
    public String sendMailTest() {
        mailService.sendMailTest();
        return "Success";
    }
}
