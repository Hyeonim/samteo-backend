USE `samteo_db`;

INSERT IGNORE INTO `REGION`
  (`region_id`, `name`, `emoji`, `description`, `badge`, `badge_type`, `bg_gradient`)
VALUES
  (1, 'Daegu Jung-gu', 'city', 'Central district with strong transit and downtown jobs.', 'Downtown', 'blue', 'linear-gradient(135deg, #2563eb, #06b6d4)'),
  (2, 'Daegu Dong-gu', 'rail', 'Transport-friendly district near Dongdaegu Station.', 'Transit', 'green', 'linear-gradient(135deg, #16a34a, #22c55e)'),
  (3, 'Daegu Dalseo-gu', 'home', 'Cost-effective district with access to industrial areas.', 'Budget', 'green', 'linear-gradient(135deg, #f97316, #facc15)');

INSERT IGNORE INTO `JOB_TYPE` (`type_id`, `name`)
VALUES
  (1, 'Cafe'),
  (2, 'Tourism'),
  (3, 'Logistics'),
  (4, 'Retail');

INSERT IGNORE INTO `USER`
  (`user_id`, `email`, `name`, `provider`, `provider_id`, `password_hash`)
VALUES
  (1, 'planner@samteo.local', 'Planner Demo', 'local', NULL, '1234'),
  (2, 'admin@samteo.local', 'Samteo Operator', 'local', NULL, '1234')
ON DUPLICATE KEY UPDATE
  `email` = VALUES(`email`),
  `name` = VALUES(`name`),
  `provider` = VALUES(`provider`),
  `provider_id` = VALUES(`provider_id`),
  `password_hash` = VALUES(`password_hash`);

INSERT INTO `users`
  (`user_id`, `email`, `name`, `provider`, `provider_id`, `password_hash`, `created_at`, `updated_at`)
