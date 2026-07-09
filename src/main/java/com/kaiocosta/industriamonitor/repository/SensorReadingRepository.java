package com.kaiocosta.industriamonitor.repository;

import com.kaiocosta.industriamonitor.domain.SensorReading;
import com.kaiocosta.industriamonitor.domain.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findTop10ByMachineAndSensorTypeOrderByTimestampDesc(
        Machine machine, String sensorType
    );
}
