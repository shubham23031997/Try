package com.neosoft.springbootrabbitmq.publisher;

import com.neosoft.springbootrabbitmq.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQJsonProducer {

    @Autowired
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.jsonQueue.name}")
    private String jsonQueue;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.jsonExchange.name}")
    private String jsonExchange;
    @Value("${rabbitmq.jsonRoutingKey.name}")
    private String jsonRoutingKey;

    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendJsonMessage(User user) {
        log.info(String.format("Json message sent to -> %s", user.toString()));
        rabbitTemplate.convertAndSend(exchange, jsonRoutingKey, user);
    }
}
