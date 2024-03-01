package com.order.ms.service;
import com.order.ms.dto.CustomerOrder;
import org.springframework.stereotype.Component;
import java.util.Optional;

import com.order.ms.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms.dto.OrderEvent;

import com.order.ms.entity.OrderRepository;

@Component
public class ReverseOrder {

	@Autowired
	private OrderRepository repository;

	//@KafkaListener(topics = "order-reverse", groupId = "orders-group")
	public void reverseOrder(CustomerOrder customerOrder) {
		System.out.println("Inside reverse order for order "+customerOrder);

		try {
			Optional<Orders> order = repository.findById(customerOrder.getOrderId());

			order.ifPresent(o -> {
				o.setStatus("FAILED");
				this.repository.save(o);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
