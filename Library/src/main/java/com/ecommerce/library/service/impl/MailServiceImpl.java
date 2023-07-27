package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomerDTO;
import com.ecommerce.library.service.MailService;
import com.ecommerce.library.service.ThymeleafService;
import com.nimbusds.jose.util.StandardCharset;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    ThymeleafService thymeleafService;

    @Override
    public void sendMailTest() {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, //file, mail, text
                    StandardCharset.UTF_8.name()
            );

            helper.setFrom(email);
            helper.setText(thymeleafService.createContent("mail-sender-test.html", null), true);
            helper.setCc("khoaly090141@gmail.com");
            helper.setTo("khoalndse172103@fpt.edu.vn");
            helper.setSubject("Mail test with template html");

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMailCreateCustomer(CustomerDTO customerDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setTo(customerDTO.getUserName());

            //Object[] bccObject = customerDTO.getMailCC().toArray();
            //String[] bcc = Arrays.copyOf(bccObject, bccObject.length, String[].class);
            //helper.setBcc(bcc);

            Map<String, Object> variables = new HashMap<>();
            variables.put("email", customerDTO.getUserName());
            variables.put("first_name", customerDTO.getFirstName());
            variables.put("last_name", customerDTO.getLastName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            variables.put("date", sdf.format(new Date()));
            helper.setText(thymeleafService.createContent("create-customer-mail-template.html", variables), true);
            helper.setFrom(email);
            helper.setSubject("Welcome to Ray Shop");
            mailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
