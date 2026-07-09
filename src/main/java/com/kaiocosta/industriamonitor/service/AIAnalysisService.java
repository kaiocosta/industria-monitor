package com.kaiocosta.industriamonitor.service;

import com.kaiocosta.industriamonitor.domain.Alert;
import com.kaiocosta.industriamonitor.domain.SensorReading;
import com.kaiocosta.industriamonitor.repository.AlertRepository;
import com.kaiocosta.industriamonitor.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIAnalysisService {

    private final AlertRepository alertRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final RestTemplate restTemplate;

    @Value("${anthropic.api.key}")
    private String apiKey;

    public void analyze(Long alertId) {
        Alert alert = alertRepository.findById(alertId).orElse(null);
        if (alert == null) {
            log.error("Alerta {} não encontrado.", alertId);
            return;
        }

        List<SensorReading> history = sensorReadingRepository
            .findTop10ByMachineAndSensorTypeOrderByTimestampDesc(
                alert.getMachine(),
                alert.getSensorType()
            );

        String prompt = buildPrompt(alert, history);
        String response = callClaudeApi(prompt);

        alert.setAiDiagnosis(extractSection(response, "DIAGNÓSTICO"));
        alert.setAiAction(extractSection(response, "AÇÃO"));
        alertRepository.save(alert);

        log.info("Diagnóstico de IA salvo para o alerta {}.", alertId);
    }

    private String buildPrompt(Alert alert, List<SensorReading> history) {
        StringBuilder sb = new StringBuilder();
        sb.append("Você é um especialista em manutenção industrial.\n\n");
        sb.append("ALERTA GERADO:\n");
        sb.append("- Máquina: ").append(alert.getMachine().getName()).append("\n");
        sb.append("- Tipo: ").append(alert.getMachine().getType()).append("\n");
        sb.append("- Sensor: ").append(alert.getSensorType()).append("\n");
        sb.append("- Valor registrado: ").append(alert.getTriggerValue()).append("\n");
        sb.append("- Limite: ").append(alert.getThreshold()).append("\n");
        sb.append("- Severidade: ").append(alert.getSeverity()).append("\n\n");
        sb.append("HISTÓRICO DAS ÚLTIMAS LEITURAS:\n");
        history.forEach(r ->
            sb.append("  ").append(r.getTimestamp()).append(" → ").append(r.getValue()).append("\n")
        );
        sb.append("\nResponda exatamente neste formato:\n");
        sb.append("DIAGNÓSTICO: [causa mais provável em uma frase]\n");
        sb.append("AÇÃO: [ação corretiva recomendada em uma frase]\n");
        return sb.toString();
    }

    private String callClaudeApi(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> body = Map.of(
            "model", "claude-haiku-4-5-20251001",
            "max_tokens", 256,
            "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.anthropic.com/v1/messages",
                request,
                Map.class
            );
            List<Map> content = (List<Map>) response.getBody().get("content");
            return content.get(0).get("text").toString();
        } catch (Exception e) {
            log.error("Erro ao chamar Claude API: {}", e.getMessage());
            return "DIAGNÓSTICO: Não foi possível analisar.\nAÇÃO: Verificar manualmente.";
        }
    }

    private String extractSection(String response, String section) {
        for (String line : response.split("\n")) {
            if (line.startsWith(section + ":")) {
                return line.substring(section.length() + 1).trim();
            }
        }
        return "Não disponível.";
    }
}
