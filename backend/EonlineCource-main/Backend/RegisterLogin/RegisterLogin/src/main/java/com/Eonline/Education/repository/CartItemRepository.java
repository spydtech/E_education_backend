package com.Eonline.Education.repository;


import com.Eonline.Education.modals.Cart;
import com.Eonline.Education.modals.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Add custom methods if needed
    List<CartItem> findByUserId(Long userId);
    @Query("SELECT ci From CartItem ci Where ci.cart=:cart And ci.userId=:userId")
    public CartItem isCartItemExist(@Param("cart") Cart cart, @Param("userId")Long userId);

}
