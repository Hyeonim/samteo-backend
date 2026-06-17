USE `samteo_db`;

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

INSERT INTO `meta_area` (`area_code`, `area_name`)
VALUES
  (1, 'Seoul'),
  (2, 'Incheon'),
  (3, 'Daejeon'),
  (4, 'Daegu'),
  (5, 'Gwangju'),
  (6, 'Busan'),
  (7, 'Ulsan'),
  (8, 'Sejong'),
  (31, 'Gyeonggi-do'),
  (32, 'Gangwon-do'),
  (33, 'Chungcheongbuk-do'),
  (34, 'Chungcheongnam-do'),
  (35, 'Gyeongsangbuk-do'),
  (36, 'Gyeongsangnam-do'),
  (37, 'Jeonbuk-do'),
  (38, 'Jeollanam-do'),
  (39, 'Jeju-do')
ON DUPLICATE KEY UPDATE
  `area_name` = VALUES(`area_name`);

INSERT INTO `meta_sigungu` (`sigungu_id`, `area_code`, `sigungu_code`, `sigungu_name`)
VALUES
  (101, 1, 23, 'Jongno-gu'),
  (102, 4, 1, 'Jung-gu'),
  (103, 4, 4, 'Dong-gu'),
  (104, 6, 16, 'Haeundae-gu'),
  (105, 31, 23, 'Seongnam-si'),
  (106, 39, 1, 'Jeju-si')
ON DUPLICATE KEY UPDATE
  `area_code` = VALUES(`area_code`),
  `sigungu_code` = VALUES(`sigungu_code`),
  `sigungu_name` = VALUES(`sigungu_name`);

INSERT INTO `meta_content_type` (`content_type_id`, `content_type_name`, `is_active`)
VALUES
  (12, 'Tourist Spot', TRUE),
  (14, 'Culture Facility', TRUE),
  (15, 'Festival Event', TRUE),
  (25, 'Travel Course', TRUE),
  (28, 'Leports', FALSE),
  (32, 'Accommodation', TRUE),
  (38, 'Shopping', TRUE),
  (39, 'Restaurant', TRUE)
ON DUPLICATE KEY UPDATE
  `content_type_name` = VALUES(`content_type_name`),
  `is_active` = VALUES(`is_active`);

INSERT INTO `tour_content`
  (`content_id`, `area_code`, `sigungu_id`, `content_type_id`, `title`, `tel`, `homepage`, `addr1`, `addr2`, `zipcode`, `mapx`, `mapy`, `first_image`, `first_image2`, `mlevel`, `cpyrht_div_cd`, `overview_summary`, `created_time`, `modified_time`)
