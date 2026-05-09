package com.app.ecom.services;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.models.CartItem;
import com.app.ecom.models.Product;
import com.app.ecom.models.User;
import com.app.ecom.repositories.CartItemRepo;
import com.app.ecom.repositories.ProductRepository;
import com.app.ecom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty()) {
            return false;
        }
        Product product = productOpt.get();
        if(product.getStockQuantity() < request.getQuantity()) {
            return false;
        }
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        CartItem existingCartItem = cartItemRepo.findByUserIdAndProductId(user,product);
        if(existingCartItem!=null){
            //update the qty
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(new java.math.BigDecimal(existingCartItem.getQuantity())));
            cartItemRepo.save(existingCartItem);
        }
        else{
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(new java.math.BigDecimal(request.getQuantity())));
            cartItemRepo.save(cartItem);
        }
        return true;
    }
}
