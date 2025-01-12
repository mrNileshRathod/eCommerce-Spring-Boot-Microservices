package com.ecommerce.microservice.inventory.service;

import com.ecommerce.microservice.inventory.model.Inventory;
import com.ecommerce.microservice.inventory.repository.InventoryRepository;
import groovy.util.logging.Slf4j;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    Logger log = LoggerFactory.getLogger(InventoryRepository.class);

    public boolean isInStock(String skuCode, Integer quantity) {
        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }

    @Transactional
    public boolean updateInventoryStock(String skuCode, Integer quantity) {
        try {
            Inventory inventory = inventoryRepository.findBySkuCode(skuCode);
            inventory.setQuantity(inventory.getQuantity() - quantity);
            Inventory updatedInventory = inventoryRepository.save(inventory);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;
    }
}
