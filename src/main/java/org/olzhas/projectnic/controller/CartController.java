package org.olzhas.projectnic.controller;

import lombok.AllArgsConstructor;
import org.olzhas.projectnic.dto.CartDto;
import org.olzhas.projectnic.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.addProductToCart(userId, productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/remove")
    ResponseEntity<String> removeProductFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{customerId}")
    ResponseEntity<String> deleteCart(@PathVariable("customerId") Long customerId) {
        cartService.clearCart(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/{userId}")
    ResponseEntity<CartDto> getCartForUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(cartService.getCartForUser(userId));
    }
}
