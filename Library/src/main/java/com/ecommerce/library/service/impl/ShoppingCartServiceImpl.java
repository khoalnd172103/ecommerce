package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart addItemToCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();

        if (cart == null) {
            cart = new ShoppingCart();
        }

        Set<CartItem> cartItems = cart.getCartItemSet();
        CartItem cartItem = findCartItem(cartItems, product.getId());

        if (cartItems == null) {
            cartItems = new HashSet<>();
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(quantity * product.getCostPrice());
                cartItem.setShoppingCart(cart);
                cartItems.add(cartItem);
                cartItemRepository.save(cartItem);
            }
        } else {
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(quantity * product.getCostPrice());
                cartItem.setShoppingCart(cart);
                cartItems.add(cartItem);
                cartItemRepository.save(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity());
                cartItem.setTotalPrice(cartItem.getTotalPrice() + (quantity * product.getCostPrice()));
                cartItemRepository.save(cartItem);
            }
        }
        cart.setCartItemSet(cartItems);

        int totalItems = totalItems(cart.getCartItemSet());
        int totalPrice = totalItems(cart.getCartItemSet());

        cart.setTotalItem(totalItems);
        cart.setTotalPrice(totalPrice);
        cart.setCustomer(customer);

        return shoppingCartRepository.save(cart);
    }

    private int totalItems(Set<CartItem> cartItems) {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }

        return total;
    }

    private double totalPrice(Set<CartItem> cartItems) {
        double total = 0;
        for(CartItem item : cartItems) {
            total += item.getTotalPrice();
        }

        return total;
    }

    private CartItem findCartItem(Set<CartItem> cartItems, Long productId) {
        if (cartItems == null) {
            return null;
        } else {
            CartItem cartItem = null;

            for (CartItem item : cartItems) {
                if (item.getProduct().getId() == productId) {
                    cartItem = item;
                }
            }

            return cartItem;
        }
    }
}
