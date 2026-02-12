package com.Eonline.Education.Service;


import com.Eonline.Education.exceptions.CartItemException;
import com.Eonline.Education.exceptions.UserException;
import com.Eonline.Education.modals.Cart;
import com.Eonline.Education.modals.CartItem;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CartItemServiceImplementation implements CartItemService {

    private CartItemRepository cartItemRepository;
    private UserService userService;

    public CartItemServiceImplementation(CartItemRepository cartItemRepository,UserService userService) {
        this.cartItemRepository=cartItemRepository;
        this.userService=userService;
    }

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setCoursePrice(cartItem.getCoursePrice());

        CartItem createdCartItem=cartItemRepository.save(cartItem);

        return createdCartItem;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception, UserException {

        CartItem item=findCartItemById(id);
        User user=userService.findUserById(item.getUserId());


        if(user.getId().equals(userId)) {

            item.setCoursePrice(item.getCoursePrice());

            return cartItemRepository.save(item);


        }
        else {
            throw new CartItemException("You can't update  another users cart_item");
        }

    }


    @Override
    public CartItem isCartItemExist(Cart cart, Long userId) {

        CartItem cartItem=cartItemRepository.isCartItemExist(cart, userId);

        return cartItem;
    }



    @Override
    public void removeCartItem(Long userId,Long cartItemId) throws Exception {

        System.out.println("userId- "+userId+" cartItemId "+cartItemId);

        CartItem cartItem=findCartItemById(cartItemId);

        User user=userService.findUserById(cartItem.getUserId());
        User reqUser=userService.findUserById(userId);

        if(user.getId().equals(reqUser.getId())) {
            cartItemRepository.deleteById(cartItem.getId());
        }
        else {
            throw new UserException("you can't remove anothor users item");
        }

    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt=cartItemRepository.findById(cartItemId);

        if(opt.isPresent()) {
            return opt.get();
        }
        throw new CartItemException("cartItem not found with id : "+cartItemId);
    }

}
