package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDTO;

public interface MailService {

    void sendMailTest();

    void sendMailCreateCustomer(CustomerDTO customerDTO);
}
