package com.neosoft.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.jsonQueue.name}")
    private String jsonQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.jsonExchange.name}")
    private String jsonExchange;

    @Value("${rabbitmq.routingKey.name}")
    private String routingKey;

    @Value("${rabbitmq.jsonRoutingKey.name}")
    private String jsonRoutingKey;

    //spring bean for Rabbitmq queue
    @Bean
    public Queue queue() {
        return new Queue("shubham");
    }

    @Bean
    public Queue jsonQueue() {
        return new Queue("shubham_json");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("shubham_exchange");
    }


    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(topicExchange())
                .with("shubham_routing_key");

    }

    @Bean
    public Binding jsonBinding() {
        return BindingBuilder
                .bind(jsonQueue())
                .to(topicExchange())
                .with("shubham_jsonRouting_key");
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    //  this use for java object into json and json into java object
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
