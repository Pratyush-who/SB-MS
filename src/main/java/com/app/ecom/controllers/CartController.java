package com.app.ecom.controllers;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader ("X-USER_ID") String userId, @RequestBody CartItemRequest request ) {
        if(!cartService.addToCart(userId, request)){
            return ResponseEntity.badRequest().body("Invalid product or insufficient stock or user not found");
        }
        return ResponseEntity.ok("Item added to cart successfully");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader ("X-USER_ID") String userId, @PathVariable String productId) {
        if(!cartService.deleteItemFromCart(userId, productId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
