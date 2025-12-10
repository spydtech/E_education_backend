package com.Eonline.Education.Service;

import com.Eonline.Education.Request.AddItemRequest;
import com.Eonline.Education.modals.Cart;
import com.Eonline.Education.modals.CartItem;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImplementation implements CartService{
    private final CartRepository cartRepository;
    private final CartItemService cartItemService;

    @Autowired
    public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
    }
    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        Cart createdCart=cartRepository.save(cart);
        return createdCart;
    }
    @Override
    public CartItem addCartItem(Long userId, AddItemRequest req) throws Exception {
        Cart cart=cartRepository.findByUserId(userId);


        CartItem isPresent=cartItemService.isCartItemExist(cart,userId);

        if(isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCourseName(req.getCourseName());
            cartItem.setCart(cart);
            cartItem.setUserId(userId);
            cartItem.setCoursePrice(req.getCoursePrice());

            CartItem createdCartItem=cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
            return createdCartItem;
        }


        return isPresent;
    }

    public Cart findUserCart(Long userId) {
        Cart cart =	cartRepository.findByUserId(userId);
        int totalPrice=0;
        int totalDiscountedPrice=0;
        int totalItem=0;
        for(CartItem cartsItem : cart.getCartItems()) {
            totalPrice+=cartsItem.getCoursePrice();

        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalItem(cart.getCartItems().size());
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setDiscounte(totalPrice-totalDiscountedPrice);
        cart.setTotalItem(totalItem);

        return cartRepository.save(cart);

    }
}
