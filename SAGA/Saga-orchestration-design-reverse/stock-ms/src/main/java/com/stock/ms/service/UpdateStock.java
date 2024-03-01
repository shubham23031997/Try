package com.stock.ms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.ms.dto.CustomerOrder;
import com.stock.ms.dto.DeliveryEvent;
import com.stock.ms.dto.OrderEvent;
import com.stock.ms.dto.PaymentEvent;
import com.stock.ms.entity.StockRepository;
import com.stock.ms.entity.WareHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UpdateStock {
    @Autowired
    private StockRepository repository;

    @Autowired
    private KafkaTemplate<String, DeliveryEvent> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaPaymentTemplate;
    @KafkaListener(topics = "update-stock", groupId = "payments-group-3")
    public void updateStock(String stockEvent) throws JsonMappingException, JsonProcessingException {
        System.out.println("Inside update stock for order "+stockEvent);

        DeliveryEvent event = new DeliveryEvent();

        OrderEvent p = new ObjectMapper().readValue(stockEvent, OrderEvent.class);
        CustomerOrder order = p.getCustomerOrder();

        try {
            Iterable<WareHouse> inventories = repository.findByItem(order.getItem());

            boolean exists = inventories.iterator().hasNext();

            if (!exists) {
                System.out.println("Stock not exist so reverting the order");
                throw new Exception("Stock not available");
            }

            for(WareHouse iterable:inventories){
                int quantityLeft = iterable.getQuantity() - order.getQuantity();
                if(quantityLeft<0){
                    System.out.println("Enough Stock quantity not exist so reverting the order");
                    throw new Exception("Stock quantity not available");
                }
            }

            inventories.forEach(i -> {
                i.setQuantity(i.getQuantity() - order.getQuantity());

                repository.save(i);
            });

            event.setType("STOCK-UPDATED");
            event.setCustomerOrder(p.getCustomerOrder());
            kafkaTemplate.send("stock-updated", event);
        } catch (Exception e) {
            PaymentEvent pe = new PaymentEvent();
            pe.setCustomerOrder(order);
            pe.setType("PAYMENT_REVERSED");
            kafkaPaymentTemplate.send("reverse-payment", pe);
        }
    }
}