VALUES
  (1000001, 1, 101, 12, 'Bukchon Hanok Village', '02-123-4567', 'https://example.com/bukchon', '37 Gyedong-gil, Jongno-gu, Seoul', NULL, '03059', 126.9849000000, 37.5826000000, 'https://example.com/images/bukchon-main.jpg', 'https://example.com/images/bukchon-thumb.jpg', '6', 'Type1', 'Sample tourist spot content for traditional village browsing.', '2026-06-01 09:00:00', '2026-06-01 09:00:00'),
  (1000002, 6, 104, 15, 'Busan Sea Festival', '051-555-1000', 'https://example.com/busan-festival', '264 Haeundaehaebyeon-ro, Haeundae-gu, Busan', NULL, '48094', 129.1589000000, 35.1587000000, 'https://example.com/images/busan-festival-main.jpg', 'https://example.com/images/busan-festival-thumb.jpg', '6', 'Type1', 'Sample festival content for beach events and evening shows.', '2026-06-01 09:10:00', '2026-06-01 09:10:00'),
  (1000003, 39, 106, 32, 'Jeju Ocean Stay', '064-700-2000', 'https://example.com/jeju-stay', '100 Haean-ro, Jeju-si, Jeju-do', NULL, '63100', 126.4930000000, 33.5063000000, 'https://example.com/images/jeju-stay-main.jpg', 'https://example.com/images/jeju-stay-thumb.jpg', '6', 'Type1', 'Sample accommodation content with ocean-view rooms.', '2026-06-01 09:20:00', '2026-06-01 09:20:00'),
  (1000004, 4, 102, 39, 'Daegu Gukbap Street', '053-777-3000', 'https://example.com/daegu-food', '80 Jungang-daero, Jung-gu, Daegu', NULL, '41934', 128.5934000000, 35.8696000000, 'https://example.com/images/daegu-food-main.jpg', 'https://example.com/images/daegu-food-thumb.jpg', '6', 'Type1', 'Sample restaurant content focused on local food discovery.', '2026-06-01 09:30:00', '2026-06-01 09:30:00'),
  (1000005, 31, 105, 38, 'Bundang Local Market', '031-123-4000', 'https://example.com/bundang-market', '10 Junganggongwon-ro, Bundang-gu, Seongnam-si', NULL, '13561', 127.1185000000, 37.3785000000, 'https://example.com/images/bundang-market-main.jpg', 'https://example.com/images/bundang-market-thumb.jpg', '6', 'Type1', 'Sample shopping content for local market browsing.', '2026-06-01 09:40:00', '2026-06-01 09:40:00'),
  (1000006, 4, 103, 14, 'Daegu Modern Culture Hall', '053-222-5000', 'https://example.com/daegu-culture', '21 Munhwa-ro, Dong-gu, Daegu', NULL, '41250', 128.6374000000, 35.8867000000, 'https://example.com/images/daegu-culture-main.jpg', 'https://example.com/images/daegu-culture-thumb.jpg', '6', 'Type1', 'Sample culture facility content for exhibitions and education.', '2026-06-01 09:50:00', '2026-06-01 09:50:00')
ON DUPLICATE KEY UPDATE
  `area_code` = VALUES(`area_code`),
  `sigungu_id` = VALUES(`sigungu_id`),
  `content_type_id` = VALUES(`content_type_id`),
  `title` = VALUES(`title`),
  `tel` = VALUES(`tel`),
  `homepage` = VALUES(`homepage`),
  `addr1` = VALUES(`addr1`),
  `addr2` = VALUES(`addr2`),
  `zipcode` = VALUES(`zipcode`),
  `mapx` = VALUES(`mapx`),
  `mapy` = VALUES(`mapy`),
  `first_image` = VALUES(`first_image`),
  `first_image2` = VALUES(`first_image2`),
  `mlevel` = VALUES(`mlevel`),
  `cpyrht_div_cd` = VALUES(`cpyrht_div_cd`),
  `overview_summary` = VALUES(`overview_summary`),
  `created_time` = VALUES(`created_time`),
  `modified_time` = VALUES(`modified_time`);

INSERT INTO `tour_content_overview` (`content_id`, `overview`)
VALUES
  (1000001, 'Sample overview for a traditional village sightseeing destination.'),
  (1000002, 'Sample overview for a large seasonal festival near the beach.'),
  (1000003, 'Sample overview for a stay-oriented lodging property.'),
  (1000004, 'Sample overview for a local food destination.'),
  (1000005, 'Sample overview for a regional shopping destination.'),
  (1000006, 'Sample overview for a museum-style culture facility.')
ON DUPLICATE KEY UPDATE
  `overview` = VALUES(`overview`);

INSERT INTO `tour_content_image` (`image_id`, `content_id`, `image_name`, `origin_img_url`, `small_img_url`, `sort_order`)
VALUES
  (1, 1000001, 'bukchon-main', 'https://example.com/images/bukchon-main.jpg', 'https://example.com/images/bukchon-thumb.jpg', 1),
  (2, 1000002, 'festival-night', 'https://example.com/images/busan-festival-main.jpg', 'https://example.com/images/busan-festival-thumb.jpg', 1),
  (3, 1000003, 'room-view', 'https://example.com/images/jeju-stay-main.jpg', 'https://example.com/images/jeju-stay-thumb.jpg', 1),
  (4, 1000004, 'signature-menu', 'https://example.com/images/daegu-food-main.jpg', 'https://example.com/images/daegu-food-thumb.jpg', 1),
  (5, 1000005, 'market-view', 'https://example.com/images/bundang-market-main.jpg', 'https://example.com/images/bundang-market-thumb.jpg', 1),
  (6, 1000006, 'hall-front', 'https://example.com/images/daegu-culture-main.jpg', 'https://example.com/images/daegu-culture-thumb.jpg', 1)
