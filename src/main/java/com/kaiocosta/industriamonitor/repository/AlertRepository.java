package com.kaiocosta.industriamonitor.repository;

import com.kaiocosta.industriamonitor.domain.Alert;
import com.kaiocosta.industriamonitor.domain.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByMachineOrderByCreatedAtDesc(Machine machine);
}
