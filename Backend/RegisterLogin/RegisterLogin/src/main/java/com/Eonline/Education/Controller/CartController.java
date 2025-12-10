package com.Eonline.Education.Controller;

import com.Eonline.Education.Request.AddItemRequest;
import com.Eonline.Education.Service.CartService;
import com.Eonline.Education.Service.UserService;
import com.Eonline.Education.modals.Cart;
import com.Eonline.Education.modals.CartItem;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private CartService cartService;
    private UserService userService;

    public CartController(CartService cartService,UserService userService) {
        this.cartService=cartService;
        this.userService=userService;
    }
    @GetMapping("/")
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {

        User user=userService.findUserProfileByJwt(jwt);

        Cart cart=cartService.findUserCart(user.getId());

        System.out.println("cart - "+cart.getUser().getEmail());

        return new ResponseEntity<Cart>(cart,HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {

        User user=userService.findUserProfileByJwt(jwt);

        CartItem item = cartService.addCartItem(user.getId(), req);

        ApiResponse res= new ApiResponse("Item Added To Cart Successfully",true);

        return new ResponseEntity<>(item,HttpStatus.ACCEPTED);

    }
}