ON DUPLICATE KEY UPDATE
  `content_id` = VALUES(`content_id`),
  `image_name` = VALUES(`image_name`),
  `origin_img_url` = VALUES(`origin_img_url`),
  `small_img_url` = VALUES(`small_img_url`),
  `sort_order` = VALUES(`sort_order`);

INSERT INTO `detail_tourspot`
  (`content_id`, `area_code`, `heritage1`, `heritage2`, `heritage3`, `infocenter`, `opentime`, `restdate`, `parking`, `useseason`, `usetime`, `expguide`, `expagerange`, `accomcount`, `chk_pet`, `chk_creditcard`, `chk_baby_carriage`)
VALUES
  (1000001, 1, 'none', NULL, NULL, 'Jongno Tourist Center 02-123-4567', 'always open', 'open year-round', 'public parking nearby', 'all seasons', 'available anytime', 'guided alley walking program', 'all ages', NULL, 'limited', 'not available', 'available')
ON DUPLICATE KEY UPDATE
  `area_code` = VALUES(`area_code`),
  `heritage1` = VALUES(`heritage1`),
  `heritage2` = VALUES(`heritage2`),
  `heritage3` = VALUES(`heritage3`),
  `infocenter` = VALUES(`infocenter`),
  `opentime` = VALUES(`opentime`),
  `restdate` = VALUES(`restdate`),
  `parking` = VALUES(`parking`),
  `useseason` = VALUES(`useseason`),
  `usetime` = VALUES(`usetime`),
  `expguide` = VALUES(`expguide`),
  `expagerange` = VALUES(`expagerange`),
  `accomcount` = VALUES(`accomcount`),
  `chk_pet` = VALUES(`chk_pet`),
  `chk_creditcard` = VALUES(`chk_creditcard`),
  `chk_baby_carriage` = VALUES(`chk_baby_carriage`);

INSERT INTO `detail_culture_facility`
  (`content_id`, `area_code`, `accomcount`, `usefee`, `discountinfo`, `spendtime`, `parkingfee`, `infocenter`, `usetime`, `restdate`, `parking`, `scale`, `chk_pet`, `chk_creditcard`, `chk_baby_carriage`)
VALUES
  (1000006, 4, 'up to 200 visitors', '3000 KRW adult', 'student discount', 'about 1 hour', 'first 30 minutes free', '053-222-5000', '09:00-18:00', 'Monday', 'onsite parking available', 'medium', 'not available', 'available', 'available')
ON DUPLICATE KEY UPDATE
  `area_code` = VALUES(`area_code`),
  `accomcount` = VALUES(`accomcount`),
  `usefee` = VALUES(`usefee`),
  `discountinfo` = VALUES(`discountinfo`),
  `spendtime` = VALUES(`spendtime`),
  `parkingfee` = VALUES(`parkingfee`),
  `infocenter` = VALUES(`infocenter`),
  `usetime` = VALUES(`usetime`),
  `restdate` = VALUES(`restdate`),
  `parking` = VALUES(`parking`),
  `scale` = VALUES(`scale`),
  `chk_pet` = VALUES(`chk_pet`),
  `chk_creditcard` = VALUES(`chk_creditcard`),
  `chk_baby_carriage` = VALUES(`chk_baby_carriage`);

INSERT INTO `detail_festival`
  (`content_id`, `area_code`, `sponsor1`, `sponsor1tel`, `sponsor2`, `sponsor2tel`, `event_start_date`, `event_end_date`, `event_place`, `playtime`, `booking_place`, `place_info`, `sub_event`, `program`, `use_time_festival`, `discountinfofestival`, `festival_grade`)
