// RabbitMQConfig.java
package com.meraproject.apigateway.RabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitMQConfig {

    // Declare a simple queue
    @Bean
    public Queue helloQueue() {
        return new Queue("hello-queue", true); // durable = true
    }

     @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange("fanout-exchange");
    }

    @Bean
    public Queue queue1() {
        return new Queue("queue1");
    }

    @Bean
    public Queue queue2() {
        return new Queue("queue2");
    }

    @Bean
    public Binding binding1(Queue queue1, FanoutExchange exchange) {
        return BindingBuilder.bind(queue1).to(exchange);
    }

    @Bean
    public Binding binding2(Queue queue2, FanoutExchange exchange) {
        return BindingBuilder.bind(queue2).to(exchange);
    }

    // ========================
    // CONSTANTS
    // ========================
    public static final String MAIN_EXCHANGE = "main-exchange";
    public static final String MAIN_QUEUE = "main-queue";

    public static final String DLX = "dlx";
    public static final String DLQ = "dead-letter-queue";

    public static final String ROUTING_KEY = "main-key";
    public static final String DLQ_ROUTING_KEY = "dlq-key";

    // ========================
    // MAIN FLOW
    // ========================

    // Exchange where producer sends message
    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EXCHANGE);
    }

    // Main Queue (IMPORTANT PART)
    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(MAIN_QUEUE)
                .deadLetterExchange(DLX)              // where to send on failure
                .deadLetterRoutingKey(DLQ_ROUTING_KEY) // HOW to route in DLX
                .build();
    }

    // Binding MAIN QUEUE to MAIN EXCHANGE
    @Bean
    public Binding mainBinding() {
        return BindingBuilder
                .bind(mainQueue())
                .to(mainExchange())
                .with(ROUTING_KEY);
    }

    // ========================
    // DLQ FLOW
    // ========================

    // Dead Letter Exchange
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX);
    }

    // DLQ (NORMAL QUEUE)
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ);
    }

    // Binding DLQ to DLX
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }
}