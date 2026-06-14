USE `samteo_db`;

INSERT INTO `USER`
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

INSERT INTO `META_AREA` (`area_code`, `area_name`)
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

INSERT INTO `META_SIGUNGU` (`sigungu_id`, `area_code`, `sigungu_code`, `sigungu_name`)
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

INSERT INTO `META_CONTENT_TYPE` (`content_type_id`, `content_type_name`, `is_active`)
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

INSERT INTO `TOUR_CONTENT`
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

INSERT INTO `TOUR_CONTENT_OVERVIEW` (`content_id`, `overview`)
VALUES
  (1000001, 'Sample overview for a traditional village sightseeing destination.'),
  (1000002, 'Sample overview for a large seasonal festival near the beach.'),
  (1000003, 'Sample overview for a stay-oriented lodging property.'),
  (1000004, 'Sample overview for a local food destination.'),
  (1000005, 'Sample overview for a regional shopping destination.'),
  (1000006, 'Sample overview for a museum-style culture facility.')
ON DUPLICATE KEY UPDATE
  `overview` = VALUES(`overview`);

INSERT INTO `TOUR_CONTENT_IMAGE` (`image_id`, `content_id`, `image_name`, `origin_img_url`, `small_img_url`, `sort_order`)
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

INSERT INTO `DETAIL_TOURSPOT`
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

INSERT INTO `DETAIL_CULTURE_FACILITY`
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

INSERT INTO `DETAIL_FESTIVAL`
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

INSERT INTO `DETAIL_ACCOMMODATION`
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

INSERT INTO `DETAIL_SHOPPING`
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

INSERT INTO `DETAIL_RESTAURANT`
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

INSERT INTO `TOUR_CONTENT_REPEAT_INFO`
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

INSERT INTO `TOUR_API_SYNC_HISTORY`
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

INSERT INTO `TOUR_CONTENT`
  (`content_id`, `area_code`, `sigungu_id`, `content_type_id`, `title`, `tel`, `homepage`, `addr1`, `addr2`, `zipcode`, `mapx`, `mapy`, `first_image`, `first_image2`, `mlevel`, `cpyrht_div_cd`, `overview_summary`, `created_time`, `modified_time`)
