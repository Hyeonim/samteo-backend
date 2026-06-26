CREATE TABLE IF NOT EXISTS community_posts (
  post_id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  content TEXT NULL,
  like_count INT NOT NULL DEFAULT 0,
  comment_count INT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  deleted_at DATETIME(6) NULL,
  PRIMARY KEY (post_id),
  INDEX idx_community_posts_created_at (created_at),
  INDEX idx_community_posts_user_id (user_id),
  CONSTRAINT fk_community_posts_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS community_post_images (
  image_id BIGINT NOT NULL AUTO_INCREMENT,
  post_id BIGINT NOT NULL,
  image_url VARCHAR(500) NOT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (image_id),
  INDEX idx_community_post_images_post_id (post_id),
  CONSTRAINT fk_community_post_images_post FOREIGN KEY (post_id) REFERENCES community_posts (post_id)
);

CREATE TABLE IF NOT EXISTS community_comments (
  comment_id BIGINT NOT NULL AUTO_INCREMENT,
  post_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  created_at DATETIME(6) NOT NULL,
  deleted_at DATETIME(6) NULL,
  PRIMARY KEY (comment_id),
  INDEX idx_community_comments_post_id (post_id),
  INDEX idx_community_comments_user_id (user_id),
  CONSTRAINT fk_community_comments_post FOREIGN KEY (post_id) REFERENCES community_posts (post_id),
  CONSTRAINT fk_community_comments_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS community_likes (
  like_id BIGINT NOT NULL AUTO_INCREMENT,
  post_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (like_id),
  UNIQUE KEY uk_community_likes_post_user (post_id, user_id),
  INDEX idx_community_likes_user_id (user_id),
  CONSTRAINT fk_community_likes_post FOREIGN KEY (post_id) REFERENCES community_posts (post_id),
  CONSTRAINT fk_community_likes_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS user_follows (
  follow_id BIGINT NOT NULL AUTO_INCREMENT,
  follower_id BIGINT NOT NULL,
  following_id BIGINT NOT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (follow_id),
  UNIQUE KEY uk_user_follows_pair (follower_id, following_id),
  INDEX idx_user_follows_follower_id (follower_id),
  INDEX idx_user_follows_following_id (following_id),
  CONSTRAINT fk_user_follows_follower FOREIGN KEY (follower_id) REFERENCES users (user_id),
  CONSTRAINT fk_user_follows_following FOREIGN KEY (following_id) REFERENCES users (user_id)
);
