CREATE DATABASE IF NOT EXISTS `samteo_db`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `samteo_db`;

CREATE TABLE IF NOT EXISTS `USER` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `profile_image` VARCHAR(500),
  `provider` ENUM('local','kakao','naver','google') NOT NULL DEFAULT 'local',
  `provider_id` VARCHAR(255),
  `password_hash` VARCHAR(255),
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uq_user_email` (`email`),
  UNIQUE KEY `uq_user_provider` (`provider`, `provider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `provider` VARCHAR(255) NOT NULL,
  `provider_id` VARCHAR(255),
  `password_hash` VARCHAR(255),
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uq_users_email` (`email`),
  UNIQUE KEY `uq_users_provider` (`provider`, `provider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `REGION` (
  `region_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `emoji` VARCHAR(10) NOT NULL,
  `description` VARCHAR(200),
  `badge` VARCHAR(30),
  `badge_type` VARCHAR(10),
  `bg_gradient` VARCHAR(200),
  PRIMARY KEY (`region_id`),
  UNIQUE KEY `uq_region_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `JOB_TYPE` (
  `type_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `uq_job_type_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `FESTIVAL` (
  `festival_id` INT NOT NULL AUTO_INCREMENT,
  `region_id` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `description` VARCHAR(300),
  PRIMARY KEY (`festival_id`),
  KEY `idx_festival_region_id` (`region_id`),
  CONSTRAINT `fk_festival_region`
    FOREIGN KEY (`region_id`) REFERENCES `REGION` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `JOB` (
  `job_id` BIGINT NOT NULL AUTO_INCREMENT,
  `region_id` INT NOT NULL,
  `type_id` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `salary` INT NOT NULL,
  `description` TEXT,
  `location_desc` VARCHAR(150),
  `latitude` DECIMAL(10,7),
  `longitude` DECIMAL(10,7),
  `emoji` VARCHAR(10),
  `is_best` BOOLEAN NOT NULL DEFAULT FALSE,
  `price_label` VARCHAR(20),
  `unit` VARCHAR(10) DEFAULT '/month',
  `sub_description` VARCHAR(50),
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`job_id`),
  KEY `idx_job_region_id` (`region_id`),
  KEY `idx_job_type_id` (`type_id`),
  CONSTRAINT `fk_job_region`
    FOREIGN KEY (`region_id`) REFERENCES `REGION` (`region_id`),
  CONSTRAINT `fk_job_type`
    FOREIGN KEY (`type_id`) REFERENCES `JOB_TYPE` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ACCOMMODATION` (
  `acc_id` BIGINT NOT NULL AUTO_INCREMENT,
  `region_id` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `price_per_month` INT NOT NULL,
  `location_desc` VARCHAR(150),
  `district` VARCHAR(30),
  `latitude` DECIMAL(10,7),
  `longitude` DECIMAL(10,7),
  `bg_gradient` VARCHAR(200),
  `color_hex` VARCHAR(10),
  `rank` INT,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`acc_id`),
  KEY `idx_accommodation_region_id` (`region_id`),
  CONSTRAINT `fk_accommodation_region`
    FOREIGN KEY (`region_id`) REFERENCES `REGION` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `JOB_TAG` (
  `tag_id` BIGINT NOT NULL AUTO_INCREMENT,
  `job_id` BIGINT NOT NULL,
  `label` VARCHAR(50) NOT NULL,
  `sort_order` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`tag_id`),
  KEY `idx_job_tag_job_id` (`job_id`),
  CONSTRAINT `fk_job_tag_job`
    FOREIGN KEY (`job_id`) REFERENCES `JOB` (`job_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ACC_TAG` (
  `tag_id` BIGINT NOT NULL AUTO_INCREMENT,
  `acc_id` BIGINT NOT NULL,
  `label` VARCHAR(50) NOT NULL,
  `tag_type` VARCHAR(10) NOT NULL DEFAULT 'gray',
  PRIMARY KEY (`tag_id`),
  KEY `idx_acc_tag_acc_id` (`acc_id`),
  CONSTRAINT `fk_acc_tag_acc`
    FOREIGN KEY (`acc_id`) REFERENCES `ACCOMMODATION` (`acc_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ACC_DISPOSABLE` (
  `disp_id` BIGINT NOT NULL AUTO_INCREMENT,
  `acc_id` BIGINT NOT NULL,
  `job_id` BIGINT NOT NULL,
  `amount` INT NOT NULL,
  `display_text` VARCHAR(30),
  PRIMARY KEY (`disp_id`),
  UNIQUE KEY `uq_acc_disposable_acc_job` (`acc_id`, `job_id`),
  KEY `idx_acc_disposable_job_id` (`job_id`),
  CONSTRAINT `fk_acc_disposable_acc`
    FOREIGN KEY (`acc_id`) REFERENCES `ACCOMMODATION` (`acc_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_acc_disposable_job`
    FOREIGN KEY (`job_id`) REFERENCES `JOB` (`job_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `PLAN` (
  `plan_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `region_id` INT NOT NULL,
  `acc_id` BIGINT NOT NULL,
  `stay_month` CHAR(7) NOT NULL COMMENT 'YYYY-MM',
  `total_salary` INT NOT NULL DEFAULT 0,
  `accommodation_cost` INT NOT NULL DEFAULT 0,
  `food_cost` INT NOT NULL DEFAULT 300000,
  `transport_cost` INT NOT NULL DEFAULT 80000,
  `disposable_income` INT NOT NULL DEFAULT 0,
  `status` ENUM('draft','confirmed') NOT NULL DEFAULT 'draft',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`plan_id`),
  KEY `idx_plan_user_id` (`user_id`),
  KEY `idx_plan_region_id` (`region_id`),
  KEY `idx_plan_acc_id` (`acc_id`),
  CONSTRAINT `fk_plan_user`
    FOREIGN KEY (`user_id`) REFERENCES `USER` (`user_id`),
  CONSTRAINT `fk_plan_region`
    FOREIGN KEY (`region_id`) REFERENCES `REGION` (`region_id`),
  CONSTRAINT `fk_plan_accommodation`
    FOREIGN KEY (`acc_id`) REFERENCES `ACCOMMODATION` (`acc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `PLAN_JOB` (
  `plan_job_id` BIGINT NOT NULL AUTO_INCREMENT,
  `plan_id` BIGINT NOT NULL,
  `job_id` BIGINT NOT NULL,
  `is_primary` BOOLEAN NOT NULL DEFAULT FALSE,
  `sort_order` INT NOT NULL DEFAULT 0,
  `added_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`plan_job_id`),
  UNIQUE KEY `uq_plan_job` (`plan_id`, `job_id`),
  KEY `idx_plan_job_job_id` (`job_id`),
  CONSTRAINT `fk_plan_job_plan`
    FOREIGN KEY (`plan_id`) REFERENCES `PLAN` (`plan_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_plan_job_job`
    FOREIGN KEY (`job_id`) REFERENCES `JOB` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `PLAN_CALENDAR` (
  `calendar_id` BIGINT NOT NULL AUTO_INCREMENT,
  `plan_id` BIGINT NOT NULL,
  `work_date` DATE NOT NULL,
  `day_type` ENUM('work','rest','festival','today') NOT NULL DEFAULT 'work',
  `event_name` VARCHAR(100),
  PRIMARY KEY (`calendar_id`),
  UNIQUE KEY `uq_plan_calendar_plan_date` (`plan_id`, `work_date`),
  CONSTRAINT `fk_plan_calendar_plan`
    FOREIGN KEY (`plan_id`) REFERENCES `PLAN` (`plan_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
