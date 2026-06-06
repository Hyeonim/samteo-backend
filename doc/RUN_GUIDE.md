# Run Guide

## Prerequisites

- Java 21
- Docker Desktop or Docker Engine
- Gradle Wrapper included in the repository

## Local Database

Start local MariaDB:

```bash
docker compose up -d
```

Connection:

```text
host: localhost
port: 3307
database: samteo_db
username: samteo
password: samteo
```

MariaDB init scripts:

```text
src/main/resources/schema/mariadb/00_drop.sql
src/main/resources/schema/mariadb/01_create.sql
src/main/resources/schema/mariadb/02_init.sql
```

These scripts are mounted to:

```text
/docker-entrypoint-initdb.d
```

They run automatically only when the MariaDB volume is first created.

To reset local DB data:

```bash
docker compose down -v
docker compose up -d
```

## Local Secret File

Create:

```text
src/main/resources/application-secret.properties
```

Use `src/main/resources/application-secret.txt` as the template.

For local Docker MariaDB:

```properties
dev.datasource.password=samteo
```

## Run Application

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

Bash:

```bash
./gradlew bootRun
```

Default local profile is `dev`.

Health check:

```bash
curl http://localhost:8080/api/health
```

## Local Test Accounts

Seed data creates:

```text
planner@samteo.local / 1234
admin@samteo.local   / 1234
```

The application login API reads the `users` table and expects BCrypt password hashes.

## Build

```bash
./gradlew bootJar -x test
```

Windows PowerShell:

```powershell
.\gradlew.bat bootJar -x test
```

## Production Notes

Production uses `docker-compose.prod.yml`, not the local `docker-compose.yml`.

The production server must have:

```text
~/samteo/.env
~/samteo/docker-compose.prod.yml
~/samteo/Caddyfile
```

Real secrets must stay on the server or in GitHub Secrets.
