package com.ecommerce.library.service;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;

public interface OrderService {

    Order saveOrder(ShoppingCart shoppingCart);
}