VALUES
  (1000002, 6, 'Busan Tourism Organization', '051-555-1000', 'Haeundae-gu Office', '051-555-2000', '2026-08-01', '2026-08-05', 'Haeundae Beach Area', '18:00-22:00', 'online and onsite booking', 'main stage and activity zone', 'busking and night market', 'opening show, beach concert, activity program', 'free entry with paid extras', 'early booking discount', 'A')
ON DUPLICATE KEY UPDATE
  `area_code` = VALUES(`area_code`),
  `sponsor1` = VALUES(`sponsor1`),
  `sponsor1tel` = VALUES(`sponsor1tel`),
  `sponsor2` = VALUES(`sponsor2`),
  `sponsor2tel` = VALUES(`sponsor2tel`),
  `event_start_date` = VALUES(`event_start_date`),
  `event_end_date` = VALUES(`event_end_date`),
  `event_place` = VALUES(`event_place`),
  `playtime` = VALUES(`playtime`),
  `booking_place` = VALUES(`booking_place`),
  `place_info` = VALUES(`place_info`),
  `sub_event` = VALUES(`sub_event`),
  `program` = VALUES(`program`),
  `use_time_festival` = VALUES(`use_time_festival`),
  `discountinfofestival` = VALUES(`discountinfofestival`),
  `festival_grade` = VALUES(`festival_grade`);

INSERT INTO `detail_accommodation`
  (`content_id`, `area_code`, `accomcountlodging`, `benikia`, `checkintime`, `checkouttime`, `chkcooking`, `foodplace`, `goodstay`, `hanok`, `infocenterlodging`, `parkinglodging`, `pickup`, `roomcount`, `reservationlodging`, `reservationurl`, `roomtype`, `scalelodging`, `subfacility`, `barbecue`, `beauty`, `beverage`, `bicycle`, `campfire`, `fitness`, `karaoke`, `publicbath`, `publicpc`, `sauna`, `seminar`, `sports`, `refundregulation`)
VALUES
  (1000003, 39, 'up to 180 guests', 'N', '15:00', '11:00', 'partial room support', 'restaurant and cafe', 'Y', 'N', '064-700-2000', 'surface and basement parking', 'airport shuttle on request', '72 rooms', 'online reservation available', 'https://example.com/jeju-stay/reservation', 'double, family, ocean-view', 'large', 'pool, lounge, laundry room', 'N', 'N', 'Y', 'Y', 'N', 'Y', 'N', 'Y', 'Y', 'N', 'Y', 'marine leisure linkage', 'free cancellation up to 3 days before check-in')
ON DUPLICATE KEY UPDATE
  `area_code` = VALUES(`area_code`),
  `accomcountlodging` = VALUES(`accomcountlodging`),
  `benikia` = VALUES(`benikia`),
  `checkintime` = VALUES(`checkintime`),
  `checkouttime` = VALUES(`checkouttime`),
  `chkcooking` = VALUES(`chkcooking`),
  `foodplace` = VALUES(`foodplace`),
  `goodstay` = VALUES(`goodstay`),
  `hanok` = VALUES(`hanok`),
  `infocenterlodging` = VALUES(`infocenterlodging`),
  `parkinglodging` = VALUES(`parkinglodging`),
  `pickup` = VALUES(`pickup`),
  `roomcount` = VALUES(`roomcount`),
  `reservationlodging` = VALUES(`reservationlodging`),
  `reservationurl` = VALUES(`reservationurl`),
  `roomtype` = VALUES(`roomtype`),
  `scalelodging` = VALUES(`scalelodging`),
  `subfacility` = VALUES(`subfacility`),
  `barbecue` = VALUES(`barbecue`),
  `beauty` = VALUES(`beauty`),
  `beverage` = VALUES(`beverage`),
  `bicycle` = VALUES(`bicycle`),
  `campfire` = VALUES(`campfire`),
  `fitness` = VALUES(`fitness`),
  `karaoke` = VALUES(`karaoke`),
  `publicbath` = VALUES(`publicbath`),
  `publicpc` = VALUES(`publicpc`),
  `sauna` = VALUES(`sauna`),
  `seminar` = VALUES(`seminar`),
  `sports` = VALUES(`sports`),
  `refundregulation` = VALUES(`refundregulation`);

