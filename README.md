# IndustrIA Monitor

Sistema de monitoramento de equipamentos industriais em tempo real com diagnóstico automático por Inteligência Artificial.

## Sobre o projeto

Trabalhar com automação industrial na prática me fez perceber o quanto a detecção de falhas em equipamentos ainda é manual e reativa. O IndustrIA Monitor nasceu dessa observação — um sistema que monitora sensores continuamente, identifica anomalias e usa IA para sugerir o que está errado e o que fazer, antes que vire um problema maior.

O projeto combina uma stack backend robusta com integração a LLMs, refletindo o que acredito ser o caminho natural do desenvolvimento de software nos próximos anos.

## Stack

- **Backend:** Java 21, Spring Boot 3, DDD, SOLID
- **Frontend:** React, TypeScript, Recharts
- **Banco de dados:** PostgreSQL 16
- **Cache:** Redis 7
- **Mensageria:** RabbitMQ 3
- **IA:** Claude API (Anthropic)
- **Infra:** Docker, Docker Compose, Railway

## Como rodar localmente

Pré-requisitos: Java 21 e Docker instalados.

```bash
# Clone o repositório
git clone https://github.com/kaiocbcosta/industria-monitor.git
cd industria-monitor

# Sobe a infraestrutura
docker-compose up -d

# Roda a aplicação
./mvnw spring-boot:run
```

API disponível em `http://localhost:8080`
RabbitMQ Dashboard em `http://localhost:15672`

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/machines` | Lista todas as máquinas |
| GET | `/machines/{id}/readings` | Leituras de sensores |
| GET | `/alerts` | Alertas gerados |
| GET | `/alerts/{id}/diagnosis` | Diagnóstico de IA |

## Autor

Kaio Costa — [LinkedIn](https://linkedin.com/in/kaiocbcosta)
