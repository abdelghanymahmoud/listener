package com.listener.consume;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

  private static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange" ;
  private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key" ;

  @Value("${exchange}")
  private String exchange;
  @Value("${bind-key}")
  private String bindKey;
  @Value("${queue}")
  private String queue;
  @Value("${queue-dl}")
  private String queueDl;

  @Bean
  public Jackson2JsonMessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  TopicExchange messageExchange() {
    return new TopicExchange(exchange, true, false);
  }

  @Bean
  public Queue messageSearchDeadLetterQueue() {
    Map<String, Object> args = new HashMap<>();
    return new Queue(queueDl, true, false, false, args);
  }

  @Bean
  public Queue messageProfileQueue() {
    Map<String, Object> args = new HashMap<>();
    // The default exchange
    args.put(X_DEAD_LETTER_EXCHANGE, "");
    // Route to the incoming queue when the TTL occurs
    args.put(X_DEAD_LETTER_ROUTING_KEY, queueDl);

    return new Queue(queue, true, false, false, args);
  }

  @Bean
  Binding bindingDoctorProfileQueue() {
    return BindingBuilder.bind(messageProfileQueue()).to(messageExchange()).with(bindKey);
  }
}
