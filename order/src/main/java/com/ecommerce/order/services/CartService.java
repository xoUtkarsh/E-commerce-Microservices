package com.ecommerce.order.services;

import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    int attempt=0;

//    @CircuitBreaker(name = "productService",fallbackMethod = "addToCartFallback")
    @Retry(name = "retryBreaker",fallbackMethod = "addToCartFallback")
    public boolean addToCart(String userId, CartItemRequest request) {
        // Look for product
        System.out.println("ATTEMPT: "+ ++attempt);
        ProductResponse productResponse = productServiceClient.getProductDetails(String.valueOf(request.getProductId()));
        if (productResponse==null)
            return false;

        if (productResponse.getStockQuantity() < request.getQuantity())
            return false;

        UserResponse userResponse=userServiceClient.getUserDetails(userId);
        if(userResponse==null)
            return false;

//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty())
//            return false;

//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existingCartItem != null) {
            // Update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean addToCartFallback(String userId,CartItemRequest request,Exception exception){
//        System.out.println("FALLBACK CALLED");
        exception.printStackTrace();
        return false;

    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        CartItem cartItem= cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem!=null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
