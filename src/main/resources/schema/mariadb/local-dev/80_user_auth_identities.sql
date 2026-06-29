CREATE TABLE IF NOT EXISTS user_auth_identities (
  identity_id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  provider VARCHAR(50) NOT NULL,
  provider_user_id VARCHAR(255) NOT NULL,
  provider_email VARCHAR(255) NULL,
  linked_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  last_login_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (identity_id),
  UNIQUE KEY uk_auth_identity_provider_user (provider, provider_user_id),
  UNIQUE KEY uk_auth_identity_user_provider (user_id, provider),
  INDEX idx_auth_identity_user_id (user_id)
);

CREATE TABLE IF NOT EXISTS oauth_pending_identities (
  pending_id BIGINT NOT NULL AUTO_INCREMENT,
  pending_token VARCHAR(100) NOT NULL,
  provider VARCHAR(50) NOT NULL,
  provider_user_id VARCHAR(255) NOT NULL,
  provider_email VARCHAR(255) NOT NULL,
  provider_name VARCHAR(255) NULL,
  expires_at DATETIME(6) NOT NULL,
  consumed_at DATETIME(6) NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (pending_id),
  UNIQUE KEY uk_oauth_pending_token (pending_token),
  INDEX idx_oauth_pending_expiry (expires_at),
  INDEX idx_oauth_pending_provider_user (provider, provider_user_id)
);

INSERT IGNORE INTO user_auth_identities
  (user_id, provider, provider_user_id, provider_email, linked_at, last_login_at)
SELECT
  user_id,
  LOWER(provider),
  CASE
    WHEN provider = 'local' OR provider_id IS NULL OR provider_id = '' THEN LOWER(email)
    ELSE provider_id
  END,
  email,
  created_at,
  updated_at
FROM users;
