ALTER TABLE jobs
    ADD COLUMN IF NOT EXISTS bookmark_count INT NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS review_count   INT NOT NULL DEFAULT 0;

CREATE TABLE IF NOT EXISTS job_reviews (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id      VARCHAR(50)  NOT NULL,
    user_id     BIGINT       NOT NULL,
    rating      TINYINT      NOT NULL DEFAULT 5,
    content     TEXT,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_job_review_user (job_id, user_id),
    KEY idx_job_reviews_job_id  (job_id),
    KEY idx_job_reviews_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
