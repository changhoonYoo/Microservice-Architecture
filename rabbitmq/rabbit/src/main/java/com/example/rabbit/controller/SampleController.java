package com.example.rabbit.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    private static final String EXCHANGE_NAME = "sample.exchange";


    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SampleController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("sample/queue")
    public String samplePublish() {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "sample.routing.#", "RabbitMQ + SpringBoot = Success");
        return "Message Sending!";
    }
}
