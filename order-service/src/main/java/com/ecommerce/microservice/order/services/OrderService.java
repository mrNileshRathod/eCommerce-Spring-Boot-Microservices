package com.ecommerce.microservice.order.services;

import com.ecommerce.microservice.order.clients.InventoryClient;
import com.ecommerce.microservice.order.dto.OrderRequest;
import com.ecommerce.microservice.order.event.OrderPlacedEvent;
import com.ecommerce.microservice.order.model.Order;
import com.ecommerce.microservice.order.repository.OrderRepository;
import groovy.util.logging.Slf4j;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Transactional
    public boolean placeOrder(OrderRequest orderRequest) {
        boolean stockResponse = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (!stockResponse) return false;

        boolean isInventoryUpdated = inventoryClient.updateInventoryStockInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (!isInventoryUpdated) return false;

        Order order = mapToOrder(orderRequest);
        orderRepository.save(order);

        // Send message to kafka
        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
        orderPlacedEvent.setOrderNumber(order.getOrderNumber());
        orderPlacedEvent.setEmail(orderRequest.userDetails().email());
        orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
        orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());

        log.info("Start- Sending OrderPlacedEvent {} to Kafka Topic", orderPlacedEvent);
        kafkaTemplate.send("order-placed", orderPlacedEvent);
        log.info("End- Sending OrderPlacedEvent {} to Kafka Topic", orderPlacedEvent);

        return true;
    }

    private static Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());
        return order;
    }

}
