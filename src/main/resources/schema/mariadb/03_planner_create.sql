USE `samteo_db`;

CREATE TABLE IF NOT EXISTS `regions` (
  `id` VARCHAR(50) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `summary` VARCHAR(500) NOT NULL,
  `hot_place_score` INT NOT NULL DEFAULT 0,
  `housing_cost_score` INT NOT NULL DEFAULT 0,
  `center_latitude` DECIMAL(10,7),
  `center_longitude` DECIMAL(10,7),
  PRIMARY KEY (`id`),
  KEY `idx_regions_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `region_tags` (
  `region_id` VARCHAR(50) NOT NULL,
  `tag` VARCHAR(255) NOT NULL,
  UNIQUE KEY `uq_region_tags_region_tag` (`region_id`, `tag`),
  KEY `idx_region_tags_region_id` (`region_id`),
  CONSTRAINT `fk_region_tags_region`
    FOREIGN KEY (`region_id`) REFERENCES `regions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `region_recommendation_reasons` (
  `region_id` VARCHAR(50) NOT NULL,
  `reason` VARCHAR(500) NOT NULL,
  UNIQUE KEY `uq_region_reasons_region_reason` (`region_id`, `reason`),
  KEY `idx_region_reasons_region_id` (`region_id`),
  CONSTRAINT `fk_region_reasons_region`
    FOREIGN KEY (`region_id`) REFERENCES `regions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `jobs` (
  `id` VARCHAR(50) NOT NULL,
  `title` VARCHAR(150) NOT NULL,
  `company` VARCHAR(150) NOT NULL,
  `city_id` VARCHAR(50),
  `city_name` VARCHAR(100),
  `region_id` VARCHAR(50) NOT NULL,
  `district` VARCHAR(100) NOT NULL,
  `address` VARCHAR(255),
  `category` VARCHAR(100) NOT NULL,
  `employment_type` VARCHAR(100) NOT NULL,
  `monthly_salary` INT NOT NULL DEFAULT 0,
  `working_days` VARCHAR(100) NOT NULL,
  `commute_minutes` INT NOT NULL DEFAULT 0,
  `latitude` DECIMAL(10,7),
  `longitude` DECIMAL(10,7),
  PRIMARY KEY (`id`),
  KEY `idx_jobs_city_id` (`city_id`),
  KEY `idx_jobs_region_id` (`region_id`),
  KEY `idx_jobs_category` (`category`),
  CONSTRAINT `fk_jobs_region`
    FOREIGN KEY (`region_id`) REFERENCES `regions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE `jobs`
  ADD COLUMN IF NOT EXISTS `city_id` VARCHAR(50) NULL AFTER `company`,
  ADD COLUMN IF NOT EXISTS `city_name` VARCHAR(100) NULL AFTER `city_id`,
  ADD COLUMN IF NOT EXISTS `address` VARCHAR(255) NULL AFTER `district`;

CREATE TABLE IF NOT EXISTS `job_tags` (
  `job_id` VARCHAR(50) NOT NULL,
  `tag` VARCHAR(255) NOT NULL,
  UNIQUE KEY `uq_job_tags_job_tag` (`job_id`, `tag`),
  KEY `idx_job_tags_job_id` (`job_id`),
  CONSTRAINT `fk_job_tags_job`
    FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `accommodations` (
  `id` VARCHAR(50) NOT NULL,
  `name` VARCHAR(150) NOT NULL,
  `region_id` VARCHAR(50) NOT NULL,
  `district` VARCHAR(100) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `monthly_price` INT NOT NULL DEFAULT 0,
  `deposit` INT NOT NULL DEFAULT 0,
  `distance_km` DECIMAL(6,2),
  `commute_minutes` INT NOT NULL DEFAULT 0,
  `latitude` DECIMAL(10,7),
  `longitude` DECIMAL(10,7),
  PRIMARY KEY (`id`),
  KEY `idx_accommodations_region_id` (`region_id`),
  CONSTRAINT `fk_accommodations_region`
    FOREIGN KEY (`region_id`) REFERENCES `regions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `accommodation_tags` (
  `accommodation_id` VARCHAR(50) NOT NULL,
  `tag` VARCHAR(255) NOT NULL,
  UNIQUE KEY `uq_accommodation_tags_acc_tag` (`accommodation_id`, `tag`),
  KEY `idx_accommodation_tags_acc_id` (`accommodation_id`),
  CONSTRAINT `fk_accommodation_tags_accommodation`
    FOREIGN KEY (`accommodation_id`) REFERENCES `accommodations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
