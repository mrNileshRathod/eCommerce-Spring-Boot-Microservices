package com.ecommerce.microservice.inventory.controller;

import com.ecommerce.microservice.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean updateInventoryStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        return inventoryService.updateInventoryStock(skuCode, quantity);
    }
}
