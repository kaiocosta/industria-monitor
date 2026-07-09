package com.kaiocosta.industriamonitor.controller;

import com.kaiocosta.industriamonitor.domain.Machine;
import com.kaiocosta.industriamonitor.domain.SensorReading;
import com.kaiocosta.industriamonitor.repository.MachineRepository;
import com.kaiocosta.industriamonitor.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/machines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MachineController {

    private final MachineRepository machineRepository;
    private final SensorReadingRepository sensorReadingRepository;

    @GetMapping
    public List<Machine> listAll() {
        return machineRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Machine> findById(@PathVariable Long id) {
        return machineRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/readings")
    public ResponseEntity<List<SensorReading>> readings(
            @PathVariable Long id,
            @RequestParam(defaultValue = "TEMPERATURE") String sensorType) {

        return machineRepository.findById(id)
            .map(machine -> ResponseEntity.ok(
                sensorReadingRepository.findTop10ByMachineAndSensorTypeOrderByTimestampDesc(
                    machine, sensorType)
            ))
            .orElse(ResponseEntity.notFound().build());
    }
}
