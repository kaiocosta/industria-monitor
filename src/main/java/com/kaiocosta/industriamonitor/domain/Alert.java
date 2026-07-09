package com.kaiocosta.industriamonitor.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @ManyToOne
    @JoinColumn(name = "sensor_reading_id")
    private SensorReading sensorReading;

    private String sensorType;
    private Double triggerValue;
    private Double threshold;
    private String severity;
    private String aiDiagnosis;
    private String aiAction;
    private LocalDateTime createdAt;
}