VALUES
  (1, 'planner@samteo.local', 'Planner Demo', 'local', NULL, '$2a$10$18q9Hb7Cj6bmWPBSZJJjKe5rtM.HnwxFEYd71PbQDLXgPnUOsD0gW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'admin@samteo.local', 'Samteo Operator', 'local', NULL, '$2a$10$18q9Hb7Cj6bmWPBSZJJjKe5rtM.HnwxFEYd71PbQDLXgPnUOsD0gW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
  `email` = VALUES(`email`),
  `name` = VALUES(`name`),
  `provider` = VALUES(`provider`),
  `provider_id` = VALUES(`provider_id`),
  `password_hash` = VALUES(`password_hash`),
  `updated_at` = CURRENT_TIMESTAMP;

INSERT IGNORE INTO `JOB`
  (`job_id`, `region_id`, `type_id`, `name`, `salary`, `description`, `location_desc`, `latitude`, `longitude`, `emoji`, `is_best`, `price_label`, `unit`, `sub_description`)
VALUES
  (1, 1, 1, 'Dongseong-ro Bakery Cafe Staff', 1600000, 'Cafe counter and beverage support for a downtown bakery.', 'Near Jungangno Station, about 15 minutes on foot.', 35.8698000, 128.5940000, 'coffee', TRUE, '1600000', '/month', 'Meals negotiable'),
  (2, 2, 2, 'Dongdaegu Travel Lounge Staff', 2100000, 'Tourism desk support and visitor guidance.', 'Near Dongdaegu Station, about 10 minutes on foot.', 35.8797000, 128.6289000, 'map', TRUE, '2100000', '/month', 'Weekend shifts'),
  (3, 3, 3, 'Seongseo Logistics Picking', 1850000, 'Short-term warehouse picking and packing.', 'Near Seongseo Industrial Complex.', 35.8372000, 128.4888000, 'box', FALSE, '1850000', '/month', 'Meal support');

INSERT IGNORE INTO `JOB_TAG` (`tag_id`, `job_id`, `label`, `sort_order`)
VALUES
  (1, 1, 'beginner', 1),
  (2, 1, 'downtown', 2),
  (3, 2, 'tourism', 1),
  (4, 2, 'station', 2),
  (5, 3, 'short-term', 1),
  (6, 3, 'meal-support', 2);

INSERT IGNORE INTO `ACCOMMODATION`
  (`acc_id`, `region_id`, `name`, `price_per_month`, `location_desc`, `district`, `latitude`, `longitude`, `bg_gradient`, `color_hex`, `rank`)
VALUES
  (1, 1, 'Daegu Downtown Hostel', 450000, 'Central shared stay near downtown jobs.', 'Jung-gu', 35.8714000, 128.6014000, 'linear-gradient(135deg, #2563eb, #06b6d4)', '#2563eb', 1),
  (2, 2, 'Dongdaegu Stay', 520000, 'Private room near Dongdaegu Station.', 'Dong-gu', 35.8797000, 128.6289000, 'linear-gradient(135deg, #16a34a, #22c55e)', '#16a34a', 2),
  (3, 3, 'Seongseo Budget Residence', 380000, 'Low-cost residence near industrial work sites.', 'Dalseo-gu', 35.8423000, 128.4921000, 'linear-gradient(135deg, #f97316, #facc15)', '#f97316', 3);

INSERT IGNORE INTO `ACC_TAG` (`tag_id`, `acc_id`, `label`, `tag_type`)
VALUES
  (1, 1, 'shared-kitchen', 'blue'),
  (2, 1, 'downtown', 'green'),
  (3, 2, 'private-room', 'blue'),
  (4, 2, 'station', 'green'),
  (5, 3, 'budget', 'green'),
  (6, 3, 'laundry', 'gray');

INSERT IGNORE INTO `ACC_DISPOSABLE`
  (`disp_id`, `acc_id`, `job_id`, `amount`, `display_text`)
VALUES
  (1, 1, 1, 770000, '+770000'),
  (2, 2, 2, 1200000, '+1200000'),
  (3, 3, 3, 1090000, '+1090000');

INSERT IGNORE INTO `FESTIVAL`
  (`festival_id`, `region_id`, `name`, `start_date`, `end_date`, `description`)
VALUES
  (1, 1, 'Daegu Downtown Festival', '2026-05-01', '2026-05-05', 'Downtown cultural festival sample data.'),
  (2, 2, 'Dongdaegu Travel Week', '2026-06-10', '2026-06-16', 'Station-area tourism event sample data.');

INSERT IGNORE INTO `PLAN`
  (`plan_id`, `user_id`, `region_id`, `acc_id`, `stay_month`, `total_salary`, `accommodation_cost`, `food_cost`, `transport_cost`, `disposable_income`, `status`)
VALUES
  (1, 1, 1, 1, '2026-06', 1600000, 450000, 300000, 80000, 770000, 'confirmed')
ON DUPLICATE KEY UPDATE
  `user_id` = VALUES(`user_id`),
  `region_id` = VALUES(`region_id`),
  `acc_id` = VALUES(`acc_id`),
  `stay_month` = VALUES(`stay_month`),
  `total_salary` = VALUES(`total_salary`),
  `accommodation_cost` = VALUES(`accommodation_cost`),
  `food_cost` = VALUES(`food_cost`),
  `transport_cost` = VALUES(`transport_cost`),
  `disposable_income` = VALUES(`disposable_income`),
  `status` = VALUES(`status`);

INSERT IGNORE INTO `PLAN_JOB`
  (`plan_job_id`, `plan_id`, `job_id`, `is_primary`, `sort_order`)
VALUES
  (1, 1, 1, TRUE, 1);

INSERT IGNORE INTO `PLAN_CALENDAR`
  (`calendar_id`, `plan_id`, `work_date`, `day_type`, `event_name`)
VALUES
  (1, 1, '2026-06-01', 'today', 'Plan start'),
  (2, 1, '2026-06-02', 'work', 'Cafe shift'),
  (3, 1, '2026-06-03', 'rest', NULL);
