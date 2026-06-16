USE `samteo_db`;

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

DROP TEMPORARY TABLE IF EXISTS `tmp_planner_cities`;
CREATE TEMPORARY TABLE `tmp_planner_cities` (
  `city_id` VARCHAR(50) NOT NULL,
  `city_name` VARCHAR(100) NOT NULL,
  `summary` VARCHAR(500) NOT NULL,
  `latitude` DECIMAL(10,7) NOT NULL,
  `longitude` DECIMAL(10,7) NOT NULL,
  PRIMARY KEY (`city_id`)
);

INSERT INTO `tmp_planner_cities` (`city_id`, `city_name`, `summary`, `latitude`, `longitude`)
VALUES
  ('seoul', '서울', '수도권 중심 상권과 다양한 단기 일자리가 밀집한 도시입니다.', 37.5665000, 126.9780000),
  ('busan', '부산', '해변 관광과 항만 물류 일자리를 함께 볼 수 있는 도시입니다.', 35.1796000, 129.0756000),
  ('daegu', '대구', '도심 상권과 교통 거점 기반의 체류형 일자리가 있는 도시입니다.', 35.8714000, 128.6014000),
  ('jeju', '제주', '숙박·관광·렌터카 중심의 시즌 일자리가 많은 도시입니다.', 33.4996000, 126.5312000),
  ('gangneung', '강릉', '해변 카페와 숙박업 중심의 체류형 일자리가 있는 도시입니다.', 37.7519000, 128.8761000),
  ('jeonju', '전주', '한옥마을과 전통 음식 관광 일자리가 강한 도시입니다.', 35.8242000, 127.1480000),
  ('gyeongju', '경주', '역사 관광지와 숙박 서비스 일자리가 연결된 도시입니다.', 35.8562000, 129.2247000),
  ('incheon', '인천', '공항·송도·항만 기반의 서비스와 물류 일자리가 있는 도시입니다.', 37.4563000, 126.7052000),
  ('yeosu', '여수', '해양 관광과 식음료 서비스 일자리가 있는 도시입니다.', 34.7604000, 127.6622000),
  ('sokcho', '속초', '설악산과 해변 숙박권을 함께 보는 관광 도시입니다.', 38.2070000, 128.5918000),
  ('gwangju', '광주', '문화 예술과 도심 서비스 일자리가 균형 잡힌 도시입니다.', 35.1595000, 126.8526000),
  ('daejeon', '대전', '교통 중심성과 연구단지 기반 서비스 일자리가 있는 도시입니다.', 36.3504000, 127.3845000);

