package com.stock.ms.controller;

import com.stock.ms.service.ReversesStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.ms.dto.CustomerOrder;
import com.stock.ms.dto.DeliveryEvent;
import com.stock.ms.dto.PaymentEvent;
import com.stock.ms.dto.Stock;
import com.stock.ms.entity.WareHouse;
import com.stock.ms.entity.StockRepository;

@RestController
@RequestMapping("/api")
public class StockController {

	@Autowired
	private StockRepository repository;

	@Autowired
	private KafkaTemplate<String, DeliveryEvent> kafkaTemplate;

	@Autowired
	private KafkaTemplate<String, PaymentEvent> kafkaPaymentTemplate;

	@Autowired
	ReversesStock reversesStock;

//	@KafkaListener(topics = "new-payments", groupId = "payments-group")
//	public void updateStock(String paymentEvent) throws JsonMappingException, JsonProcessingException {
//		System.out.println("Inside update inventory for order "+paymentEvent);
//
//		DeliveryEvent event = new DeliveryEvent();
//
//		PaymentEvent p = new ObjectMapper().readValue(paymentEvent, PaymentEvent.class);
//		CustomerOrder order = p.getOrder();
//
//		try {
//			Iterable<WareHouse> inventories = repository.findByItem(order.getItem());
//
//			boolean exists = inventories.iterator().hasNext();
//
//			if (!exists) {
//				System.out.println("Stock not exist so reverting the order");
//				throw new Exception("Stock not available");
//			}
//
//			for(WareHouse iterable:inventories){
//				int quantityLeft = iterable.getQuantity() - order.getQuantity();
//				if(quantityLeft<0){
//					System.out.println("Enough Stock quantity not exist so reverting the order");
//					throw new Exception("Stock quantity not available");
//				}
//			}
//
//			inventories.forEach(i -> {
//				i.setQuantity(i.getQuantity() - order.getQuantity());
//
//				repository.save(i);
//			});
//
//			event.setType("STOCK_UPDATED");
//			event.setOrder(p.getOrder());
//			kafkaTemplate.send("new-stock", event);
//		} catch (Exception e) {
//			PaymentEvent pe = new PaymentEvent();
//			pe.setOrder(order);
//			pe.setType("PAYMENT_REVERSED");
//			kafkaPaymentTemplate.send("reversed-payments", pe);
//		}
//	}

	@PostMapping("/addItems")
	public void addItems(@RequestBody Stock stock) {
		Iterable<WareHouse> items = repository.findByItem(stock.getItem());

		if (items.iterator().hasNext()) {
			items.forEach(i -> {
				i.setQuantity(stock.getQuantity() + i.getQuantity());
				repository.save(i);
			});
		} else {
			WareHouse i = new WareHouse();
			i.setItem(stock.getItem());
			i.setQuantity(stock.getQuantity());
			repository.save(i);
		}
	}

	@PostMapping("/revert-stock")
	public ResponseEntity<String> revertStock(@RequestBody CustomerOrder customerOrder) {
		reversesStock.reverseStock(customerOrder);
		return ResponseEntity.status(HttpStatus.OK).body("revert-stock");
	}
}
