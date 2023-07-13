package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.OrderDetail;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailServiceImpl (OrderDetailRepository theOrderDetailRepository) {
        this.orderDetailRepository = theOrderDetailRepository;
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrderId(Long id) {

        return orderDetailRepository.findAllByOrderId(id);
    }
}
