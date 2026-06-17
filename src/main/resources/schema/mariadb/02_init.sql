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
  (2000001, 4, NULL, 15, '?援?移섎㎘ ?쇳겕???섏뒪?', '053-100-0001', 'https://example.com/festivals/daegu-chimac', '?援ш킅??떆 以묎뎄 援?콈蹂댁긽濡?670', NULL, '41911', 128.6014000000, 35.8714000000, NULL, NULL, '6', 'Type1', '?援??꾩떖 怨듭썝?먯꽌 ?대━???쇨컙 癒밴굅由?異뺤젣?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000002, 4, NULL, 15, '?援?洹쇰?怨⑤ぉ ?쇱떆??, '053-100-0002', 'https://example.com/festivals/daegu-modern', '?援ш킅??떆 以묎뎄 ?쒖꽦濡?10', NULL, '41918', 128.5909000000, 35.8707000000, NULL, NULL, '6', 'Type1', '洹쇰?怨⑤ぉ ?쇰??먯꽌 吏꾪뻾?섎뒗 濡쒖뺄 ?쇱떆???됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000003, 4, NULL, 15, '?援??숈꽦濡?踰꾩뒪???꾪겕', '053-100-0003', 'https://example.com/festivals/daegu-busking', '?援ш킅??떆 以묎뎄 ?숈꽦濡?22', NULL, '41942', 128.5965000000, 35.8683000000, NULL, NULL, '6', 'Type1', '?숈꽦濡쒖뿉???대━??泥?뀈 ?뚯븙 怨듭뿰 異뺤젣?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000004, 4, NULL, 15, '?援??섏꽦紐??쇱씠????, '053-100-0004', 'https://example.com/festivals/daegu-suseong', '?援ш킅??떆 ?섏꽦援??⑺븰濡?35', NULL, '42217', 128.6176000000, 35.8269000000, NULL, NULL, '6', 'Type1', '?섏꽦紐??곗콉濡쒖뿉???대━??議곕챸怨??뚯븙 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000005, 4, NULL, 15, '?援??붽났???뀁냽 留덉폆', '053-100-0005', 'https://example.com/festivals/daegu-palgong', '?援ш킅??떆 ?숆뎄 ?붽났?곕줈 199', NULL, '41007', 128.6908000000, 35.9901000000, NULL, NULL, '6', 'Type1', '?붽났?곌텒 濡쒖뺄 怨듭삁? 癒밴굅由?留덉폆?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000006, 6, NULL, 15, '遺???댁슫? 諛붾떎 ?곹솕??, '051-100-0006', 'https://example.com/festivals/busan-movie', '遺?곌킅??떆 ?댁슫?援??댁슫??대?濡?264', NULL, '48094', 129.1589000000, 35.1587000000, NULL, NULL, '6', 'Type1', '?댁슫? ?대??먯꽌 利먭린???쇱쇅 ?곹솕 異뺤젣?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000007, 6, NULL, 15, '遺??愿묒븞由??쒕줎 ?섏씠??, '051-100-0007', 'https://example.com/festivals/busan-drone', '遺?곌킅??떆 ?섏쁺援?愿묒븞?대?濡?219', NULL, '48303', 129.1185000000, 35.1532000000, NULL, NULL, '6', 'Type1', '愿묒븞由??쇨꼍怨??④퍡?섎뒗 ?쒕줎 怨듭뿰 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000008, 6, NULL, 15, '遺??濡쒖뺄?몃뱶 ?ㅽ듃由ы듃', '051-100-0008', 'https://example.com/festivals/busan-food', '遺?곌킅??떆 以묎뎄 愿묐났濡?58', NULL, '48953', 129.0318000000, 35.1009000000, NULL, NULL, '6', 'Type1', '遺???먮룄??癒밴굅由ъ? 怨듭뿰???④퍡 利먭린??異뺤젣?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000009, 39, NULL, 15, '?쒖＜ 諛붾떎罹좏봽 ?섏뒪?곕쾶', '064-100-0009', 'https://example.com/festivals/jeju-camp', '?쒖＜?밸퀎?먯튂???쒖＜???좎썡???좎썡?댁븞濡?620', NULL, '63047', 126.3096000000, 33.4626000000, NULL, NULL, '6', 'Type1', '?댁븞 罹좏븨怨?濡쒖뺄 怨듭뿰??寃고빀???쒖＜ 異뺤젣?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000010, 39, NULL, 15, '?쒖＜ 媛먭랠 ?뚮━留덉폆', '064-100-0010', 'https://example.com/festivals/jeju-market', '?쒖＜?밸퀎?먯튂???쒖＜??愿?뺣줈 14', NULL, '63166', 126.5219000000, 33.5130000000, NULL, NULL, '6', 'Type1', '媛먭랠 ?붿??몄? 濡쒖뺄 釉뚮옖?쒕? 留뚮굹???뚮━留덉폆?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000011, 39, NULL, 15, '?쒖＜ ?깆궛 ?쇱텧 ?뚯븙??, '064-100-0011', 'https://example.com/festivals/jeju-sunrise', '?쒖＜?밸퀎?먯튂???쒓??ъ떆 ?깆궛???쇱텧濡?284-12', NULL, '63643', 126.9426000000, 33.4581000000, NULL, NULL, '6', 'Type1', '?깆궛 ?쇱텧沅뚯뿉???대━???꾩묠 ?뚯븙 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000012, 32, NULL, 15, '媛뺣쫱 而ㅽ뵾 踰꾩뒪???곗씠', '033-100-0012', 'https://example.com/festivals/gangneung-coffee', '媛뺤썝?밸퀎?먯튂??媛뺣쫱??李쏀빐濡?14', NULL, '25556', 128.9079000000, 37.7955000000, NULL, NULL, '6', 'Type1', '?덈ぉ?대? 而ㅽ뵾嫄곕━?먯꽌 吏꾪뻾?섎뒗 踰꾩뒪???됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000013, 32, NULL, 15, '媛뺣쫱 寃쏀룷 ?몄닔湲?異뺤젣', '033-100-0013', 'https://example.com/festivals/gangneung-lake', '媛뺤썝?밸퀎?먯튂??媛뺣쫱??寃쏀룷濡?365', NULL, '25461', 128.8961000000, 37.8034000000, NULL, NULL, '6', 'Type1', '寃쏀룷??二쇰? ?곗콉怨?怨듭뿰???④퍡 利먭린??異뺤젣?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000014, 32, NULL, 15, '媛뺣쫱 ?뷀뼢 ?쇨컙 留덉폆', '033-100-0014', 'https://example.com/festivals/gangneung-night', '媛뺤썝?밸퀎?먯튂??媛뺣쫱???꾩쁺濡?131', NULL, '25528', 128.8958000000, 37.7519000000, NULL, NULL, '6', 'Type1', '媛뺣쫱 ?먮룄?ъ뿉???대━???쇨컙 濡쒖뺄 留덉폆?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000015, 37, NULL, 15, '?꾩＜ ?쒖삦留덉쓣 ?щ튆 ?곗콉', '063-100-0015', 'https://example.com/festivals/jeonju-moon', '?꾨턿?밸퀎?먯튂???꾩＜???꾩궛援?湲곕┛?濡?99', NULL, '55041', 127.1530000000, 35.8151000000, NULL, NULL, '6', 'Type1', '?쒖삦留덉쓣 ?쇨컙 ?댁꽕怨?怨듭뿰???덈뒗 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000016, 37, NULL, 15, '?꾩＜ 鍮꾨퉼 濡쒖뺄 ?섏뼱', '063-100-0016', 'https://example.com/festivals/jeonju-food', '?꾨턿?밸퀎?먯튂???꾩＜???꾩궛援??붾떖濡?144', NULL, '55043', 127.1437000000, 35.8176000000, NULL, NULL, '6', 'Type1', '?꾩＜ ?앸Ц?붿? 泥?뀈 ?고봽媛 留뚮굹???뚯떇 異뺤젣?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000017, 37, NULL, 15, '?꾩＜ ?꾪넻二??뚯씠?ㅽ똿 ?꾪겕', '063-100-0017', 'https://example.com/festivals/jeonju-drink', '?꾨턿?밸퀎?먯튂???꾩＜???꾩궛援??띾궓臾?湲?1', NULL, '55043', 127.1489000000, 35.8136000000, NULL, NULL, '6', 'Type1', '?꾪넻二??쒖쓬怨?濡쒖뺄 ?몃뱶瑜??④퍡 ?뚭컻?섎뒗 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000018, 35, NULL, 15, '寃쎌＜ 蹂대Ц ?몃컲 ?섏뒪?곕쾶', '054-100-0018', 'https://example.com/festivals/gyeongju-bomun', '寃쎌긽遺곷룄 寃쎌＜??蹂대Ц濡?424-33', NULL, '38117', 129.2874000000, 35.8429000000, NULL, NULL, '6', 'Type1', '蹂대Ц??二쇰??먯꽌 ?대━??怨듭뿰怨?泥댄뿕 異뺤젣?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000019, 35, NULL, 15, '寃쎌＜ ?⑸━?④만 ?쇳뻾', '054-100-0019', 'https://example.com/festivals/gyeongju-night', '寃쎌긽遺곷룄 寃쎌＜???ъ꽍濡?1080', NULL, '38166', 129.2092000000, 35.8378000000, NULL, NULL, '6', 'Type1', '?⑸━?④만?먯꽌 ?대━???쇨컙 ?곗콉??異뺤젣?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000020, 35, NULL, 15, '寃쎌＜ ?좊씪 泥?뀈 留덉폆', '054-100-0020', 'https://example.com/festivals/gyeongju-market', '寃쎌긽遺곷룄 寃쎌＜??泥⑥꽦濡?140-25', NULL, '38170', 129.2194000000, 35.8346000000, NULL, NULL, '6', 'Type1', '??궗 愿愿묎텒?먯꽌 留뚮굹??泥?뀈 李쎌옉 留덉폆?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000021, 2, NULL, 15, '?몄쿇 媛쒗빆??臾명솕?쇳뻾', '032-100-0021', 'https://example.com/festivals/incheon-openport', '?몄쿇愿묒뿭??以묎뎄 ?좏룷濡?7踰덇만 80', NULL, '22314', 126.6247000000, 37.4729000000, NULL, NULL, '6', 'Type1', '媛쒗빆???쇰? ??궗 怨듦컙???쒖슜???쇨컙 臾명솕 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000022, 2, NULL, 15, '?몄쿇 ?〓룄 臾쇰튆 異뺤젣', '032-100-0022', 'https://example.com/festivals/incheon-songdo', '?몄쿇愿묒뿭???곗닔援?而⑤깽?쒖븘?濡?160', NULL, '22004', 126.6469000000, 37.3926000000, NULL, NULL, '6', 'Type1', '?〓룄 ?쇳듃?댄뙆??二쇰??먯꽌 ?대━???섎? 異뺤젣?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000023, 2, NULL, 15, '?몄쿇 李⑥씠?섑????몃뱶 ?곗씠', '032-100-0023', 'https://example.com/festivals/incheon-food', '?몄쿇愿묒뿭??以묎뎄 李⑥씠?섑??대줈 59', NULL, '22312', 126.6187000000, 37.4752000000, NULL, NULL, '6', 'Type1', '李⑥씠?섑???癒밴굅由ъ? 怨듭뿰???④퍡 利먭린???됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000024, 38, NULL, 15, '?ъ닔 諛ㅻ컮??踰꾩뒪??, '061-100-0024', 'https://example.com/festivals/yeosu-night', '?꾨씪?⑤룄 ?ъ닔???섎찞濡?102', NULL, '59746', 127.7363000000, 34.7394000000, NULL, NULL, '6', 'Type1', '?ъ닔 諛ㅻ컮?ㅻ? 諛곌꼍?쇰줈 ???쇨컙 ?뚯븙 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000025, 38, NULL, 15, '?ъ닔 ?뚯궛 ?댁긽 留덉폆', '061-100-0025', 'https://example.com/festivals/yeosu-market', '?꾨씪?⑤룄 ?ъ닔???뚯궛???뚯궛濡?3617-7', NULL, '59771', 127.7526000000, 34.7195000000, NULL, NULL, '6', 'Type1', '?뚯궛沅??댁궛臾쇨낵 濡쒖뺄 ?곹뭹???뚭컻?섎뒗 留덉폆?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000026, 38, NULL, 15, '?ъ닔 ??쭔?ъ감 ?섏뼱', '061-100-0026', 'https://example.com/festivals/yeosu-pocha', '?꾨씪?⑤룄 ?ъ닔???댁닚?좉킅?λ줈 146', NULL, '59737', 127.7339000000, 34.7410000000, NULL, NULL, '6', 'Type1', '??쭔?ъ감 嫄곕━? ?곌퀎??癒밴굅由??섏뼱?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000027, 32, NULL, 15, '?띿큹 ?ㅼ븙 濡쒖뺄 ?섏뒪?', '033-100-0027', 'https://example.com/festivals/sokcho-seorak', '媛뺤썝?밸퀎?먯튂???띿큹???ㅼ븙?곕줈 833', NULL, '24903', 128.4655000000, 38.1194000000, NULL, NULL, '6', 'Type1', '?ㅼ븙??愿愿묎텒 濡쒖뺄 釉뚮옖?쒖? 怨듭뿰???④퍡?섎뒗 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000028, 32, NULL, 15, '?띿큹 以묒븰?쒖옣 誘몄떇 二쇨컙', '033-100-0028', 'https://example.com/festivals/sokcho-food', '媛뺤썝?밸퀎?먯튂???띿큹??以묒븰濡?47踰덇만 16', NULL, '24832', 128.5906000000, 38.2044000000, NULL, NULL, '6', 'Type1', '?띿큹 以묒븰?쒖옣 癒밴굅由щ? 二쇱젣濡???誘몄떇 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000029, 32, NULL, 15, '?띿큹 ?대? ?몃㉧ ?ㅽ뀒?댁?', '033-100-0029', 'https://example.com/festivals/sokcho-beach', '媛뺤썝?밸퀎?먯튂???띿큹???댁삤由꾨줈 190', NULL, '24887', 128.6015000000, 38.1907000000, NULL, NULL, '6', 'Type1', '?띿큹?대??먯꽌 ?대━???щ쫫 怨듭뿰 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000030, 1, NULL, 15, '?쒖슱 ?쒓컯 ?쇳겕??肄섏꽌??, '02-100-0030', 'https://example.com/festivals/seoul-hangang', '?쒖슱?밸퀎???곷벑?ш뎄 ?ъ쓽?숇줈 330', NULL, '07337', 126.9349000000, 37.5283000000, NULL, NULL, '6', 'Type1', '?쒓컯怨듭썝?먯꽌 利먭린???쇳겕?됲삎 肄섏꽌?몄엯?덈떎.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000031, 1, NULL, 15, '?쒖슱 ?깆닔 ?붿옄??留덉폆', '02-100-0031', 'https://example.com/festivals/seoul-seongsu', '?쒖슱?밸퀎???깅룞援??곕Т?κ만 20', NULL, '04782', 127.0569000000, 37.5437000000, NULL, NULL, '6', 'Type1', '?깆닔???붿옄??釉뚮옖?쒖? ?앹뾽??留뚮굹???됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000032, 1, NULL, 15, '?쒖슱 遺곸큿 怨듭삁 ?곗콉', '02-100-0032', 'https://example.com/festivals/seoul-bukchon', '?쒖슱?밸퀎??醫낅줈援?怨꾨룞湲?37', NULL, '03059', 126.9849000000, 37.5826000000, NULL, NULL, '6', 'Type1', '遺곸큿 怨⑤ぉ 怨듬갑???뚯븘蹂대뒗 怨듭삁 泥댄뿕 ?됱궗?낅땲??', '2026-06-01 10:00:00', '2026-06-01 10:00:00')
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
  (2000001, 4, '?援ш?愿묒옱??, '053-100-0001', '以묎뎄泥?, '053-100-1001', '2026-06-14', '2026-06-16', '援?콈蹂댁긽?대룞湲곕뀗怨듭썝', '18:00-22:00', '?꾩옣 ?묒닔', '?몃뱶議닿낵 ?쇱쇅臾대?', '踰꾩뒪?? ?몃뱶?몃윮', '移섎㎘ ?쇳겕?? ?쇨컙 怨듭뿰', '臾대즺 ?낆옣', '?쇰? 泥댄뿕 ?좊즺', 'A'),
  (2000002, 4, '?援ш?愿묒옱??, '053-100-0002', '以묎뎄泥?, '053-100-1002', '2026-06-20', '2026-06-22', '洹쇰?怨⑤ぉ ?쇰?', '17:00-22:00', '?꾩옣 ?묒닔', '怨⑤ぉ ?ъ뼱? ?쇱떆??, '?ㅽ꺃???ъ뼱', '?쇱떆?? ?댁꽕 ?ъ뼱', '臾대즺 ?낆옣', '?놁쓬', 'A'),
  (2000003, 4, '?援ъ껌?꾩꽱??, '053-100-0003', '以묎뎄?곸씤??, '053-100-1003', '2026-07-04', '2026-07-07', '?숈꽦濡??쇱쇅臾대?', '16:00-21:00', '?⑤씪???ъ쟾 ?좎껌', '嫄곕━ 怨듭뿰議?, '?ㅽ뵂留덉씠??, '泥?뀈 踰꾩뒪?? ?뚮━留덉폆', '臾대즺 ?낆옣', '?놁쓬', 'A'),
  (2000004, 4, '?섏꽦援ъ껌', '053-100-0004', '?援щЦ?붿옱??, '053-100-1004', '2026-07-18', '2026-07-21', '?섏꽦紐??곹솕?숈궛', '19:00-22:30', '?꾩옣 ?묒닔', '?몄닔 ?곗콉濡?, '?쇱씠???쇰젅?대뱶', '議곕챸?? ?뚯븙 怨듭뿰', '臾대즺 ?낆옣', '?놁쓬', 'A'),
  (2000005, 4, '?숆뎄泥?, '053-100-0005', '?붽났?곗긽?명쉶', '053-100-1005', '2026-08-08', '2026-08-11', '?붽났???쒕??덉쟾?뚮쭏?뚰겕 ?멸렐', '11:00-18:00', '?꾩옣 ?묒닔', '?뀁냽 留덉폆議?, '怨듭삁 泥댄뿕', '濡쒖뺄 留덉폆, ??泥댄뿕', '臾대즺 ?낆옣', '泥댄뿕 ?쇰? ?좊즺', 'B'),
  (2000006, 6, '遺?곌?愿묎났??, '051-100-0006', '?댁슫?援ъ껌', '051-100-1006', '2026-06-12', '2026-06-15', '?댁슫??댁닔?뺤옣', '19:00-23:00', '?⑤씪???덈ℓ', '?대? ?쇱쇅 ?곸쁺愿', '愿媛앷낵?????, '?쇱쇅 ?곹솕 ?곸쁺, 踰꾩뒪??, '臾대즺 ?낆옣', '醫뚯꽍 ?ъ쟾 ?덉빟', 'A'),
  (2000007, 6, '?섏쁺援ъ껌', '051-100-0007', '遺?곕Ц?붿옱??, '051-100-1007', '2026-07-10', '2026-07-12', '愿묒븞由ы빐?섏슃??, '20:00-22:00', '?꾩옣 愿??, '?대? 愿??援ъ뿭', '?ы넗議?, '?쒕줎?? ?쇨컙 怨듭뿰', '臾대즺 ?낆옣', '?놁쓬', 'A'),
  (2000008, 6, '遺?곗쨷援ъ껌', '051-100-0008', '愿묐났濡쒖긽?명쉶', '051-100-1008', '2026-08-14', '2026-08-17', '愿묐났濡??쇰?', '12:00-21:00', '?꾩옣 ?묒닔', '?먮룄???몃뱶議?, '嫄곕━ 怨듭뿰', '濡쒖뺄?몃뱶 遺?? 怨듭뿰', '臾대즺 ?낆옣', '?뚯떇 援щℓ 蹂꾨룄', 'B'),
  (2000009, 39, '?쒖＜愿愿묎났??, '064-100-0009', '?좎썡??, '064-100-1009', '2026-06-18', '2026-06-21', '?좎썡 ?댁븞 ?쇰?', '15:00-22:00', '?⑤씪???덈ℓ', '?댁븞 罹좏븨議?, '?붽?, 踰꾩뒪??, '罹좏븨, 怨듭뿰, 濡쒖뺄?몃뱶', '?쇰? ?좊즺', '議곌린 ?덈ℓ ?좎씤', 'A'),
  (2000010, 39, '?쒖＜??, '064-100-0010', '濡쒖뺄釉뚮옖?쒗삊??, '064-100-1010', '2026-07-05', '2026-07-07', '愿?뺤젙 愿묒옣', '11:00-18:00', '?꾩옣 ?묒닔', '?뚮━留덉폆議?, '媛먭랠 ?붿????대옒??, '留덉폆, 泥댄뿕 遺??, '臾대즺 ?낆옣', '泥댄뿕 ?쇰? ?좊즺', 'B'),
  (2000011, 39, '?쒓??ъ떆', '064-100-0011', '?깆궛??, '064-100-1011', '2026-08-01', '2026-08-03', '?깆궛?쇱텧遊??멸렐', '06:00-09:00', '?⑤씪???좎껌', '?쇱텧 愿??援ъ뿭', '?꾩묠 ?뚮줈源?, '?쇱텧 ?뚯븙?? ?곗콉 ?꾨줈洹몃옩', '臾대즺 ?낆옣', '?놁쓬', 'A'),
  (2000012, 32, '媛뺣쫱??, '033-100-0012', '?덈ぉ?곸씤??, '033-100-1012', '2026-06-21', '2026-06-23', '?덈ぉ 而ㅽ뵾嫄곕━', '14:00-21:00', '?꾩옣 ?묒닔', '移댄럹 嫄곕━ 怨듭뿰議?, '而ㅽ뵾 ?쒖쓬', '踰꾩뒪?? 濡쒖뒪???대옒??, '臾대즺 ?낆옣', '?대옒???좊즺', 'A'),
  (2000013, 32, '媛뺣쫱??, '033-100-0013', '寃쏀룷??, '033-100-1013', '2026-07-17', '2026-07-20', '寃쏀룷???곗콉濡?, '10:00-20:00', '?꾩옣 ?묒닔', '?몄닔湲??됱궗??, '?ㅽ꺃???ъ뼱', '?곗콉 怨듭뿰, 泥댄뿕 遺??, '臾대즺 ?낆옣', '?놁쓬', 'B'),
  (2000014, 32, '媛뺣쫱臾명솕?щ떒', '033-100-0014', '媛뺣쫱以묒븰?쒖옣', '033-100-1014', '2026-08-21', '2026-08-23', '媛뺣쫱 ?먮룄???쇰?', '17:00-22:00', '?꾩옣 ?묒닔', '?쇨컙 留덉폆議?, '濡쒖뺄 怨듭뿰', '?쇱떆?? 怨듭삁 留덉폆', '臾대즺 ?낆옣', '?곹뭹 援щℓ 蹂꾨룄', 'B'),
  (2000015, 37, '?꾩＜??, '063-100-0015', '?쒖삦留덉쓣?묒쓽??, '063-100-1015', '2026-06-13', '2026-06-15', '?꾩＜ ?쒖삦留덉쓣', '18:00-22:00', '?⑤씪???좎껌', '?쒖삦留덉쓣 怨⑤ぉ', '?쇨컙 ?댁꽕', '?щ튆 ?곗콉, ?꾪넻 怨듭뿰', '臾대즺 ?낆옣', '?댁꽕 ?ъ쟾 ?좎껌', 'A'),
  (2000016, 37, '?꾩＜??, '063-100-0016', '泥?뀈?고봽?묓쉶', '063-100-1016', '2026-07-11', '2026-07-13', '?꾩＜ ?⑤??쒖옣', '11:00-20:00', '?꾩옣 ?묒닔', '?몃뱶 ?섏뼱議?, '?고봽 ?좏겕', '鍮꾨퉼 ?몃뱶 ?섏뼱, ?쒖떇', '臾대즺 ?낆옣', '?뚯떇 援щℓ 蹂꾨룄', 'A'),
  (2000017, 37, '?꾩＜臾명솕?щ떒', '063-100-0017', '?꾪넻二쇳삊??, '063-100-1017', '2026-08-07', '2026-08-09', '?띾궓臾?愿묒옣', '15:00-21:00', '?⑤씪???좎껌', '?꾪넻二?泥댄뿕議?, '?쒖쓬 ?대옒??, '?꾪넻二??쒖쓬, 濡쒖뺄?몃뱶', '?깆씤 ?몄쬆 ?꾩슂', '?ъ쟾 ?좎껌 ?좎씤', 'B'),
  (2000018, 35, '寃쎌＜??, '054-100-0018', '蹂대Ц愿愿묐떒吏', '054-100-1018', '2026-06-26', '2026-06-29', '蹂대Ц???쇰?', '14:00-22:00', '?꾩옣 ?묒닔', '?몃컲 怨듭뿰??, '遺덇퐙 誘몃땲??, '?몃컲 怨듭뿰, 泥댄뿕 遺??, '臾대즺 ?낆옣', '?놁쓬', 'A'),
  (2000019, 35, '寃쎌＜??, '054-100-0019', '?⑸━?④만?곸씤??, '054-100-1019', '2026-07-24', '2026-07-26', '?⑸━?④만 ?쇰?', '18:00-23:00', '?꾩옣 ?묒닔', '?쇨컙 ?곗콉 肄붿뒪', '?ы넗 ?ъ뼱', '?쇳뻾, 濡쒖뺄 怨듭뿰', '臾대즺 ?낆옣', '?놁쓬', 'A'),
  (2000020, 35, '寃쎌＜??, '054-100-0020', '泥?뀈李쎌뾽?묓쉶', '054-100-1020', '2026-08-15', '2026-08-18', '泥⑥꽦? ?멸렐', '11:00-19:00', '?꾩옣 ?묒닔', '泥?뀈 留덉폆議?, '怨듭삁 泥댄뿕', '李쎌옉 留덉폆, ??궗 泥댄뿕', '臾대즺 ?낆옣', '泥댄뿕 ?쇰? ?좊즺', 'B'),
  (2000021, 2, '?몄쿇??, '032-100-0021', '以묎뎄泥?, '032-100-1021', '2026-06-19', '2026-06-21', '媛쒗빆??臾명솕吏援?, '18:00-23:00', '?⑤씪???좎껌', '媛쒗빆???쇰?', '?쇨컙 ?꾩뒯??, '臾명솕?쇳뻾, 怨듭뿰', '臾대즺 ?낆옣', '?댁꽕 ?ъ쟾 ?좎껌', 'A'),
  (2000022, 2, '?곗닔援ъ껌', '032-100-0022', '?〓룄?곸씤??, '032-100-1022', '2026-07-25', '2026-07-28', '?〓룄 ?쇳듃?댄뙆??, '16:00-22:00', '?꾩옣 ?묒닔', '?섎? 怨듭뿰??, '臾쇰튆 ?ы넗議?, '?섎? 怨듭뿰, 留덉폆', '臾대즺 ?낆옣', '?놁쓬', 'A'),
  (2000023, 2, '以묎뎄泥?, '032-100-0023', '李⑥씠?섑??댁긽?명쉶', '032-100-1023', '2026-08-22', '2026-08-24', '李⑥씠?섑????쇰?', '11:00-20:00', '?꾩옣 ?묒닔', '?몃뱶 ?ㅽ듃由ы듃', '嫄곕━ ?쇰젅?대뱶', '?몃뱶 ?곗씠, 怨듭뿰', '臾대즺 ?낆옣', '?뚯떇 援щℓ 蹂꾨룄', 'B'),
  (2000024, 38, '?ъ닔??, '061-100-0024', '?ъ닔臾명솕?щ떒', '061-100-1024', '2026-06-27', '2026-06-30', '?섎찞?깅? ?멸렐', '19:00-23:00', '?꾩옣 愿??, '?댁븞 怨듭뿰議?, '?ы넗議?, '諛ㅻ컮??踰꾩뒪?? ?뚮━留덉폆', '臾대즺 ?낆옣', '?놁쓬', 'A'),
  (2000025, 38, '?ъ닔??, '061-100-0025', '?뚯궛?곸씤??, '061-100-1025', '2026-07-31', '2026-08-03', '?뚯궛怨듭썝 ?멸렐', '12:00-21:00', '?꾩옣 ?묒닔', '?댁긽 留덉폆議?, '?섏궛臾?泥댄뿕', '?댁긽 留덉폆, 怨듭뿰', '臾대즺 ?낆옣', '?곹뭹 援щℓ 蹂꾨룄', 'B'),
  (2000026, 38, '?ъ닔??, '061-100-0026', '??쭔?ъ감?묒쓽??, '061-100-1026', '2026-08-28', '2026-08-30', '?댁닚?좉킅???쇰?', '17:00-23:00', '?꾩옣 ?묒닔', '?ъ감 嫄곕━', '?쇨컙 怨듭뿰', '??쭔?ъ감 ?섏뼱, 怨듭뿰', '臾대즺 ?낆옣', '?뚯떇 援щℓ 蹂꾨룄', 'B'),
  (2000027, 32, '?띿큹??, '033-100-0027', '?ㅼ븙愿愿묓삊??, '033-100-1027', '2026-06-28', '2026-07-01', '?ㅼ븙???뚭났??, '10:00-18:00', '?⑤씪???좎껌', '?ㅼ븙??愿愿묎텒', '濡쒖뺄 ?ъ뼱', '濡쒖뺄 ?섏뒪?, ?곗콉 ?꾨줈洹몃옩', '臾대즺 ?낆옣', '?쇰? 泥댄뿕 ?좊즺', 'A'),
  (2000028, 32, '?띿큹??, '033-100-0028', '?띿큹以묒븰?쒖옣', '033-100-1028', '2026-07-18', '2026-07-21', '?띿큹 以묒븰?쒖옣', '11:00-21:00', '?꾩옣 ?묒닔', '?쒖옣 誘몄떇議?, '荑좏궧??, '誘몄떇 二쇨컙, ?쒖옣 ?ъ뼱', '臾대즺 ?낆옣', '?뚯떇 援щℓ 蹂꾨룄', 'A'),
  (2000029, 32, '?띿큹??, '033-100-0029', '?띿큹?대??곸씤??, '033-100-1029', '2026-08-01', '2026-08-05', '?띿큹?댁닔?뺤옣', '17:00-23:00', '?꾩옣 愿??, '?대? 臾대?', '鍮꾩튂 寃뚯엫', '?대? 怨듭뿰, ?쇨컙 留덉폆', '臾대즺 ?낆옣', '?놁쓬', 'A'),
  (2000030, 1, '?쒖슱??, '02-100-0030', '?곷벑?ш뎄泥?, '02-100-1030', '2026-06-15', '2026-06-18', '?ъ쓽???쒓컯怨듭썝', '17:00-22:00', '?⑤씪???덈ℓ', '?쒓컯 ?쇳겕?됱〈', '?몃뱶?몃윮', '?쇳겕??肄섏꽌?? 留덉폆', '臾대즺 ?낆옣', '醫뚯꽍 ?ъ쟾 ?좎껌', 'A'),
  (2000031, 1, '?깅룞援ъ껌', '02-100-0031', '?깆닔釉뚮옖?쒗삊??, '02-100-1031', '2026-07-08', '2026-07-12', '?깆닔???곕Т?κ만', '12:00-20:00', '?꾩옣 ?묒닔', '?앹뾽 嫄곕━', '?붿옄???대옒??, '?붿옄??留덉폆, ?앹뾽', '臾대즺 ?낆옣', '泥댄뿕 ?쇰? ?좊즺', 'B'),
  (2000032, 1, '醫낅줈援ъ껌', '02-100-0032', '遺곸큿怨듬갑?묓쉶', '02-100-1032', '2026-08-12', '2026-08-15', '遺곸큿 ?쒖삦留덉쓣', '10:00-18:00', '?⑤씪???좎껌', '怨듭삁 怨듬갑 嫄곕━', '怨듬갑 ?ъ뼱', '怨듭삁 泥댄뿕, 怨⑤ぉ ?곗콉', '臾대즺 ?낆옣', '泥댄뿕 ?쇰? ?좊즺', 'B')
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