INSERT INTO `detail_shopping`
  (`content_id`, `area_code`, `saleitem`, `saleitemcost`, `fairday`, `opendateshopping`, `opentime`, `restdateshopping`, `shopguide`, `culturecenter`, `restroom`, `infocentershopping`, `parkingshopping`, `chk_petshopping`, `chk_creditcardshopping`, `chk_baby_carriageshopping`)
VALUES
  (1000005, 31, 'local food, crafts, lifestyle goods', 'varies by store', 'special market every Saturday', '2024-03-01', '10:00-21:00', 'closed on major holidays', 'merchant zone and popup zone layout', 'cooking class available', 'indoor restroom available', '031-123-4000', '2 hours free parking', 'available', 'available', 'available')
ON DUPLICATE KEY UPDATE
  `area_code` = VALUES(`area_code`),
  `saleitem` = VALUES(`saleitem`),
  `saleitemcost` = VALUES(`saleitemcost`),
  `fairday` = VALUES(`fairday`),
  `opendateshopping` = VALUES(`opendateshopping`),
  `opentime` = VALUES(`opentime`),
  `restdateshopping` = VALUES(`restdateshopping`),
  `shopguide` = VALUES(`shopguide`),
  `culturecenter` = VALUES(`culturecenter`),
  `restroom` = VALUES(`restroom`),
  `infocentershopping` = VALUES(`infocentershopping`),
  `parkingshopping` = VALUES(`parkingshopping`),
  `chk_petshopping` = VALUES(`chk_petshopping`),
  `chk_creditcardshopping` = VALUES(`chk_creditcardshopping`),
  `chk_baby_carriageshopping` = VALUES(`chk_baby_carriageshopping`);

INSERT INTO `detail_restaurant`
  (`content_id`, `area_code`, `firstmenu`, `treatmenu`, `infocenterfood`, `kidsfacility`, `opentimefood`, `packing`, `parkingfood`, `reservationfood`, `restdatefood`, `scalefood`, `seat`, `smoking`, `chk_creditcardfood`, `discountinfofood`, `lcnsno`)
VALUES
  (1000004, 4, 'pork gukbap', 'gukbap, suyuk, sundae, soup set', '053-777-3000', 'child chair available', '09:00-22:00', 'available', 'paid parking nearby', 'phone reservation available', 'Monday', 'medium', '80 seats', 'indoor non-smoking', 'available', 'lunch set discount', '2026-DAEGU-JG-0001')
ON DUPLICATE KEY UPDATE
  `area_code` = VALUES(`area_code`),
  `firstmenu` = VALUES(`firstmenu`),
  `treatmenu` = VALUES(`treatmenu`),
  `infocenterfood` = VALUES(`infocenterfood`),
  `kidsfacility` = VALUES(`kidsfacility`),
  `opentimefood` = VALUES(`opentimefood`),
  `packing` = VALUES(`packing`),
  `parkingfood` = VALUES(`parkingfood`),
  `reservationfood` = VALUES(`reservationfood`),
  `restdatefood` = VALUES(`restdatefood`),
  `scalefood` = VALUES(`scalefood`),
  `seat` = VALUES(`seat`),
  `smoking` = VALUES(`smoking`),
  `chk_creditcardfood` = VALUES(`chk_creditcardfood`),
  `discountinfofood` = VALUES(`discountinfofood`),
  `lcnsno` = VALUES(`lcnsno`);

INSERT INTO `tour_content_repeat_info`
  (`repeat_id`, `content_id`, `content_type_id`, `item_name`, `item_value`, `sort_order`)