VALUES
  (2000001, 4, NULL, 15, '대구 치맥 피크닉 페스타', '053-100-0001', 'https://example.com/festivals/daegu-chimac', '대구광역시 중구 국채보상로 670', NULL, '41911', 128.6014000000, 35.8714000000, NULL, NULL, '6', 'Type1', '대구 도심 공원에서 열리는 야간 먹거리 축제입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000002, 4, NULL, 15, '대구 근대골목 야시장', '053-100-0002', 'https://example.com/festivals/daegu-modern', '대구광역시 중구 서성로 10', NULL, '41918', 128.5909000000, 35.8707000000, NULL, NULL, '6', 'Type1', '근대골목 일대에서 진행되는 로컬 야시장 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000003, 4, NULL, 15, '대구 동성로 버스킹 위크', '053-100-0003', 'https://example.com/festivals/daegu-busking', '대구광역시 중구 동성로 22', NULL, '41942', 128.5965000000, 35.8683000000, NULL, NULL, '6', 'Type1', '동성로에서 열리는 청년 음악 공연 축제입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000004, 4, NULL, 15, '대구 수성못 라이트 쇼', '053-100-0004', 'https://example.com/festivals/daegu-suseong', '대구광역시 수성구 용학로 35', NULL, '42217', 128.6176000000, 35.8269000000, NULL, NULL, '6', 'Type1', '수성못 산책로에서 열리는 조명과 음악 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000005, 4, NULL, 15, '대구 팔공산 숲속 마켓', '053-100-0005', 'https://example.com/festivals/daegu-palgong', '대구광역시 동구 팔공산로 199', NULL, '41007', 128.6908000000, 35.9901000000, NULL, NULL, '6', 'Type1', '팔공산권 로컬 공예와 먹거리 마켓입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000006, 6, NULL, 15, '부산 해운대 바다 영화제', '051-100-0006', 'https://example.com/festivals/busan-movie', '부산광역시 해운대구 해운대해변로 264', NULL, '48094', 129.1589000000, 35.1587000000, NULL, NULL, '6', 'Type1', '해운대 해변에서 즐기는 야외 영화 축제입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000007, 6, NULL, 15, '부산 광안리 드론 나이트', '051-100-0007', 'https://example.com/festivals/busan-drone', '부산광역시 수영구 광안해변로 219', NULL, '48303', 129.1185000000, 35.1532000000, NULL, NULL, '6', 'Type1', '광안리 야경과 함께하는 드론 공연 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000008, 6, NULL, 15, '부산 로컬푸드 스트리트', '051-100-0008', 'https://example.com/festivals/busan-food', '부산광역시 중구 광복로 58', NULL, '48953', 129.0318000000, 35.1009000000, NULL, NULL, '6', 'Type1', '부산 원도심 먹거리와 공연을 함께 즐기는 축제입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000009, 39, NULL, 15, '제주 바다캠프 페스티벌', '064-100-0009', 'https://example.com/festivals/jeju-camp', '제주특별자치도 제주시 애월읍 애월해안로 620', NULL, '63047', 126.3096000000, 33.4626000000, NULL, NULL, '6', 'Type1', '해안 캠핑과 로컬 공연이 결합된 제주 축제입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000010, 39, NULL, 15, '제주 감귤 플리마켓', '064-100-0010', 'https://example.com/festivals/jeju-market', '제주특별자치도 제주시 관덕로 14', NULL, '63166', 126.5219000000, 33.5130000000, NULL, NULL, '6', 'Type1', '감귤 디저트와 로컬 브랜드를 만나는 플리마켓입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000011, 39, NULL, 15, '제주 성산 일출 음악회', '064-100-0011', 'https://example.com/festivals/jeju-sunrise', '제주특별자치도 서귀포시 성산읍 일출로 284-12', NULL, '63643', 126.9426000000, 33.4581000000, NULL, NULL, '6', 'Type1', '성산 일출권에서 열리는 아침 음악 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000012, 32, NULL, 15, '강릉 커피 버스킹 데이', '033-100-0012', 'https://example.com/festivals/gangneung-coffee', '강원특별자치도 강릉시 창해로 14', NULL, '25556', 128.9079000000, 37.7955000000, NULL, NULL, '6', 'Type1', '안목해변 커피거리에서 진행되는 버스킹 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000013, 32, NULL, 15, '강릉 경포 호수길 축제', '033-100-0013', 'https://example.com/festivals/gangneung-lake', '강원특별자치도 강릉시 경포로 365', NULL, '25461', 128.8961000000, 37.8034000000, NULL, NULL, '6', 'Type1', '경포호 주변 산책과 공연을 함께 즐기는 축제입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000014, 32, NULL, 15, '강릉 솔향 야간 마켓', '033-100-0014', 'https://example.com/festivals/gangneung-night', '강원특별자치도 강릉시 임영로 131', NULL, '25528', 128.8958000000, 37.7519000000, NULL, NULL, '6', 'Type1', '강릉 원도심에서 열리는 야간 로컬 마켓입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000015, 37, NULL, 15, '전주 한옥마을 달빛 산책', '063-100-0015', 'https://example.com/festivals/jeonju-moon', '전북특별자치도 전주시 완산구 기린대로 99', NULL, '55041', 127.1530000000, 35.8151000000, NULL, NULL, '6', 'Type1', '한옥마을 야간 해설과 공연이 있는 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000016, 37, NULL, 15, '전주 비빔 로컬 페어', '063-100-0016', 'https://example.com/festivals/jeonju-food', '전북특별자치도 전주시 완산구 팔달로 144', NULL, '55043', 127.1437000000, 35.8176000000, NULL, NULL, '6', 'Type1', '전주 식문화와 청년 셰프가 만나는 음식 축제입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000017, 37, NULL, 15, '전주 전통주 테이스팅 위크', '063-100-0017', 'https://example.com/festivals/jeonju-drink', '전북특별자치도 전주시 완산구 풍남문3길 1', NULL, '55043', 127.1489000000, 35.8136000000, NULL, NULL, '6', 'Type1', '전통주 시음과 로컬 푸드를 함께 소개하는 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000018, 35, NULL, 15, '경주 보문 호반 페스티벌', '054-100-0018', 'https://example.com/festivals/gyeongju-bomun', '경상북도 경주시 보문로 424-33', NULL, '38117', 129.2874000000, 35.8429000000, NULL, NULL, '6', 'Type1', '보문호 주변에서 열리는 공연과 체험 축제입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000019, 35, NULL, 15, '경주 황리단길 야행', '054-100-0019', 'https://example.com/festivals/gyeongju-night', '경상북도 경주시 포석로 1080', NULL, '38166', 129.2092000000, 35.8378000000, NULL, NULL, '6', 'Type1', '황리단길에서 열리는 야간 산책형 축제입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000020, 35, NULL, 15, '경주 신라 청년 마켓', '054-100-0020', 'https://example.com/festivals/gyeongju-market', '경상북도 경주시 첨성로 140-25', NULL, '38170', 129.2194000000, 35.8346000000, NULL, NULL, '6', 'Type1', '역사 관광권에서 만나는 청년 창작 마켓입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000021, 2, NULL, 15, '인천 개항장 문화야행', '032-100-0021', 'https://example.com/festivals/incheon-openport', '인천광역시 중구 신포로27번길 80', NULL, '22314', 126.6247000000, 37.4729000000, NULL, NULL, '6', 'Type1', '개항장 일대 역사 공간을 활용한 야간 문화 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000022, 2, NULL, 15, '인천 송도 물빛 축제', '032-100-0022', 'https://example.com/festivals/incheon-songdo', '인천광역시 연수구 컨벤시아대로 160', NULL, '22004', 126.6469000000, 37.3926000000, NULL, NULL, '6', 'Type1', '송도 센트럴파크 주변에서 열리는 수변 축제입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000023, 2, NULL, 15, '인천 차이나타운 푸드 데이', '032-100-0023', 'https://example.com/festivals/incheon-food', '인천광역시 중구 차이나타운로 59', NULL, '22312', 126.6187000000, 37.4752000000, NULL, NULL, '6', 'Type1', '차이나타운 먹거리와 공연을 함께 즐기는 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000024, 38, NULL, 15, '여수 밤바다 버스킹', '061-100-0024', 'https://example.com/festivals/yeosu-night', '전라남도 여수시 하멜로 102', NULL, '59746', 127.7363000000, 34.7394000000, NULL, NULL, '6', 'Type1', '여수 밤바다를 배경으로 한 야간 음악 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000025, 38, NULL, 15, '여수 돌산 해상 마켓', '061-100-0025', 'https://example.com/festivals/yeosu-market', '전라남도 여수시 돌산읍 돌산로 3617-7', NULL, '59771', 127.7526000000, 34.7195000000, NULL, NULL, '6', 'Type1', '돌산권 해산물과 로컬 상품을 소개하는 마켓입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000026, 38, NULL, 15, '여수 낭만포차 페어', '061-100-0026', 'https://example.com/festivals/yeosu-pocha', '전라남도 여수시 이순신광장로 146', NULL, '59737', 127.7339000000, 34.7410000000, NULL, NULL, '6', 'Type1', '낭만포차 거리와 연계한 먹거리 페어입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000027, 32, NULL, 15, '속초 설악 로컬 페스타', '033-100-0027', 'https://example.com/festivals/sokcho-seorak', '강원특별자치도 속초시 설악산로 833', NULL, '24903', 128.4655000000, 38.1194000000, NULL, NULL, '6', 'Type1', '설악산 관광권 로컬 브랜드와 공연이 함께하는 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000028, 32, NULL, 15, '속초 중앙시장 미식 주간', '033-100-0028', 'https://example.com/festivals/sokcho-food', '강원특별자치도 속초시 중앙로147번길 16', NULL, '24832', 128.5906000000, 38.2044000000, NULL, NULL, '6', 'Type1', '속초 중앙시장 먹거리를 주제로 한 미식 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000029, 32, NULL, 15, '속초 해변 썸머 스테이지', '033-100-0029', 'https://example.com/festivals/sokcho-beach', '강원특별자치도 속초시 해오름로 190', NULL, '24887', 128.6015000000, 38.1907000000, NULL, NULL, '6', 'Type1', '속초해변에서 열리는 여름 공연 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000030, 1, NULL, 15, '서울 한강 피크닉 콘서트', '02-100-0030', 'https://example.com/festivals/seoul-hangang', '서울특별시 영등포구 여의동로 330', NULL, '07337', 126.9349000000, 37.5283000000, NULL, NULL, '6', 'Type1', '한강공원에서 즐기는 피크닉형 콘서트입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000031, 1, NULL, 15, '서울 성수 디자인 마켓', '02-100-0031', 'https://example.com/festivals/seoul-seongsu', '서울특별시 성동구 연무장길 20', NULL, '04782', 127.0569000000, 37.5437000000, NULL, NULL, '6', 'Type1', '성수동 디자인 브랜드와 팝업을 만나는 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00'),
  (2000032, 1, NULL, 15, '서울 북촌 공예 산책', '02-100-0032', 'https://example.com/festivals/seoul-bukchon', '서울특별시 종로구 계동길 37', NULL, '03059', 126.9849000000, 37.5826000000, NULL, NULL, '6', 'Type1', '북촌 골목 공방을 돌아보는 공예 체험 행사입니다.', '2026-06-01 10:00:00', '2026-06-01 10:00:00')
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

INSERT INTO `DETAIL_FESTIVAL`
  (`content_id`, `area_code`, `sponsor1`, `sponsor1tel`, `sponsor2`, `sponsor2tel`, `event_start_date`, `event_end_date`, `event_place`, `playtime`, `booking_place`, `place_info`, `sub_event`, `program`, `use_time_festival`, `discountinfofestival`, `festival_grade`)
VALUES
  (2000001, 4, '대구관광재단', '053-100-0001', '중구청', '053-100-1001', '2026-06-14', '2026-06-16', '국채보상운동기념공원', '18:00-22:00', '현장 접수', '푸드존과 야외무대', '버스킹, 푸드트럭', '치맥 피크닉, 야간 공연', '무료 입장', '일부 체험 유료', 'A'),
  (2000002, 4, '대구관광재단', '053-100-0002', '중구청', '053-100-1002', '2026-06-20', '2026-06-22', '근대골목 일대', '17:00-22:00', '현장 접수', '골목 투어와 야시장', '스탬프 투어', '야시장, 해설 투어', '무료 입장', '없음', 'A'),
  (2000003, 4, '대구청년센터', '053-100-0003', '중구상인회', '053-100-1003', '2026-07-04', '2026-07-07', '동성로 야외무대', '16:00-21:00', '온라인 사전 신청', '거리 공연존', '오픈마이크', '청년 버스킹, 플리마켓', '무료 입장', '없음', 'A'),
  (2000004, 4, '수성구청', '053-100-0004', '대구문화재단', '053-100-1004', '2026-07-18', '2026-07-21', '수성못 상화동산', '19:00-22:30', '현장 접수', '호수 산책로', '라이트 퍼레이드', '조명쇼, 음악 공연', '무료 입장', '없음', 'A'),
  (2000005, 4, '동구청', '053-100-0005', '팔공산상인회', '053-100-1005', '2026-08-08', '2026-08-11', '팔공산 시민안전테마파크 인근', '11:00-18:00', '현장 접수', '숲속 마켓존', '공예 체험', '로컬 마켓, 숲 체험', '무료 입장', '체험 일부 유료', 'B'),
  (2000006, 6, '부산관광공사', '051-100-0006', '해운대구청', '051-100-1006', '2026-06-12', '2026-06-15', '해운대해수욕장', '19:00-23:00', '온라인 예매', '해변 야외 상영관', '관객과의 대화', '야외 영화 상영, 버스킹', '무료 입장', '좌석 사전 예약', 'A'),
  (2000007, 6, '수영구청', '051-100-0007', '부산문화재단', '051-100-1007', '2026-07-10', '2026-07-12', '광안리해수욕장', '20:00-22:00', '현장 관람', '해변 관람 구역', '포토존', '드론쇼, 야간 공연', '무료 입장', '없음', 'A'),
  (2000008, 6, '부산중구청', '051-100-0008', '광복로상인회', '051-100-1008', '2026-08-14', '2026-08-17', '광복로 일대', '12:00-21:00', '현장 접수', '원도심 푸드존', '거리 공연', '로컬푸드 부스, 공연', '무료 입장', '음식 구매 별도', 'B'),
  (2000009, 39, '제주관광공사', '064-100-0009', '애월읍', '064-100-1009', '2026-06-18', '2026-06-21', '애월 해안 일대', '15:00-22:00', '온라인 예매', '해안 캠핑존', '요가, 버스킹', '캠핑, 공연, 로컬푸드', '일부 유료', '조기 예매 할인', 'A'),
  (2000010, 39, '제주시', '064-100-0010', '로컬브랜드협회', '064-100-1010', '2026-07-05', '2026-07-07', '관덕정 광장', '11:00-18:00', '현장 접수', '플리마켓존', '감귤 디저트 클래스', '마켓, 체험 부스', '무료 입장', '체험 일부 유료', 'B'),
  (2000011, 39, '서귀포시', '064-100-0011', '성산읍', '064-100-1011', '2026-08-01', '2026-08-03', '성산일출봉 인근', '06:00-09:00', '온라인 신청', '일출 관람 구역', '아침 플로깅', '일출 음악회, 산책 프로그램', '무료 입장', '없음', 'A'),
  (2000012, 32, '강릉시', '033-100-0012', '안목상인회', '033-100-1012', '2026-06-21', '2026-06-23', '안목 커피거리', '14:00-21:00', '현장 접수', '카페 거리 공연존', '커피 시음', '버스킹, 로스터 클래스', '무료 입장', '클래스 유료', 'A'),
  (2000013, 32, '강릉시', '033-100-0013', '경포동', '033-100-1013', '2026-07-17', '2026-07-20', '경포호 산책로', '10:00-20:00', '현장 접수', '호수길 행사장', '스탬프 투어', '산책 공연, 체험 부스', '무료 입장', '없음', 'B'),
  (2000014, 32, '강릉문화재단', '033-100-0014', '강릉중앙시장', '033-100-1014', '2026-08-21', '2026-08-23', '강릉 원도심 일대', '17:00-22:00', '현장 접수', '야간 마켓존', '로컬 공연', '야시장, 공예 마켓', '무료 입장', '상품 구매 별도', 'B'),
  (2000015, 37, '전주시', '063-100-0015', '한옥마을협의회', '063-100-1015', '2026-06-13', '2026-06-15', '전주 한옥마을', '18:00-22:00', '온라인 신청', '한옥마을 골목', '야간 해설', '달빛 산책, 전통 공연', '무료 입장', '해설 사전 신청', 'A'),
  (2000016, 37, '전주시', '063-100-0016', '청년셰프협회', '063-100-1016', '2026-07-11', '2026-07-13', '전주 남부시장', '11:00-20:00', '현장 접수', '푸드 페어존', '셰프 토크', '비빔 푸드 페어, 시식', '무료 입장', '음식 구매 별도', 'A'),
  (2000017, 37, '전주문화재단', '063-100-0017', '전통주협회', '063-100-1017', '2026-08-07', '2026-08-09', '풍남문 광장', '15:00-21:00', '온라인 신청', '전통주 체험존', '시음 클래스', '전통주 시음, 로컬푸드', '성인 인증 필요', '사전 신청 할인', 'B'),
  (2000018, 35, '경주시', '054-100-0018', '보문관광단지', '054-100-1018', '2026-06-26', '2026-06-29', '보문호 일대', '14:00-22:00', '현장 접수', '호반 공연장', '불꽃 미니쇼', '호반 공연, 체험 부스', '무료 입장', '없음', 'A'),
  (2000019, 35, '경주시', '054-100-0019', '황리단길상인회', '054-100-1019', '2026-07-24', '2026-07-26', '황리단길 일대', '18:00-23:00', '현장 접수', '야간 산책 코스', '포토 투어', '야행, 로컬 공연', '무료 입장', '없음', 'A'),
  (2000020, 35, '경주시', '054-100-0020', '청년창업협회', '054-100-1020', '2026-08-15', '2026-08-18', '첨성대 인근', '11:00-19:00', '현장 접수', '청년 마켓존', '공예 체험', '창작 마켓, 역사 체험', '무료 입장', '체험 일부 유료', 'B'),
  (2000021, 2, '인천시', '032-100-0021', '중구청', '032-100-1021', '2026-06-19', '2026-06-21', '개항장 문화지구', '18:00-23:00', '온라인 신청', '개항장 일대', '야간 도슨트', '문화야행, 공연', '무료 입장', '해설 사전 신청', 'A'),
  (2000022, 2, '연수구청', '032-100-0022', '송도상인회', '032-100-1022', '2026-07-25', '2026-07-28', '송도 센트럴파크', '16:00-22:00', '현장 접수', '수변 공연장', '물빛 포토존', '수변 공연, 마켓', '무료 입장', '없음', 'A'),
  (2000023, 2, '중구청', '032-100-0023', '차이나타운상인회', '032-100-1023', '2026-08-22', '2026-08-24', '차이나타운 일대', '11:00-20:00', '현장 접수', '푸드 스트리트', '거리 퍼레이드', '푸드 데이, 공연', '무료 입장', '음식 구매 별도', 'B'),
  (2000024, 38, '여수시', '061-100-0024', '여수문화재단', '061-100-1024', '2026-06-27', '2026-06-30', '하멜등대 인근', '19:00-23:00', '현장 관람', '해안 공연존', '포토존', '밤바다 버스킹, 플리마켓', '무료 입장', '없음', 'A'),
  (2000025, 38, '여수시', '061-100-0025', '돌산상인회', '061-100-1025', '2026-07-31', '2026-08-03', '돌산공원 인근', '12:00-21:00', '현장 접수', '해상 마켓존', '수산물 체험', '해상 마켓, 공연', '무료 입장', '상품 구매 별도', 'B'),
  (2000026, 38, '여수시', '061-100-0026', '낭만포차협의회', '061-100-1026', '2026-08-28', '2026-08-30', '이순신광장 일대', '17:00-23:00', '현장 접수', '포차 거리', '야간 공연', '낭만포차 페어, 공연', '무료 입장', '음식 구매 별도', 'B'),
  (2000027, 32, '속초시', '033-100-0027', '설악관광협회', '033-100-1027', '2026-06-28', '2026-07-01', '설악산 소공원', '10:00-18:00', '온라인 신청', '설악산 관광권', '로컬 투어', '로컬 페스타, 산책 프로그램', '무료 입장', '일부 체험 유료', 'A'),
  (2000028, 32, '속초시', '033-100-0028', '속초중앙시장', '033-100-1028', '2026-07-18', '2026-07-21', '속초 중앙시장', '11:00-21:00', '현장 접수', '시장 미식존', '쿠킹쇼', '미식 주간, 시장 투어', '무료 입장', '음식 구매 별도', 'A'),
  (2000029, 32, '속초시', '033-100-0029', '속초해변상인회', '033-100-1029', '2026-08-01', '2026-08-05', '속초해수욕장', '17:00-23:00', '현장 관람', '해변 무대', '비치 게임', '해변 공연, 야간 마켓', '무료 입장', '없음', 'A'),
  (2000030, 1, '서울시', '02-100-0030', '영등포구청', '02-100-1030', '2026-06-15', '2026-06-18', '여의도 한강공원', '17:00-22:00', '온라인 예매', '한강 피크닉존', '푸드트럭', '피크닉 콘서트, 마켓', '무료 입장', '좌석 사전 신청', 'A'),
  (2000031, 1, '성동구청', '02-100-0031', '성수브랜드협회', '02-100-1031', '2026-07-08', '2026-07-12', '성수동 연무장길', '12:00-20:00', '현장 접수', '팝업 거리', '디자인 클래스', '디자인 마켓, 팝업', '무료 입장', '체험 일부 유료', 'B'),
  (2000032, 1, '종로구청', '02-100-0032', '북촌공방협회', '02-100-1032', '2026-08-12', '2026-08-15', '북촌 한옥마을', '10:00-18:00', '온라인 신청', '공예 공방 거리', '공방 투어', '공예 체험, 골목 산책', '무료 입장', '체험 일부 유료', 'B')
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
