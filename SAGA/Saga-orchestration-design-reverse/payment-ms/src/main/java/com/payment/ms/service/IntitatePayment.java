package com.payment.ms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.ms.dto.CustomerOrder;
import com.payment.ms.dto.OrderEvent;
import com.payment.ms.dto.PaymentEvent;
import com.payment.ms.entity.Payment;
import com.payment.ms.entity.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
@Component
public class IntitatePayment {
    @Autowired
    private PaymentRepository repository;

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    @Autowired
	private KafkaTemplate<String, OrderEvent> kafkaOrderTemplate;

    @KafkaListener(topics = "initiate-payment", groupId = "payments-group-2")
    public void intitatePayment(String event) throws JsonProcessingException {
        System.out.println("Inside initiate payment for order " + event);
        Payment payment = new Payment();

        OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
        CustomerOrder order = orderEvent.getCustomerOrder();
        try{
            payment.setAmount(order.getAmount());
            payment.setMode(order.getPaymentMode());
            payment.setOrderId(order.getOrderId());
            payment.setStatus("SUCCESS");
            repository.save(payment);

            PaymentEvent paymentEvent = new PaymentEvent();
            paymentEvent.setCustomerOrder(orderEvent.getCustomerOrder());
            paymentEvent.setType("PAYMENT-SUCCESS");
            kafkaTemplate.send("payment-success", paymentEvent);
        } catch (Exception e) {
            payment.setOrderId(order.getOrderId());
            payment.setStatus("FAILED");
            repository.save(payment);

            OrderEvent oe = new OrderEvent();
            oe.setCustomerOrder(order);
            oe.setType("ORDER-REVERSED");
            kafkaOrderTemplate.send("order-reverse", oe);
        }
    }
}