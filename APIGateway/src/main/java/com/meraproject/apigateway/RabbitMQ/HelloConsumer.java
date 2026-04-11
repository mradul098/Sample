// HelloConsumer.java
package com.meraproject.apigateway.RabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.*;

@Component
public class HelloConsumer {

    @RabbitListener(queues = "hello-queue")
    public void receiveMessage(String message) {
        System.out.println("📥 API Gateway received: " + message);
    }

    @RabbitListener(queues = "queue1")
    public void receiveMessage1(String message) {
        System.out.println("📥 API Gateway received q1: " + message);
    }

    @RabbitListener(queues = "queue2")
    public void receiveMessage2(String message) {
        System.out.println("📥 API Gateway received q2: " + message);
    }

    @RabbitListener(queues = "main-queue")
    public void consume(String msg) {
        System.out.println("Processing: " + msg);
        throw new RuntimeException("Failing intentionally");
    }

    
}