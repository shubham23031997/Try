package com.payment.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.ms.dto.CustomerOrder;
import com.payment.ms.dto.OrderEvent;
import com.payment.ms.dto.PaymentEvent;
import com.payment.ms.entity.Payment;
import com.payment.ms.entity.PaymentRepository;

@Component
public class ReversePayment {

	@Autowired
	private PaymentRepository repository;

	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;

	//@KafkaListener(topics = "payment-reverse", groupId = "payments-group")
	public void reversePayment(CustomerOrder customerOrder) {
		System.out.println("Inside reverse payment for order "+customerOrder);
		try {
			//PaymentEvent paymentEvent = new ObjectMapper().readValue(event, PaymentEvent.class);

			Iterable<Payment> payments = this.repository.findByOrderId(customerOrder.getOrderId());
			payments.forEach(p -> {
				p.setStatus("FAILED");
				repository.save(p);
			});

			OrderEvent orderEvent = new OrderEvent();
			orderEvent.setCustomerOrder(customerOrder);
			orderEvent.setType("ORDER-REVERSED");
			kafkaTemplate.send("order-reverse", orderEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
