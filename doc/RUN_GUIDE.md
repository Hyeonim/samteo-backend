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
src/main/resources/schema/mariadb/prod/00_prod_baseline_dump.sql
src/main/resources/schema/mariadb/local-dev/10_local_dev_schema_delta.sql
src/main/resources/schema/mariadb/local-dev/20_local_dev_seed_delta.sql
src/main/resources/schema/mariadb/local-dev/30_local_dev_tour_api_compat_views.sql
```

These scripts are mounted to:

```text
/docker-entrypoint-initdb.d
```

They run automatically only when the MariaDB volume is first created.
The local database is initialized from the production dump first, then local-only development deltas and compatibility migrations are applied.

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
