package com.stock.ms.service;

import com.stock.ms.dto.CustomerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.ms.dto.DeliveryEvent;
import com.stock.ms.dto.PaymentEvent;
import com.stock.ms.entity.WareHouse;
import com.stock.ms.entity.StockRepository;

@RestController
public class ReversesStock {

	@Autowired
	private StockRepository repository;

	@Autowired
	private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

	//@KafkaListener(topics = "reversed-stock", groupId = "stock-group")
	public void reverseStock(CustomerOrder customerOrder) {
		System.out.println("Inside reverse stock for order "+customerOrder);

		try {
			//DeliveryEvent deliveryEvent = new ObjectMapper().readValue(event, DeliveryEvent.class);

			Iterable<WareHouse> inv = this.repository.findByItem(customerOrder.getItem());

			inv.forEach(i -> {
				i.setQuantity(i.getQuantity() + customerOrder.getQuantity());
				repository.save(i);
			});

			PaymentEvent paymentEvent = new PaymentEvent();
			paymentEvent.setCustomerOrder(customerOrder);
			paymentEvent.setType("PAYMENT_REVERSED");
			kafkaTemplate.send("reversed-payments", paymentEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
