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

## First deploy

Create the deployment directory and environment file on EC2.

```bash
mkdir -p ~/samteo
cd ~/samteo
vi .env
```

Use `.env.example` as the template. Do not commit real secrets.

Required GitHub repository secrets:

```text
EC2_HOST=54.116.25.70
EC2_USER=ec2-user
EC2_SSH_KEY=<private pem contents>
```

If the GHCR package is private, also add a GitHub PAT with `read:packages`.

```text
GHCR_USERNAME=<github username>
GHCR_TOKEN=<github token with read:packages>
```

After `.env` exists on the server, push to `main` or run the `Deploy` workflow manually.

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
