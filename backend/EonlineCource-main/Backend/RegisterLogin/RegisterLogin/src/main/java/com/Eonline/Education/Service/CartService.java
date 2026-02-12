package com.Eonline.Education.Service;


import com.Eonline.Education.Request.AddItemRequest;
import com.Eonline.Education.modals.Cart;
import com.Eonline.Education.modals.CartItem;
import com.Eonline.Education.modals.User;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    public Cart createCart(User user);

    public CartItem addCartItem(Long userId, AddItemRequest req) throws Exception;

    public Cart findUserCart(Long userId);
}