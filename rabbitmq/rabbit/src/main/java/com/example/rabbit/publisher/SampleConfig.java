package com.example.rabbit.publisher;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;

public class SampleConfig {

    private static final String EXCHANGE_NAME = "sample.exchange";
    private static final String QUEUE_NAME = "sample.queue";
    private static final String ROUTING_KEY = "sample.routing.#";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        // exchange 와 routing key 의 패턴이 일치하는 queue 에 메시지를 전달하겠다는 규칙
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 메시지에 담을 Object 를 rabbitmq 의 메시지 형식으로 변환
        // Jackson2JsonMessageConverter 가 변환한 메시지는 body 형식의 JSON message 로 변환
        // Jackson2JsonMessageConverter 를 사용하지 않는다면 ObjectMapper 를 통해 변환 작업 수행해야 하는 추가 작업이 필요
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
