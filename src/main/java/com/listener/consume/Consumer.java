package com.listener.consume;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

  @RabbitListener(queues = "${queue}")
  public void listen(@Payload Message message, @Header("header1") String header) {
    System.err.println(message.toString());
    System.err.println(header);
  }
}
