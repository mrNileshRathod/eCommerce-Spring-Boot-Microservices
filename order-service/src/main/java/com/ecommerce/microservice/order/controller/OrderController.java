package com.ecommerce.microservice.order.controller;

import com.ecommerce.microservice.order.dto.OrderRequest;
import com.ecommerce.microservice.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        boolean isOrderPlaced = orderService.placeOrder(orderRequest);

        return isOrderPlaced ? "Order Placed Successfully" : "Order is failed";
    }

}