VALUES
  (1, 1000002, 15, 'program_1', 'beach opening show', 1),
  (2, 1000002, 15, 'program_2', 'night drone show', 2),
  (3, 1000003, 32, 'room_option', 'ocean-view family room', 1),
  (4, 1000004, 39, 'menu_detail', 'suyuk soup set', 1)
ON DUPLICATE KEY UPDATE
  `content_id` = VALUES(`content_id`),
  `content_type_id` = VALUES(`content_type_id`),
  `item_name` = VALUES(`item_name`),
  `item_value` = VALUES(`item_value`),
  `sort_order` = VALUES(`sort_order`);

INSERT INTO `tour_api_sync_history`
  (`sync_id`, `job_name`, `area_code`, `content_type_id`, `requested_count`, `success_count`, `failed_count`, `started_at`, `ended_at`, `status`, `error_message`)
VALUES
  (1, 'tour_content_initial_load', 4, 39, 120, 118, 2, '2026-06-01 01:00:00', '2026-06-01 01:03:00', 'PARTIAL_SUCCESS', 'two detail responses missing and queued for retry'),
  (2, 'tour_content_initial_load', 39, 32, 80, 80, 0, '2026-06-01 02:00:00', '2026-06-01 02:02:00', 'SUCCESS', NULL)
ON DUPLICATE KEY UPDATE
  `job_name` = VALUES(`job_name`),
  `area_code` = VALUES(`area_code`),
  `content_type_id` = VALUES(`content_type_id`),
  `requested_count` = VALUES(`requested_count`),
  `success_count` = VALUES(`success_count`),
  `failed_count` = VALUES(`failed_count`),
  `started_at` = VALUES(`started_at`),
  `ended_at` = VALUES(`ended_at`),
  `status` = VALUES(`status`),
  `error_message` = VALUES(`error_message`);


INSERT INTO `tour_content`
  (`content_id`, `area_code`, `sigungu_id`, `content_type_id`, `title`, `tel`, `homepage`, `addr1`, `addr2`, `zipcode`, `mapx`, `mapy`, `first_image`, `first_image2`, `mlevel`, `cpyrht_div_cd`, `overview_summary`, `created_time`, `modified_time`)
