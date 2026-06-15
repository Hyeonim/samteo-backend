# Samteo Backend Deployment

## Server prerequisites

Install Docker and Docker Compose on the EC2 instance.

```bash
sudo yum update -y
sudo yum install -y docker git
sudo systemctl enable --now docker
sudo usermod -aG docker ec2-user
```

Log out and reconnect after adding `ec2-user` to the `docker` group.

## RDS setup

Create the application database and an application-only user in MariaDB.

```sql
CREATE DATABASE samteo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'samteo_app'@'%' IDENTIFIED BY 'replace-with-a-strong-password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, DROP
ON samteo.* TO 'samteo_app'@'%';

FLUSH PRIVILEGES;
```

Allow MariaDB port `3306` only from the EC2 security group.

## GitHub Secrets and Variables

The deploy workflow creates `~/samteo/.env` on EC2 from GitHub repository
Secrets and Variables. Configure them in:

```text
GitHub repository > Settings > Secrets and variables > Actions
```

Add these values under **Secrets**:

```text
EC2_HOST=<EC2 public IP or DNS>
EC2_USER=ec2-user
EC2_SSH_KEY=<private pem contents>

DB_URL=jdbc:mariadb://samteo-mariadb.cz4qceswyc1x.ap-northeast-2.rds.amazonaws.com:3306/samteo_db
DB_USERNAME=admin
DB_PASSWORD=<database password>

JWT_SECRET=<long random secret>
ODSAY_API_KEY=<ODsay API key>
GWANWANGIN_API_KEY=<Gwanwangin API key, or leave unset if unused>
KAKAO_CLIENT_ID=<Kakao REST API key>
KAKAO_CLIENT_SECRET=<Kakao client secret>
```

Add these values under **Variables**:

```text
SPRING_PROFILES_ACTIVE=prod
DDL_AUTO=update
JWT_EXPIRATION=86400000
KAKAO_REDIRECT_URI=https://www.samteo.org/login/oauth2/code/kakao
APP_FRONTEND_URL=https://www.samteo.org
APP_IMAGE=ghcr.io/hyeonim/samteo-backend:latest
SITE_DOMAIN=www.samteo.org
```

Values such as `APP_IMAGE`, `SITE_DOMAIN`, and redirect URLs are not secret,
so repository variables are enough. Database credentials, API keys, OAuth
client secrets, JWT secrets, and SSH keys must stay in repository secrets.

If any real secret has been posted in chat, committed, or shared in a screenshot,
rotate it before using it in production.

## First deploy

Create the deployment directory on EC2.

```bash
mkdir -p ~/samteo
```

After GitHub Secrets and Variables are configured, push to `main` or run the
`Deploy` workflow manually. The workflow builds the backend image, pushes it to
GHCR, copies `docker-compose.prod.yml`, `Caddyfile`, and the generated `.env`
to EC2, then restarts Docker Compose.

## GHCR denied issue

Symptom:

```text
Error response from daemon:
Head "https://ghcr.io/v2/hyeonim/samteo-backend/manifests/latest": denied: denied
```

Cause:

```text
The EC2 host had stale or invalid ghcr.io login credentials saved in Docker.
Because Docker tried the saved credentials first, pull failed even though the package itself was public.
```

How we verified it:

```bash
docker logout ghcr.io
docker pull ghcr.io/hyeonim/samteo-backend:latest
```

Resolution:

```text
1. Remove the stale GHCR login on EC2 with `docker logout ghcr.io`.
2. For public images, do not use GHCR secrets in the deploy workflow.
3. Run `docker logout ghcr.io || true` before `docker compose pull` to avoid the same issue later.
```

## DNS and security groups

Point `www.samteo.org` to the EC2 public IP. The compose stack includes Caddy, which listens on `80` and `443` and proxies traffic to the Spring Boot container.

Recommended EC2 inbound rules:

```text
22    SSH    your IP only
80    HTTP   0.0.0.0/0
443   HTTPS  0.0.0.0/0
8080  closed
```

If Cloudflare proxy is enabled, use Full or Full strict SSL mode.
