USE `samteo_db`;

-- Compatibility views for the current service code.
-- If production later adds real tables with these names, this migration skips them.

SET @create_tour_content_view = IF(
  (
    SELECT COUNT(*)
    FROM information_schema.tables
    WHERE table_schema = DATABASE()
      AND table_name = 'tour_content'
  ) = 0
  AND (
    SELECT COUNT(*)
    FROM information_schema.tables
    WHERE table_schema = DATABASE()
      AND table_name = 'tour_info_staging_testing'
  ) > 0,
  'CREATE VIEW `tour_content` AS
    SELECT
      `content_id`,
      `content_type_id`,
      `area_code`,
      `title`,
      `addr1`
    FROM `tour_info_staging_testing`',
  'SELECT ''skip tour_content compatibility view'' AS message'
);

PREPARE stmt FROM @create_tour_content_view;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @create_detail_festival_view = IF(
  (
    SELECT COUNT(*)
    FROM information_schema.tables
    WHERE table_schema = DATABASE()
      AND table_name = 'detail_festival'
  ) = 0
  AND (
    SELECT COUNT(*)
    FROM information_schema.tables
    WHERE table_schema = DATABASE()
      AND table_name = 'detail_festival_info_staging'
  ) > 0,
  'CREATE VIEW `detail_festival` AS
    SELECT
      `content_id`,
      `area_code`,
      `eventstartdate` AS `event_start_date`,
      `eventenddate` AS `event_end_date`
    FROM `detail_festival_info_staging`',
  'SELECT ''skip detail_festival compatibility view'' AS message'
);

PREPARE stmt FROM @create_detail_festival_view;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
