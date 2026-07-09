package com.kaiocosta.industriamonitor.messaging;

import com.kaiocosta.industriamonitor.domain.Alert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertPublisher {

    private final RabbitTemplate rabbitTemplate;

    public static final String QUEUE_NAME = "alerts.queue";

    public void publish(Alert alert) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, alert.getId());
        log.info("Alerta {} publicado na fila.", alert.getId());
    }
}
