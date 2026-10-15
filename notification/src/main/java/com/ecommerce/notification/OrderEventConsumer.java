package com.ecommerce.notification;

import com.ecommerce.notification.payload.OrderCreatedEvent;
import com.ecommerce.notification.payload.OrderStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderEvent){
        System.out.println("Received Order Event: " + orderEvent);

        long orderId= orderEvent.getOrderId();
        OrderStatus orderStatus= orderEvent.getStatus();
        System.out.println("Order ID: " + orderId);
        System.out.println("Order Status: " + orderStatus);
    }
}
