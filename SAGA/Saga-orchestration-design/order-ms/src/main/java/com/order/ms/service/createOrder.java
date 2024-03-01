package com.order.ms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms.dto.OrderEvent;
import com.order.ms.entity.OrderRepository;
import com.order.ms.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class createOrder {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @KafkaListener(topics = "create-order", groupId = "orders-group-2")
    public void createorder(String event) throws JsonProcessingException {
        System.out.println("Inside create order for order " + event);
        Orders orders = new Orders();
        try {
            OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
            //Optional<Orders> order = repository.findById(orderEvent.getOrder().getOrderId());
            orders.setAmount(orderEvent.getCustomerOrder().getAmount());
            orders.setItem(orderEvent.getCustomerOrder().getItem());
            orders.setQuantity(orderEvent.getCustomerOrder().getQuantity());
            orders.setStatus("CREATED");

            orders = repository.save(orders);
            orderEvent.getCustomerOrder().setOrderId(orders.getId());
            OrderEvent createEvent = new OrderEvent();
            createEvent.setCustomerOrder(orderEvent.getCustomerOrder());
            createEvent.setType("ORDER_CREATED");
            kafkaTemplate.send("order-success", createEvent);
        }catch (Exception e) {
            orders.setStatus("FAILED");
            repository.save(orders);
        }
    }
}
