# Technical Specification

## Project

- Name: Samteo Backend
- Language: Java 21
- Framework: Spring Boot 3.5.14
- Build Tool: Gradle
- Main Class: `com.samteo.SamteoApplication`
- Default Local Profile: `dev`
- Production Profile: `prod`

## Main Dependencies

- Spring Web
- Spring Data JPA
- Spring Security
- MariaDB JDBC Driver
- JJWT
- Lombok

## Database

- Local DB: MariaDB 11 via Docker Compose
- Local database name: `samteo_db`
- Local port: `3307`
- Local user/password: `samteo` / `samteo`
- Production DB: AWS RDS MariaDB
- Production endpoint: `samteo-mariadb.cz4qceswyc1x.ap-northeast-2.rds.amazonaws.com:3306`

## Server

- Runtime target: Docker container
- Application port: `8080`
- Production reverse proxy: Caddy
- Production domain: `www.samteo.org`
- Production EC2 public IP: `54.116.25.70`
- EC2 OS: Amazon Linux 2023

## Production Compose

Production Docker Compose is defined in:

```text
docker-compose.prod.yml
```

It runs:

- `samteo-caddy`: receives `80/443` traffic and proxies to the app.
- `samteo-backend`: Spring Boot application container.

## CI/CD

GitHub Actions workflow:

```text
.github/workflows/deploy.yml
```

Pipeline:

1. Checkout source
2. Set up JDK 21
3. Build Spring Boot jar
4. Build Docker image
5. Push image to GHCR
6. Copy compose/Caddy config to EC2
7. Pull and restart containers on EC2

## External Services

- Kakao OAuth
- ODsay public transit API
- TourAPI
- Gwanwangin API

Secrets are injected through `application-secret.properties` locally and `.env` on the server.

Do not commit real secret files.
