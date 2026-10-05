# notifications-service

Microserviço Spring Boot 3 (Java 17, Maven) da demo `order-events`: consome eventos do Kafka, grava notificações no MongoDB e as transmite por WebSocket (STOMP/SockJS) em `/topic/notifications`; serve a página de demo. Porta **8082**.

## Ambiente

- **Runtime**: JDK 17 (`java.version` no `pom.xml`); a imagem Docker compila com Maven 3.9 + Temurin **21**. `source "$PROJECTS_ROOT/workspace/scripts/env.sh"` para o JDK na sessão.
- **Variáveis** (`.env`, ignorado; chaves esperadas): `SPRING_DATA_MONGODB_URI`, `SPRING_KAFKA_BOOTSTRAP_SERVERS`, `SSL_STORE_PASSWORD`, `KAFKA_CA_PEM`, `KAFKA_CERT_PEM`, `KAFKA_KEY_PEM`, `ORDERS_SERVICE_URL`, `NOTIFICATIONS_WS_URL`.
- **Comandos**: `mvn spring-boot:run` · `mvn test` · `docker build -f Dockerfile.dev -t notifications-service .` (o compose usa o `Dockerfile.dev`).
- O `docker-compose.yml` dos 3 serviços fica em `../` (fora do git).

## Regras

- Sem caminhos absolutos de máquina em arquivos versionados; regras comuns em `$PROJECTS_ROOT/CLAUDE.md`.
- Segredos e `.env` **não vêm no `git clone`** — ver `workspace/docs/segredos-e-arquivos-fora-do-git.md`.
- Commit/push só quando o usuário pedir.
