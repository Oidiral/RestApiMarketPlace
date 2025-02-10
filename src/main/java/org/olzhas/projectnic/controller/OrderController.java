package org.olzhas.projectnic.controller;

import lombok.RequiredArgsConstructor;
import org.olzhas.projectnic.dto.OrdersDto;
import org.olzhas.projectnic.service.impl.OrdersServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrdersServiceImpl ordersServiceImpl;

    @PostMapping("/createOrder/{userId}")
    public ResponseEntity<OrdersDto> createOrder(@PathVariable Long userId){
        return new ResponseEntity<>(ordersServiceImpl.createOrder(userId), HttpStatus.CREATED);
    }

    @GetMapping("/getAllOrders/{userId}")
    public ResponseEntity<Page<OrdersDto>> getAllOrders(@PageableDefault(size = 5, sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable,
                                                        @PathVariable("userId") Long userId){
        return new ResponseEntity<>(ordersServiceImpl.getUserOrders(pageable, userId), HttpStatus.OK);
    }

    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<OrdersDto> getOrder(@PathVariable("orderId") Long orderId){
        return new ResponseEntity<>(ordersServiceImpl.getOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/cancelOrder/")
    public ResponseEntity<OrdersDto> cancelOrder(@RequestBody Long orderId, @RequestParam Long userId){
        return new ResponseEntity<>(ordersServiceImpl.cancelOrder(orderId, userId), HttpStatus.OK);
    }



}