VALUES
  (2000001, 4, NULL, 15, 'Daegu Chimac Festival', '053-100-0001', 'https://example.com/festivals/daegu-chimac', 'Daegu Jung-gu Dalgubeol-daero 670', NULL, '41911', 128.6014000000, 35.8714000000, NULL, NULL, '6', 'Type1', 'A local night food and music festival in Daegu.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000002, 6, NULL, 15, 'Busan Beach Movie Night', '051-100-0002', 'https://example.com/festivals/busan-movie', 'Busan Haeundae-gu Haeundaehaebyeon-ro 264', NULL, '48094', 129.1589000000, 35.1587000000, NULL, NULL, '6', 'Type1', 'Outdoor movie and busking event near Haeundae Beach.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000003, 39, NULL, 15, 'Jeju Sunrise Music Day', '064-100-0003', 'https://example.com/festivals/jeju-sunrise', 'Jeju Seogwipo-si Seongsan-ri 284-12', NULL, '63643', 126.9426000000, 33.4581000000, NULL, NULL, '6', 'Type1', 'Morning music event near Seongsan Ilchulbong.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000004, 32, NULL, 15, 'Gangneung Coffee Busking Day', '033-100-0004', 'https://example.com/festivals/gangneung-coffee', 'Gangwon Gangneung-si Changhae-ro 14', NULL, '25556', 128.9079000000, 37.7955000000, NULL, NULL, '6', 'Type1', 'Coffee street busking event in Gangneung.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000005, 1, NULL, 15, 'Seoul Han River Picnic Concert', '02-100-0005', 'https://example.com/festivals/seoul-hangang', 'Seoul Yeongdeungpo-gu Yeouidong-ro 330', NULL, '07337', 126.9349000000, 37.5283000000, NULL, NULL, '6', 'Type1', 'Picnic style concert near the Han River.', '2026-06-01 10:00:00', '2026-06-01 10:00:00')
ON DUPLICATE KEY UPDATE
  `area_code` = VALUES(`area_code`),
  `sigungu_id` = VALUES(`sigungu_id`),
  `content_type_id` = VALUES(`content_type_id`),
  `title` = VALUES(`title`),
  `tel` = VALUES(`tel`),
  `homepage` = VALUES(`homepage`),
  `addr1` = VALUES(`addr1`),
  `addr2` = VALUES(`addr2`),
  `zipcode` = VALUES(`zipcode`),
  `mapx` = VALUES(`mapx`),
  `mapy` = VALUES(`mapy`),
  `first_image` = VALUES(`first_image`),
  `first_image2` = VALUES(`first_image2`),
  `mlevel` = VALUES(`mlevel`),
  `cpyrht_div_cd` = VALUES(`cpyrht_div_cd`),
  `overview_summary` = VALUES(`overview_summary`),
  `created_time` = VALUES(`created_time`),
  `modified_time` = VALUES(`modified_time`);

INSERT INTO `detail_festival`
  (`content_id`, `area_code`, `sponsor1`, `sponsor1tel`, `sponsor2`, `sponsor2tel`, `event_start_date`, `event_end_date`, `event_place`, `playtime`, `booking_place`, `place_info`, `sub_event`, `program`, `use_time_festival`, `discountinfofestival`, `festival_grade`)
VALUES
  (2000001, 4, 'Daegu Tourism Office', '053-100-0001', 'Jung-gu Office', '053-100-1001', '2026-06-14', '2026-06-16', 'Daegu downtown park', '18:00-22:00', 'onsite', 'outdoor stage', 'food trucks', 'food market and night concert', 'free entry', 'paid tasting booths', 'A'),
  (2000002, 6, 'Busan Tourism Office', '051-100-0002', 'Haeundae-gu Office', '051-100-1002', '2026-06-20', '2026-06-22', 'Haeundae Beach', '19:00-23:00', 'online', 'beach screening zone', 'busking', 'outdoor movie and busking', 'free entry', 'seat reservation optional', 'A'),
  (2000003, 39, 'Jeju Tourism Office', '064-100-0003', 'Seogwipo Office', '064-100-1003', '2026-07-05', '2026-07-07', 'Seongsan Ilchulbong area', '06:00-09:00', 'online', 'sunrise viewing zone', 'morning walk', 'sunrise music program', 'free entry', 'none', 'A'),
  (2000004, 32, 'Gangneung City', '033-100-0004', 'Coffee Street Association', '033-100-1004', '2026-07-18', '2026-07-20', 'Anmok Coffee Street', '14:00-21:00', 'onsite', 'street performance zone', 'coffee tasting', 'busking and coffee class', 'free entry', 'paid classes', 'B'),
  (2000005, 1, 'Seoul City', '02-100-0005', 'Yeongdeungpo-gu Office', '02-100-1005', '2026-08-12', '2026-08-15', 'Han River Park', '17:00-22:00', 'online', 'picnic zone', 'food trucks', 'picnic concert and market', 'free entry', 'none', 'A')
ON DUPLICATE KEY UPDATE
  `area_code` = VALUES(`area_code`),
  `sponsor1` = VALUES(`sponsor1`),
  `sponsor1tel` = VALUES(`sponsor1tel`),
  `sponsor2` = VALUES(`sponsor2`),
  `sponsor2tel` = VALUES(`sponsor2tel`),
  `event_start_date` = VALUES(`event_start_date`),
  `event_end_date` = VALUES(`event_end_date`),
  `event_place` = VALUES(`event_place`),
  `playtime` = VALUES(`playtime`),
  `booking_place` = VALUES(`booking_place`),
  `place_info` = VALUES(`place_info`),
  `sub_event` = VALUES(`sub_event`),
  `program` = VALUES(`program`),
  `use_time_festival` = VALUES(`use_time_festival`),
  `discountinfofestival` = VALUES(`discountinfofestival`),
  `festival_grade` = VALUES(`festival_grade`);
