USE `samteo_db`;

CREATE TABLE IF NOT EXISTS `planner_default_event_types` (
  `value` VARCHAR(100) NOT NULL,
  `label` VARCHAR(20) NOT NULL,
  `color` VARCHAR(20) NOT NULL,
  `sort_order` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`value`),
  UNIQUE KEY `uq_planner_default_event_types_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `planner_default_event_types` (`value`, `label`, `color`, `sort_order`)
VALUES
  ('personal', '개인', '#6b9ee8', 0),
  ('exercise', '운동', '#9ac768', 1),
  ('meal', '식사', '#f59e73', 2),
  ('interview', '면접', '#8b5cf6', 3),
  ('study', '공부', '#f3bf58', 4),
  ('rest', '휴식', '#6ec7bd', 5),
  ('appointment', '약속', '#475569', 6),
  ('work', '근무', '#ef7f72', 7)
ON DUPLICATE KEY UPDATE
  `label` = VALUES(`label`),
  `color` = VALUES(`color`),
  `sort_order` = VALUES(`sort_order`);
