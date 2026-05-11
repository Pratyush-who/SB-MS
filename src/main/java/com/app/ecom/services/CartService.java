package com.app.ecom.services;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.models.CartItem;
import com.app.ecom.models.Product;
import com.app.ecom.models.User;
import com.app.ecom.repositories.CartItemRepo;
import com.app.ecom.repositories.ProductRepository;
import com.app.ecom.repositories.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
        if (request == null || request.getProductId() == null || request.getQuantity() == null || request.getQuantity() <= 0) {
            return false;
        }

        Long parsedUserId;
        try {
            parsedUserId = Long.valueOf(userId);
        } catch (NumberFormatException ex) {
            return false;
        }

        Optional<User> userOpt = userRepository.findById(parsedUserId);
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (userOpt.isEmpty() || productOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        Product product = productOpt.get();
        if (product.getPrice() == null || product.getStockQuantity() == null || product.getStockQuantity() < request.getQuantity()) {
            return false;
        }

        CartItem existingCartItem = cartItemRepo.findByUserAndProduct(user, product);
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + request.getQuantity();
            if (product.getStockQuantity() < newQuantity) {
                return false;
            }
            existingCartItem.setQuantity(newQuantity);
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
            cartItemRepo.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepo.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId) {
        final Long parsedUserId;
        final Long parsedProductId;
        try {
            parsedUserId = Long.valueOf(userId);
            parsedProductId = Long.valueOf(productId);
        } catch (NumberFormatException ex) {
            return false;
        }

        Optional<User> userOpt = userRepository.findById(parsedUserId);
        Optional<Product> productOpt = productRepository.findById(parsedProductId);
        if (userOpt.isEmpty() || productOpt.isEmpty()) {
            return false;
        }

        CartItem cartItem = cartItemRepo.findByUserAndProduct(userOpt.get(), productOpt.get());
        if (cartItem == null) {
            return false;
        }

        cartItemRepo.delete(cartItem);
        return true;
    }

    public List<CartItem> getCartItems(String userId) {
        final Long parsedUserId;
        try {
            parsedUserId = Long.valueOf(userId);
        } catch (NumberFormatException ex) {
            return List.of();
        }

        return userRepository.findById(parsedUserId)
                .map(cartItemRepo::findByUser)
                .orElse(List.of());
    }

    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId)).ifPresent(user ->
                cartItemRepo.deleteByUser(user));
    }
}