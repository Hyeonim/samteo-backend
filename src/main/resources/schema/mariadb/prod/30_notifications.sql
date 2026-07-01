CREATE TABLE IF NOT EXISTS notifications (
  notification_id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  actor_user_id BIGINT NULL,
  type VARCHAR(40) NOT NULL,
  title VARCHAR(120) NOT NULL,
  message VARCHAR(500) NOT NULL,
  target_type VARCHAR(40) NULL,
  target_id VARCHAR(100) NULL,
  dedupe_key VARCHAR(190) NULL,
  read_at DATETIME(6) NULL,
  created_at DATETIME(6) NOT NULL,
  expires_at DATETIME(6) NOT NULL,
  PRIMARY KEY (notification_id),
  UNIQUE KEY uk_notifications_dedupe_key (dedupe_key),
  INDEX idx_notifications_user_created (user_id, created_at),
  INDEX idx_notifications_user_unread (user_id, read_at, expires_at),
  INDEX idx_notifications_expires_at (expires_at)
);
