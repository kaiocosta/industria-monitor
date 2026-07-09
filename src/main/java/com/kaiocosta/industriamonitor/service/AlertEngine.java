package com.kaiocosta.industriamonitor.service;

import com.kaiocosta.industriamonitor.domain.Alert;
import com.kaiocosta.industriamonitor.domain.Machine;
import com.kaiocosta.industriamonitor.domain.SensorReading;
import com.kaiocosta.industriamonitor.repository.AlertRepository;
import com.kaiocosta.industriamonitor.messaging.AlertPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertEngine {

    private final AlertRepository alertRepository;
    private final AlertPublisher alertPublisher;

    public void evaluate(SensorReading reading, Double normalThreshold, Double criticalThreshold) {
        double value = reading.getValue();

        if (value <= normalThreshold) {
            return;
        }

        String severity = value > criticalThreshold ? "CRITICAL" : "WARNING";

        Alert alert = new Alert(
            null,
            reading.getMachine(),
            reading,
            reading.getSensorType(),
            value,
            severity.equals("CRITICAL") ? criticalThreshold : normalThreshold,
            severity,
            null,
            null,
            LocalDateTime.now()
        );

        Alert saved = alertRepository.save(alert);

        updateMachineStatus(reading.getMachine(), severity);

        alertPublisher.publish(saved);

        log.warn("Alerta {} gerado — Máquina: {} | Sensor: {} | Valor: {}",
            severity,
            reading.getMachine().getName(),
            reading.getSensorType(),
            value
        );
    }

    private void updateMachineStatus(Machine machine, String severity) {
        machine.setStatus(severity);
    }
}