DROP TEMPORARY TABLE IF EXISTS `tmp_planner_districts`;
CREATE TEMPORARY TABLE `tmp_planner_districts` (
  `city_id` VARCHAR(50) NOT NULL,
  `district_order` INT NOT NULL,
  `region_id` VARCHAR(50) NOT NULL,
  `district_name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`region_id`),
  KEY `idx_tmp_planner_districts_city` (`city_id`, `district_order`)
);

INSERT INTO `tmp_planner_districts` (`city_id`, `district_order`, `region_id`, `district_name`, `description`)
VALUES
  ('seoul', 1, 'seoul-jongno', '종로구', '종로·북촌 문화권'),
  ('seoul', 2, 'seoul-gangnam', '강남구', '강남역·코엑스 상권'),
  ('seoul', 3, 'seoul-mapo', '마포구', '홍대·합정 상권'),
  ('seoul', 4, 'seoul-yongsan', '용산구', '이태원·한강 생활권'),
  ('busan', 1, 'busan-haeundae', '해운대구', '해운대 관광지구'),
  ('busan', 2, 'busan-suyeong', '수영구', '광안리 해변 상권'),
  ('busan', 3, 'busan-junggu', '중구', '남포동·자갈치 상권'),
  ('busan', 4, 'busan-busanjin', '부산진구', '서면 중심 상권'),
  ('daegu', 1, 'junggu', '중구', '동성로·대구역 중심'),
  ('daegu', 2, 'donggu', '동구', '동대구역·팔공산 생활권'),
  ('daegu', 3, 'suseong', '수성구', '수성못·주거 상권'),
  ('daegu', 4, 'dalseo', '달서구', '성서산단·생활 상권'),
  ('daegu', 5, 'bukgu', '북구', '대학가·복합 상권'),
  ('jeju', 1, 'jeju-jeju', '제주시', '공항·시내 생활권'),
  ('jeju', 2, 'jeju-aewol', '애월읍', '해안 카페 거리'),
  ('jeju', 3, 'jeju-seogwipo', '서귀포시', '중문·리조트 권역'),
  ('jeju', 4, 'jeju-seongsan', '성산읍', '성산일출봉 관광권'),
  ('gangneung', 1, 'gangneung-gyo', '교동', '강릉역 생활권'),
  ('gangneung', 2, 'gangneung-anmok', '안목동', '안목해변 카페 거리'),
  ('gangneung', 3, 'gangneung-gyeongpo', '경포동', '경포대 관광권'),
  ('gangneung', 4, 'gangneung-jumunjin', '주문진읍', '항구·해변 상권'),
  ('jeonju', 1, 'jeonju-wansan', '완산구', '한옥마을 생활권'),
  ('jeonju', 2, 'jeonju-deokjin', '덕진구', '전북대·터미널 상권'),
  ('jeonju', 3, 'jeonju-jungang', '중앙동', '객리단길 상권'),
  ('jeonju', 4, 'jeonju-hyoja', '효자동', '신시가지 생활권'),
  ('gyeongju', 1, 'gyeongju-hwangnam', '황남동', '황리단길 상권'),
  ('gyeongju', 2, 'gyeongju-bomun', '보문동', '보문관광단지'),
  ('gyeongju', 3, 'gyeongju-bulguk', '불국동', '불국사 관광권'),
  ('gyeongju', 4, 'gyeongju-dongcheon', '동천동', '시청 생활권'),
  ('incheon', 1, 'incheon-yeonsu', '연수구', '송도 국제도시'),
  ('incheon', 2, 'incheon-junggu', '중구', '공항·차이나타운 권역'),
  ('incheon', 3, 'incheon-namdong', '남동구', '남동공단 생활권'),
  ('incheon', 4, 'incheon-bupyeong', '부평구', '부평역 상권'),
  ('yeosu', 1, 'yeosu-jungang', '중앙동', '이순신광장 상권'),
  ('yeosu', 2, 'yeosu-dolsan', '돌산읍', '돌산 관광권'),
  ('yeosu', 3, 'yeosu-soho', '소호동', '소호 해변 생활권'),
  ('yeosu', 4, 'yeosu-ungcheon', '웅천동', '마리나 생활권'),
  ('sokcho', 1, 'sokcho-joyang', '조양동', '속초해변 생활권'),
  ('sokcho', 2, 'sokcho-cheongho', '청호동', '아바이마을 권역'),
  ('sokcho', 3, 'sokcho-noryang', '노학동', '설악산 진입권'),
  ('sokcho', 4, 'sokcho-geumho', '금호동', '중앙시장 상권'),
  ('gwangju', 1, 'gwangju-dong', '동구', '충장로·문화전당 권역'),
  ('gwangju', 2, 'gwangju-seo', '서구', '상무지구 상권'),
  ('gwangju', 3, 'gwangju-buk', '북구', '전남대 생활권'),
  ('gwangju', 4, 'gwangju-gwangsan', '광산구', '첨단·수완 생활권'),
  ('daejeon', 1, 'daejeon-yuseong', '유성구', '유성온천·연구단지'),
  ('daejeon', 2, 'daejeon-junggu', '중구', '은행동 중심 상권'),
  ('daejeon', 3, 'daejeon-seogu', '서구', '둔산동 행정 상권'),
  ('daejeon', 4, 'daejeon-daedeok', '대덕구', '산업단지 생활권');

DROP TEMPORARY TABLE IF EXISTS `tmp_job_templates`;
CREATE TEMPORARY TABLE `tmp_job_templates` (
  `template_no` INT NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `company` VARCHAR(100) NOT NULL,
  `category` VARCHAR(100) NOT NULL,
  `employment_type` VARCHAR(100) NOT NULL,
  `monthly_salary` INT NOT NULL,
  `working_days` VARCHAR(100) NOT NULL,
  `tag1` VARCHAR(100) NOT NULL,
  `tag2` VARCHAR(100) NOT NULL,
  `tag3` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`template_no`)
);

INSERT INTO `tmp_job_templates`
  (`template_no`, `title`, `company`, `category`, `employment_type`, `monthly_salary`, `working_days`, `tag1`, `tag2`, `tag3`)
VALUES
  (1, '카페 매장 스태프', 'Samteo Cafe', 'Food and Beverage', 'Part-time', 1650000, 'Mon-Fri 10:00-17:00', 'cafe', 'beginner', 'day-shift'),
  (2, '게스트하우스 운영 보조', 'Local Stay', 'Accommodation', 'Short contract', 1850000, 'Tue-Sat 09:00-18:00', 'stay', 'cleaning', 'guest-service'),
  (3, '관광 안내 데스크', 'Travel Lounge', 'Tourism', 'Contract', 2100000, 'Wed-Sun 09:00-18:00', 'tourism', 'language-plus', 'weekend'),
  (4, '편의점 야간 스태프', 'Samteo Retail', 'Store', 'Part-time', 1200000, '3 nights per week', 'night', 'store', 'short-term'),
  (5, '로컬 식당 홀 스태프', 'Local Table', 'Food Service', 'Part-time', 1750000, 'Thu-Mon 11:00-20:00', 'meal-support', 'service', 'local-food'),
  (6, '물류 피킹 보조', 'Urban Fulfillment', 'Logistics', 'Short contract', 1950000, 'Mon-Fri 08:00-17:00', 'logistics', 'meal-support', 'standing'),
  (7, '축제 현장 운영 스태프', 'Festival Crew', 'Event', 'Short contract', 1550000, 'Fri-Sun 12:00-21:00', 'event', 'weekend', 'team-work'),
  (8, '렌터카 접수 보조', 'Mobility Desk', 'Mobility', 'Contract', 2000000, 'Mon-Sat 09:00-18:00', 'desk', 'driver-license-plus', 'tourism'),
  (9, '기념품샵 판매 스태프', 'Local Goods', 'Shopping', 'Part-time', 1600000, 'Tue-Sun 10:00-19:00', 'sales', 'tourism', 'beginner'),
  (10, '호텔 조식 보조', 'Morning Hotel', 'Accommodation', 'Part-time', 1450000, '5 mornings per week', 'morning', 'hotel', 'meal-support');

DROP TEMPORARY TABLE IF EXISTS `tmp_seq_50`;
CREATE TEMPORARY TABLE `tmp_seq_50` (`n` INT NOT NULL PRIMARY KEY);
INSERT INTO `tmp_seq_50` (`n`)
VALUES
  (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),
  (11),(12),(13),(14),(15),(16),(17),(18),(19),(20),
  (21),(22),(23),(24),(25),(26),(27),(28),(29),(30),
  (31),(32),(33),(34),(35),(36),(37),(38),(39),(40),
  (41),(42),(43),(44),(45),(46),(47),(48),(49),(50);

DROP TEMPORARY TABLE IF EXISTS `tmp_acc_seq`;
CREATE TEMPORARY TABLE `tmp_acc_seq` (`n` INT NOT NULL PRIMARY KEY);
INSERT INTO `tmp_acc_seq` (`n`) VALUES (1), (2);

INSERT INTO `regions`
  (`id`, `name`, `summary`, `hot_place_score`, `housing_cost_score`, `center_latitude`, `center_longitude`)
SELECT
  d.`region_id`,
  CONCAT(c.`city_name`, ' ', d.`district_name`),
  CONCAT(c.`summary`, ' ', d.`description`, ' 일대를 중심으로 체류 동선을 설계할 수 있습니다.'),
  96 - ((d.`district_order` - 1) * 4),
  58 + (((d.`district_order` - 1) * 7) % 35),
  c.`latitude` + ((d.`district_order` - 1) * 0.0060),
  c.`longitude` + ((d.`district_order` - 1) * 0.0060)
FROM `tmp_planner_districts` d
JOIN `tmp_planner_cities` c ON c.`city_id` = d.`city_id`
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `summary` = VALUES(`summary`),
  `hot_place_score` = VALUES(`hot_place_score`),
  `housing_cost_score` = VALUES(`housing_cost_score`),
  `center_latitude` = VALUES(`center_latitude`),
  `center_longitude` = VALUES(`center_longitude`);

INSERT IGNORE INTO `region_tags` (`region_id`, `tag`)
SELECT d.`region_id`, c.`city_id` FROM `tmp_planner_districts` d JOIN `tmp_planner_cities` c ON c.`city_id` = d.`city_id`
UNION ALL
SELECT d.`region_id`, d.`district_name` FROM `tmp_planner_districts` d
UNION ALL
SELECT d.`region_id`, 'work-stay' FROM `tmp_planner_districts` d
UNION ALL
SELECT d.`region_id`, 'transit' FROM `tmp_planner_districts` d;

INSERT IGNORE INTO `region_recommendation_reasons` (`region_id`, `reason`)
SELECT d.`region_id`, CONCAT(d.`description`, ' 주변 일자리 접근성이 좋습니다.') FROM `tmp_planner_districts` d
UNION ALL
SELECT d.`region_id`, '숙소와 일자리의 이동 시간을 함께 비교하기 좋습니다.' FROM `tmp_planner_districts` d;

REPLACE INTO `jobs`
  (`id`, `title`, `company`, `city_id`, `city_name`, `region_id`, `district`, `address`, `category`, `employment_type`, `monthly_salary`, `working_days`, `commute_minutes`, `latitude`, `longitude`)
SELECT
  CONCAT('job-', c.`city_id`, '-', LPAD(s.`n`, 3, '0')),
  CONCAT(c.`city_name`, ' ', d.`district_name`, ' ', jt.`title`),
  CONCAT(jt.`company`, ' ', c.`city_name`, ' ', s.`n`),
  c.`city_id`,
  c.`city_name`,
  d.`region_id`,
  d.`district_name`,
  CONCAT(c.`city_name`, ' ', d.`district_name`, ' ', ELT((s.`n` % 10) + 1, '중앙로', '청년로', '시장길', '해변로', '문화로', '역전로', '공원로', '구암로', '관광로', '상생길'), ' ', 10 + s.`n`),
  jt.`category`,
  jt.`employment_type`,
  jt.`monthly_salary` + ((s.`n` % 8) * 50000),
  jt.`working_days`,
  15 + ((s.`n` * 7) % 45),
  c.`latitude` + ((s.`n` % 13) * 0.0011),
  c.`longitude` + ((s.`n` % 11) * 0.0011)
FROM `tmp_planner_cities` c
CROSS JOIN `tmp_seq_50` s
JOIN `tmp_job_templates` jt ON jt.`template_no` = ((s.`n` - 1) % 10) + 1
JOIN `tmp_planner_districts` d
  ON d.`city_id` = c.`city_id`
 AND d.`district_order` = ((s.`n` - 1) % (SELECT COUNT(*) FROM `tmp_planner_districts` cd WHERE cd.`city_id` = c.`city_id`)) + 1;

INSERT IGNORE INTO `job_tags` (`job_id`, `tag`)
SELECT CONCAT('job-', c.`city_id`, '-', LPAD(s.`n`, 3, '0')), jt.`tag1`
FROM `tmp_planner_cities` c JOIN `tmp_seq_50` s JOIN `tmp_job_templates` jt ON jt.`template_no` = ((s.`n` - 1) % 10) + 1
UNION ALL
SELECT CONCAT('job-', c.`city_id`, '-', LPAD(s.`n`, 3, '0')), jt.`tag2`
FROM `tmp_planner_cities` c JOIN `tmp_seq_50` s JOIN `tmp_job_templates` jt ON jt.`template_no` = ((s.`n` - 1) % 10) + 1
UNION ALL
SELECT CONCAT('job-', c.`city_id`, '-', LPAD(s.`n`, 3, '0')), jt.`tag3`
FROM `tmp_planner_cities` c JOIN `tmp_seq_50` s JOIN `tmp_job_templates` jt ON jt.`template_no` = ((s.`n` - 1) % 10) + 1;

REPLACE INTO `accommodations`
  (`id`, `name`, `region_id`, `district`, `address`, `monthly_price`, `deposit`, `distance_km`, `commute_minutes`, `latitude`, `longitude`)
SELECT
  CONCAT('acc-', c.`city_id`, '-', d.`region_id`, '-', a.`n`),
  CONCAT(c.`city_name`, ' ', d.`district_name`, ' 스테이 ', a.`n`),
  d.`region_id`,
  d.`district_name`,
  CONCAT(c.`city_name`, ' ', d.`district_name`, ' ', ELT(((d.`district_order` * 2 + a.`n`) % 10) + 1, '중앙로', '청년로', '시장길', '해변로', '문화로', '역전로', '공원로', '구암로', '관광로', '상생길'), ' ', 20 + d.`district_order` * 2 + a.`n`),
  380000 + (((d.`district_order` * 2 + a.`n` + CHAR_LENGTH(c.`city_id`)) % 8) * 45000),
  100000 + (((d.`district_order` * 2 + a.`n`) % 5) * 50000),
  0.80 + ((d.`district_order` * 2 + a.`n`) * 0.35),
  12 + (((d.`district_order` * 2 + a.`n`) * 5) % 35),
  c.`latitude` + ((d.`district_order` * 2 + a.`n`) * 0.0015),
  c.`longitude` + ((d.`district_order` * 2 + a.`n`) * 0.0015)
FROM `tmp_planner_cities` c
JOIN `tmp_planner_districts` d ON d.`city_id` = c.`city_id`
CROSS JOIN `tmp_acc_seq` a;

INSERT IGNORE INTO `accommodation_tags` (`accommodation_id`, `tag`)
SELECT CONCAT('acc-', c.`city_id`, '-', d.`region_id`, '-', a.`n`), 'short-term'
FROM `tmp_planner_cities` c JOIN `tmp_planner_districts` d ON d.`city_id` = c.`city_id` CROSS JOIN `tmp_acc_seq` a
UNION ALL
SELECT CONCAT('acc-', c.`city_id`, '-', d.`region_id`, '-', a.`n`), 'private-room'
FROM `tmp_planner_cities` c JOIN `tmp_planner_districts` d ON d.`city_id` = c.`city_id` CROSS JOIN `tmp_acc_seq` a
UNION ALL
SELECT CONCAT('acc-', c.`city_id`, '-', d.`region_id`, '-', a.`n`), IF(a.`n` = 1, 'budget', 'station')
FROM `tmp_planner_cities` c JOIN `tmp_planner_districts` d ON d.`city_id` = c.`city_id` CROSS JOIN `tmp_acc_seq` a;

INSERT INTO `users`
  (`email`, `name`, `provider`, `provider_id`, `password_hash`, `created_at`, `updated_at`)
VALUES
  ('demo@samteo.local', 'Samteo Demo', 'local', NULL, '$2a$10$18q9Hb7Cj6bmWPBSZJJjKe5rtM.HnwxFEYd71PbQDLXgPnUOsD0gW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `provider` = VALUES(`provider`),
  `provider_id` = VALUES(`provider_id`),
  `password_hash` = VALUES(`password_hash`),
  `updated_at` = CURRENT_TIMESTAMP;
