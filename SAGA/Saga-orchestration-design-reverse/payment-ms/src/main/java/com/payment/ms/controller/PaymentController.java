package com.payment.ms.controller;

import com.payment.ms.service.ReversePayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.ms.dto.CustomerOrder;
import com.payment.ms.dto.OrderEvent;
import com.payment.ms.dto.PaymentEvent;
import com.payment.ms.entity.Payment;
import com.payment.ms.entity.PaymentRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PaymentController {

	@Autowired
	private PaymentRepository repository;

	@Autowired
	private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaOrderTemplate;
    @Autowired
    ReversePayment reversePayment;

//	@KafkaListener(topics = "new-orders", groupId = "orders-group")
//	public void processPayment(String event) throws JsonMappingException, JsonProcessingException {
//		System.out.println("Recieved event for payment " + event);
//		OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
//
//		CustomerOrder order = orderEvent.getOrder();
//		Payment payment = new Payment();
//
//		try {
//			payment.setAmount(order.getAmount());
//			payment.setMode(order.getPaymentMode());
//			payment.setOrderId(order.getOrderId());
//			payment.setStatus("SUCCESS");
//			repository.save(payment);
//
//			PaymentEvent paymentEvent = new PaymentEvent();
//			paymentEvent.setOrder(orderEvent.getOrder());
//			paymentEvent.setType("PAYMENT_CREATED");
//			kafkaTemplate.send("new-payments", paymentEvent);
//		} catch (Exception e) {
//			payment.setOrderId(order.getOrderId());
//			payment.setStatus("FAILED");
//			repository.save(payment);
//
//			OrderEvent oe = new OrderEvent();
//			oe.setOrder(order);
//			oe.setType("ORDER_REVERSED");
//			kafkaOrderTemplate.send("reversed-orders", orderEvent);
//		}
//	}


    @PostMapping("/revert-payment")
    public ResponseEntity<String> reversePayment(@RequestBody CustomerOrder customerOrder) {
        reversePayment.reversePayment(customerOrder);
        return ResponseEntity.status(HttpStatus.OK).body("Payment reverted");
    }
}
