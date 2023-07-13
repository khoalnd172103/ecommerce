package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetail;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    public OrderServiceImpl(OrderDetailRepository theOrderDetailRepository,
                            OrderRepository theOrderRepository,
                            ShoppingCartService theShoppingCartService,
                            CartItemRepository theCartItemRepository) {
        this.orderDetailRepository = theOrderDetailRepository;
        this.orderRepository = theOrderRepository;
        this.shoppingCartService = theShoppingCartService;
        this.cartItemRepository = theCartItemRepository;
    }

    @Override
    public Order saveOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderStatus("PENDING");
        order.setOrderDate(new Date());
        order.setTax(2);
        order.setPayment("CASH");
        order.setCustomer(shoppingCart.getCustomer());
        order.setTotalPrice(shoppingCart.getTotalPrice());
        order.setQuantity(shoppingCart.getTotalItem());
        orderRepository.save(order);

        List<OrderDetail> orderDetailList = new ArrayList<>();

        for(CartItem item : shoppingCart.getCartItemSet()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            orderDetailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }

        order.setOrderDetailList(orderDetailList);
        shoppingCartService.deleteCartById(shoppingCart.getId());
        return orderRepository.save(order);
    }

    @Override
    public Order findById(Long id) {
        Optional<Order> result = orderRepository.findById(id);

        Order order = null;

        if (result.isPresent()) {
            order = result.get();
        }

        return order;
    }

    @Override
    public void cancelOrderById(Long id) {
        Order order = orderRepository.findById(id).get();

        order.setOrderStatus("CANCEL");

        orderRepository.save(order);
    }
}
