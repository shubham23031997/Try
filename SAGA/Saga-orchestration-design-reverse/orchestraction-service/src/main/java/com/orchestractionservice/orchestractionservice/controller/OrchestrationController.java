package com.orchestractionservice.orchestractionservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchestractionservice.orchestractionservice.model.CustomerOrder;
import com.orchestractionservice.orchestractionservice.model.OrderEvent;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class OrchestrationController {
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;
    @Autowired
    RestTemplate restTemplate;
    @PostMapping("/createOrder")
    public void createOrder(@RequestBody CustomerOrder customerOrder) {
            OrderEvent event = new OrderEvent();
            event.setCustomerOrder(customerOrder);
            event.setType("ORDER_CREATED");
            kafkaTemplate.send("create-order", event);
    }
    @KafkaListener(topics = "order-success", groupId = "orders-group-2")
    public void initiatePayment(String orderEvent) throws JsonProcessingException {
        System.out.println("inside orchest initiatepayment");
        OrderEvent event = new ObjectMapper().readValue(orderEvent, OrderEvent.class);

        OrderEvent paymentEvent = new OrderEvent();
        paymentEvent.setCustomerOrder(event.getCustomerOrder());
        paymentEvent.setType("PAYMENT-INITIATED");
        kafkaTemplate.send("initiate-payment", paymentEvent);
    }

    @KafkaListener(topics = "order-reverse", groupId = "orders-group-2")
    public void revertOrder(String orderEvent) throws JsonProcessingException {
        System.out.println("inside orchest initiate payment");
        OrderEvent event = new ObjectMapper().readValue(orderEvent, OrderEvent.class);
        OrderEvent order = new OrderEvent();
        order.setCustomerOrder(event.getCustomerOrder());
        order.setType("ORDER-REVERTED");
        // Define the POST request URL
        String apiUrl = "http://localhost:8080/api/revert-orders";
        // Create HttpHeaders and set the content type
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        // Create an HttpEntity with the request body and headers
        HttpEntity<CustomerOrder> requestEntity = new HttpEntity<>(event.getCustomerOrder(), headers);
        // Send the POST request
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        System.out.println(response);
         //restTemplate.getForObject(url, CustomerOrder.class);
        //kafkaTemplate.send("order-reverse", order);
    }


    @KafkaListener(topics = "payment-success", groupId = "payment-group-2")
    public void updateStock(String orderEvent) throws JsonProcessingException {
        System.out.println("inside orchest updatestock");
        OrderEvent event = new ObjectMapper().readValue(orderEvent, OrderEvent.class);
        OrderEvent stockEvent = new OrderEvent();
        stockEvent.setCustomerOrder(event.getCustomerOrder());
        stockEvent.setType("UPDATE-STOCK");
        kafkaTemplate.send("update-stock", stockEvent);
        System.out.println("after orchest updatestock");
    }

    @KafkaListener(topics = "reverse-payment", groupId = "payment-group-2")
    public void paymentReverse(String orderEvent) throws JsonProcessingException {
        System.out.println("inside reverse payment");
        OrderEvent event = new ObjectMapper().readValue(orderEvent, OrderEvent.class);
        OrderEvent stockEvent = new OrderEvent();
        stockEvent.setCustomerOrder(event.getCustomerOrder());
        stockEvent.setType("UPDATE-STOCK");
        String apiUrl = "http://localhost:8081/revert-payment";
        // Create HttpHeaders and set the content type
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        // Create an HttpEntity with the request body and headers
        HttpEntity<CustomerOrder> requestEntity = new HttpEntity<>(event.getCustomerOrder(), headers);
        // Send the POST request
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        System.out.println(response);
        //kafkaTemplate.send("update-stock", stockEvent);
        System.out.println("after reverse payment");
    }

    @KafkaListener(topics = "stock-updated", groupId = "stock-group-2")
    public void initiateDelivery(String orderEvent) throws JsonProcessingException {
        System.out.println("inside initiateDelivery ");
        OrderEvent event = new ObjectMapper().readValue(orderEvent, OrderEvent.class);
        OrderEvent deliveryEvent = new OrderEvent();
        deliveryEvent.setCustomerOrder(event.getCustomerOrder());
        deliveryEvent.setType("INITIATE-DELIVERY");
        kafkaTemplate.send("initiate-delivery", deliveryEvent);
        System.out.println("after initiateDelivery ");
    }
}
