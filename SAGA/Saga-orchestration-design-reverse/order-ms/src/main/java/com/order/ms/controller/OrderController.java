package com.order.ms.controller;

import com.order.ms.entity.Orders;
import com.order.ms.service.ReverseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.ms.dto.CustomerOrder;
import com.order.ms.dto.OrderEvent;
import com.order.ms.entity.OrderRepository;

@RestController
@RequestMapping("/api")
public class OrderController {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;
    @Autowired
    private ReverseOrder reverseOrder;


//	@PostMapping("/orders")
//	public void createOrder(@RequestBody CustomerOrder customerOrder) {
//		Orders order = new Orders();
//
//		try {
//			order.setAmount(customerOrder.getAmount());
//			order.setItem(customerOrder.getItem());
//			order.setQuantity(customerOrder.getQuantity());
//			order.setStatus("CREATED");
//
//			order = repository.save(order);
//
//			customerOrder.setOrderId(order.getId());
//
//			OrderEvent event = new OrderEvent();
//			event.setCustomerOrder(customerOrder);
//			event.setType("ORDER_CREATED");
//			kafkaTemplate.send("new-orders", event);
//		} catch (Exception e) {
//			order.setStatus("FAILED");
//			repository.save(order);
//		}
//	}

    @PostMapping("/revert-orders")
	public ResponseEntity<String> revertOrder(@RequestBody CustomerOrder customerOrder) {
        reverseOrder.reverseOrder(customerOrder);
        return ResponseEntity.status(HttpStatus.OK).body("Order reverted");
    }
}
