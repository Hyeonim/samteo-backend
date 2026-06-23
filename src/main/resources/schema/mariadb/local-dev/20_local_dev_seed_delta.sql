USE `samteo_db`;

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Stable region metadata used to translate UI selections into external API region codes.
INSERT INTO `meta_region` (`region_id`, `region_nm`)
VALUES
  (1, '서울'), (2, '인천'), (3, '대전'), (4, '대구'), (5, '광주'),
  (6, '부산'), (7, '울산'), (8, '세종특별자치시'), (31, '경기도'),
  (32, '강원특별자치도'), (33, '충청북도'), (34, '충청남도'),
  (35, '경상북도'), (36, '경상남도'), (37, '전북특별자치도'),
  (38, '전라남도'), (39, '제주특별자치도')
ON DUPLICATE KEY UPDATE `region_nm` = VALUES(`region_nm`);

-- Local-only login account. External content is intentionally not stored in MariaDB.
INSERT INTO `users`
  (`email`, `name`, `provider`, `provider_id`, `password_hash`, `created_at`, `updated_at`)
VALUES
  ('demo@samteo.local', 'Samteo Demo', 'local', NULL,
   '$2a$10$18q9Hb7Cj6bmWPBSZJJjKe5rtM.HnwxFEYd71PbQDLXgPnUOsD0gW',
   CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`), `password_hash` = VALUES(`password_hash`),
  `updated_at` = CURRENT_TIMESTAMP;
