package com.kaiocosta.industriamonitor.controller;

import com.kaiocosta.industriamonitor.domain.Alert;
import com.kaiocosta.industriamonitor.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlertController {

    private final AlertRepository alertRepository;

    @GetMapping
    public List<Alert> listAll() {
        return alertRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> findById(@PathVariable Long id) {
        return alertRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
