package com.kaiocosta.industriamonitor.messaging;

import com.kaiocosta.industriamonitor.service.AIAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertConsumer {

    private final AIAnalysisService aiAnalysisService;

    @RabbitListener(queues = AlertPublisher.QUEUE_NAME)
    public void consume(Long alertId) {
        log.info("Alerta {} recebido da fila. Acionando análise de IA...", alertId);
        aiAnalysisService.analyze(alertId);
    }
}
