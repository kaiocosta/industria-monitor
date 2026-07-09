package com.kaiocosta.industriamonitor.service;

import com.kaiocosta.industriamonitor.domain.Machine;
import com.kaiocosta.industriamonitor.domain.SensorReading;
import com.kaiocosta.industriamonitor.repository.MachineRepository;
import com.kaiocosta.industriamonitor.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorSimulator {

    private final MachineRepository machineRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final AlertEngine alertEngine;
    private final Random random = new Random();

    private static final Map<String, Double[]> THRESHOLDS = Map.of(
        "TEMPERATURE", new Double[]{60.0, 80.0},
        "PRESSURE",    new Double[]{4.0,  7.0},
        "VIBRATION",   new Double[]{2.0,  4.0}
    );

    @PostConstruct
    public void initMachines() {
        if (machineRepository.count() == 0) {
            machineRepository.saveAll(List.of(
                new Machine(null, "Compressor A",  "Compressor",  "Galpão 1", "NORMAL"),
                new Machine(null, "Torno CNC",     "Torno",       "Galpão 2", "NORMAL"),
                new Machine(null, "Esteira B",     "Esteira",     "Galpão 1", "NORMAL")
            ));
            log.info("Máquinas inicializadas no banco.");
        }
    }

    @Scheduled(fixedRate = 5000)
    public void simulate() {
        List<Machine> machines = machineRepository.findAll();

        for (Machine machine : machines) {
            for (String sensorType : THRESHOLDS.keySet()) {
                Double[] limits   = THRESHOLDS.get(sensorType);
                double normalMax  = limits[0];
                double criticalMax = limits[1];

                boolean spike = random.nextInt(100) < 10;
                double value  = spike
                    ? normalMax + random.nextDouble() * (criticalMax - normalMax + 5)
                    : random.nextDouble() * normalMax;

                SensorReading reading = new SensorReading(
                    null, machine, sensorType,
                    Math.round(value * 100.0) / 100.0,
                    LocalDateTime.now()
                );

                sensorReadingRepository.save(reading);
                alertEngine.evaluate(reading, limits[0], limits[1]);
            }
        }
    }
}
