package com.neosoft.springbootrabbitmq.consumer;

import com.neosoft.springbootrabbitmq.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQJsonConsumer {

    @RabbitListener(queues = {"${rabbitmq.jsonQueue.name}"})
    public void consumeJson(User user) {
        log.info(String.format("received json message %s..", user.toString()));
    }
}