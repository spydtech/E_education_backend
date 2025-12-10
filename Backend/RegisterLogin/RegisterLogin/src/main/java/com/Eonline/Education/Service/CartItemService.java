package com.Eonline.Education.Service;

import com.Eonline.Education.exceptions.CartItemException;
import com.Eonline.Education.exceptions.UserException;
import com.Eonline.Education.modals.Cart;
import com.Eonline.Education.modals.CartItem;

public interface CartItemService {
    public CartItem createCartItem(CartItem cartItem);

    public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws Exception;

    public CartItem isCartItemExist(Cart cart,Long userId);

    public void removeCartItem(Long userId,Long cartItemId) throws Exception;

    public CartItem findCartItemById(Long cartItemId) throws CartItemException;
}
