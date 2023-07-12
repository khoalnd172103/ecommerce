package com.ecommerce.library.repository;

import com.ecommerce.library.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Modifying
    @Query(value = "update cart_item set shopping_cart_id = null where shopping_cart_id = ?1", nativeQuery = true)
    void deleteCartItemById(Long cartId);
}
