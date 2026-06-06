CREATE DATABASE IF NOT EXISTS `samteo_db`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `samteo_db`;

CREATE TABLE IF NOT EXISTS `USER` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `profile_image` VARCHAR(500),
  `provider` ENUM('local','kakao') NOT NULL DEFAULT 'local',
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

CREATE TABLE IF NOT EXISTS `META_AREA` (
  `area_code` INT NOT NULL,
  `area_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`area_code`),
  UNIQUE KEY `uq_meta_area_name` (`area_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `META_SIGUNGU` (
  `sigungu_id` BIGINT NOT NULL AUTO_INCREMENT,
  `area_code` INT NOT NULL,
  `sigungu_code` INT NOT NULL,
  `sigungu_name` VARCHAR(80) NOT NULL,
  PRIMARY KEY (`sigungu_id`),
  UNIQUE KEY `uq_meta_sigungu_area_code` (`area_code`, `sigungu_code`),
  KEY `idx_meta_sigungu_area_code` (`area_code`),
  CONSTRAINT `fk_meta_sigungu_area`
    FOREIGN KEY (`area_code`) REFERENCES `META_AREA` (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `META_CONTENT_TYPE` (
  `content_type_id` INT NOT NULL,
  `content_type_name` VARCHAR(100) NOT NULL,
  `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`content_type_id`),
  UNIQUE KEY `uq_meta_content_type_name` (`content_type_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `TOUR_CONTENT` (
  `content_id` BIGINT NOT NULL,
  `area_code` INT NOT NULL,
  `sigungu_id` BIGINT,
  `content_type_id` INT NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `tel` VARCHAR(100),
  `homepage` TEXT,
  `addr1` VARCHAR(255),
  `addr2` VARCHAR(255),
  `zipcode` VARCHAR(20),
  `mapx` DECIMAL(13,10),
  `mapy` DECIMAL(13,10),
  `first_image` VARCHAR(500),
  `first_image2` VARCHAR(500),
  `mlevel` VARCHAR(10),
  `cpyrht_div_cd` VARCHAR(10),
  `overview_summary` TEXT,
  `created_time` DATETIME,
  `modified_time` DATETIME,
  `collected_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`content_id`),
  KEY `idx_tour_content_area_code` (`area_code`),
  KEY `idx_tour_content_sigungu_id` (`sigungu_id`),
  KEY `idx_tour_content_type_id` (`content_type_id`),
  KEY `idx_tour_content_title` (`title`),
  CONSTRAINT `fk_tour_content_area`
    FOREIGN KEY (`area_code`) REFERENCES `META_AREA` (`area_code`),
  CONSTRAINT `fk_tour_content_sigungu`
    FOREIGN KEY (`sigungu_id`) REFERENCES `META_SIGUNGU` (`sigungu_id`),
  CONSTRAINT `fk_tour_content_type`
    FOREIGN KEY (`content_type_id`) REFERENCES `META_CONTENT_TYPE` (`content_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `TOUR_CONTENT_IMAGE` (
  `image_id` BIGINT NOT NULL AUTO_INCREMENT,
  `content_id` BIGINT NOT NULL,
  `image_name` VARCHAR(255),
  `origin_img_url` VARCHAR(500) NOT NULL,
  `small_img_url` VARCHAR(500),
  `sort_order` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`image_id`),
  UNIQUE KEY `uq_tour_content_image_content_sort` (`content_id`, `sort_order`),
  KEY `idx_tour_content_image_content_id` (`content_id`),
  CONSTRAINT `fk_tour_content_image_content`
    FOREIGN KEY (`content_id`) REFERENCES `TOUR_CONTENT` (`content_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `TOUR_CONTENT_OVERVIEW` (
  `content_id` BIGINT NOT NULL,
  `overview` MEDIUMTEXT,
  PRIMARY KEY (`content_id`),
  CONSTRAINT `fk_tour_content_overview_content`
    FOREIGN KEY (`content_id`) REFERENCES `TOUR_CONTENT` (`content_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `DETAIL_TOURSPOT` (
  `content_id` BIGINT NOT NULL,
  `area_code` INT NOT NULL,
  `heritage1` VARCHAR(50),
  `heritage2` VARCHAR(50),
  `heritage3` VARCHAR(50),
  `infocenter` TEXT,
  `opentime` TEXT,
  `restdate` TEXT,
  `parking` TEXT,
  `useseason` TEXT,
  `usetime` TEXT,
  `expguide` TEXT,
  `expagerange` VARCHAR(100),
  `accomcount` VARCHAR(100),
  `chk_pet` VARCHAR(50),
  `chk_creditcard` VARCHAR(50),
  `chk_baby_carriage` VARCHAR(50),
  PRIMARY KEY (`content_id`),
  KEY `idx_detail_tourspot_area_code` (`area_code`),
  CONSTRAINT `fk_detail_tourspot_content`
    FOREIGN KEY (`content_id`) REFERENCES `TOUR_CONTENT` (`content_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_detail_tourspot_area`
    FOREIGN KEY (`area_code`) REFERENCES `META_AREA` (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `DETAIL_CULTURE_FACILITY` (
  `content_id` BIGINT NOT NULL,
  `area_code` INT NOT NULL,
  `accomcount` TEXT,
  `usefee` TEXT,
  `discountinfo` TEXT,
  `spendtime` TEXT,
  `parkingfee` TEXT,
  `infocenter` TEXT,
  `usetime` TEXT,
  `restdate` TEXT,
  `parking` TEXT,
  `scale` VARCHAR(100),
  `chk_pet` VARCHAR(50),
  `chk_creditcard` VARCHAR(50),
  `chk_baby_carriage` VARCHAR(50),
  PRIMARY KEY (`content_id`),
  KEY `idx_detail_culture_facility_area_code` (`area_code`),
  CONSTRAINT `fk_detail_culture_facility_content`
    FOREIGN KEY (`content_id`) REFERENCES `TOUR_CONTENT` (`content_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_detail_culture_facility_area`
    FOREIGN KEY (`area_code`) REFERENCES `META_AREA` (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `DETAIL_FESTIVAL` (
  `content_id` BIGINT NOT NULL,
  `area_code` INT NOT NULL,
  `sponsor1` VARCHAR(255),
  `sponsor1tel` VARCHAR(100),
  `sponsor2` VARCHAR(255),
  `sponsor2tel` VARCHAR(100),
  `event_start_date` DATE,
  `event_end_date` DATE,
  `event_place` TEXT,
  `playtime` TEXT,
  `booking_place` TEXT,
  `place_info` TEXT,
  `sub_event` TEXT,
  `program` MEDIUMTEXT,
  `use_time_festival` TEXT,
  `discountinfofestival` TEXT,
  `festival_grade` VARCHAR(50),
  PRIMARY KEY (`content_id`),
  KEY `idx_detail_festival_area_code` (`area_code`),
  CONSTRAINT `fk_detail_festival_content`
    FOREIGN KEY (`content_id`) REFERENCES `TOUR_CONTENT` (`content_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_detail_festival_area`
    FOREIGN KEY (`area_code`) REFERENCES `META_AREA` (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `DETAIL_COURSE` (
  `content_id` BIGINT NOT NULL,
  `area_code` INT NOT NULL,
  `distance` TEXT,
  `schedule` TEXT,
  `take_time` TEXT,
  `theme` TEXT,
  PRIMARY KEY (`content_id`),
  KEY `idx_detail_course_area_code` (`area_code`),
  CONSTRAINT `fk_detail_course_content`
    FOREIGN KEY (`content_id`) REFERENCES `TOUR_CONTENT` (`content_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_detail_course_area`
    FOREIGN KEY (`area_code`) REFERENCES `META_AREA` (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `DETAIL_ACCOMMODATION` (
  `content_id` BIGINT NOT NULL,
  `area_code` INT NOT NULL,
  `accomcountlodging` TEXT,
  `benikia` TEXT,
  `checkintime` TEXT,
  `checkouttime` TEXT,
  `chkcooking` TEXT,
  `foodplace` TEXT,
  `goodstay` TEXT,
  `hanok` TEXT,
  `infocenterlodging` TEXT,
  `parkinglodging` TEXT,
  `pickup` TEXT,
  `roomcount` TEXT,
  `reservationlodging` TEXT,
  `reservationurl` TEXT,
  `roomtype` TEXT,
  `scalelodging` TEXT,
  `subfacility` TEXT,
  `barbecue` TEXT,
  `beauty` TEXT,
  `beverage` TEXT,
  `bicycle` TEXT,
  `campfire` TEXT,
  `fitness` TEXT,
  `karaoke` TEXT,
  `publicbath` TEXT,
  `publicpc` TEXT,
  `sauna` TEXT,
  `seminar` TEXT,
  `sports` TEXT,
  `refundregulation` MEDIUMTEXT,
  PRIMARY KEY (`content_id`),
  KEY `idx_detail_accommodation_area_code` (`area_code`),
  CONSTRAINT `fk_detail_accommodation_content`
    FOREIGN KEY (`content_id`) REFERENCES `TOUR_CONTENT` (`content_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_detail_accommodation_area`
    FOREIGN KEY (`area_code`) REFERENCES `META_AREA` (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `DETAIL_SHOPPING` (
  `content_id` BIGINT NOT NULL,
  `area_code` INT NOT NULL,
  `saleitem` TEXT,
  `saleitemcost` TEXT,
  `fairday` TEXT,
  `opendateshopping` TEXT,
  `opentime` TEXT,
  `restdateshopping` TEXT,
  `shopguide` MEDIUMTEXT,
  `culturecenter` TEXT,
  `restroom` TEXT,
  `infocentershopping` TEXT,
  `parkingshopping` TEXT,
  `chk_petshopping` VARCHAR(50),
  `chk_creditcardshopping` VARCHAR(50),
  `chk_baby_carriageshopping` VARCHAR(50),
  PRIMARY KEY (`content_id`),
  KEY `idx_detail_shopping_area_code` (`area_code`),
  CONSTRAINT `fk_detail_shopping_content`
    FOREIGN KEY (`content_id`) REFERENCES `TOUR_CONTENT` (`content_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_detail_shopping_area`
    FOREIGN KEY (`area_code`) REFERENCES `META_AREA` (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `DETAIL_RESTAURANT` (
  `content_id` BIGINT NOT NULL,
  `area_code` INT NOT NULL,
  `firstmenu` TEXT,
  `treatmenu` MEDIUMTEXT,
  `infocenterfood` TEXT,
  `kidsfacility` TEXT,
  `opentimefood` TEXT,
  `packing` TEXT,
  `parkingfood` TEXT,
  `reservationfood` TEXT,
  `restdatefood` TEXT,
  `scalefood` TEXT,
  `seat` TEXT,
  `smoking` TEXT,
  `chk_creditcardfood` VARCHAR(50),
  `discountinfofood` TEXT,
  `lcnsno` VARCHAR(100),
  PRIMARY KEY (`content_id`),
  KEY `idx_detail_restaurant_area_code` (`area_code`),
  CONSTRAINT `fk_detail_restaurant_content`
    FOREIGN KEY (`content_id`) REFERENCES `TOUR_CONTENT` (`content_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_detail_restaurant_area`
    FOREIGN KEY (`area_code`) REFERENCES `META_AREA` (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `TOUR_CONTENT_REPEAT_INFO` (
  `repeat_id` BIGINT NOT NULL AUTO_INCREMENT,
  `content_id` BIGINT NOT NULL,
  `content_type_id` INT NOT NULL,
  `item_name` VARCHAR(100) NOT NULL,
  `item_value` TEXT,
  `sort_order` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`repeat_id`),
  KEY `idx_tour_content_repeat_content_id` (`content_id`),
  KEY `idx_tour_content_repeat_type_id` (`content_type_id`),
  CONSTRAINT `fk_tour_content_repeat_content`
    FOREIGN KEY (`content_id`) REFERENCES `TOUR_CONTENT` (`content_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_tour_content_repeat_type`
    FOREIGN KEY (`content_type_id`) REFERENCES `META_CONTENT_TYPE` (`content_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `TOUR_API_SYNC_HISTORY` (
  `sync_id` BIGINT NOT NULL AUTO_INCREMENT,
  `job_name` VARCHAR(100) NOT NULL,
  `area_code` INT,
  `content_type_id` INT,
  `requested_count` INT NOT NULL DEFAULT 0,
  `success_count` INT NOT NULL DEFAULT 0,
  `failed_count` INT NOT NULL DEFAULT 0,
  `started_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ended_at` TIMESTAMP NULL DEFAULT NULL,
  `status` VARCHAR(30) NOT NULL,
  `error_message` MEDIUMTEXT,
  PRIMARY KEY (`sync_id`),
  KEY `idx_tour_api_sync_history_area_code` (`area_code`),
  KEY `idx_tour_api_sync_history_type_id` (`content_type_id`),
  KEY `idx_tour_api_sync_history_job_name` (`job_name`),
  CONSTRAINT `fk_tour_api_sync_history_area`
    FOREIGN KEY (`area_code`) REFERENCES `META_AREA` (`area_code`),
  CONSTRAINT `fk_tour_api_sync_history_type`
    FOREIGN KEY (`content_type_id`) REFERENCES `META_CONTENT_TYPE` (`content_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
