USE `samteo_db`;
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Short-term (part-time) dummy jobs for all 12 supported cities.
-- category / employment_type / working_days values MUST match the
-- English keys that JobResponse.from() translates to Korean.

INSERT INTO `jobs`
  (`id`, `title`, `company`, `city_id`, `city_name`, `region_id`, `district`,
   `address`, `category`, `employment_type`, `monthly_salary`, `working_days`,
   `commute_minutes`, `latitude`, `longitude`)
VALUES

-- ========== 서울 (seoul, region_id='1') ==========
('short-seoul-01', '카페 홀서빙 스태프', '블루보틀 커피 종로점', 'seoul', '서울', '1', '종로구',
 '서울 종로구 율곡로 84', 'Food and Beverage', 'Part-time', 2096270,
 'Mon-Fri 10:00-17:00', 15, 37.5720, 126.9850),

('short-seoul-02', '게스트하우스 프론트 데스크', '이태원 크로스로드 게스트하우스', 'seoul', '서울', '1', '용산구',
 '서울 용산구 이태원로 177', 'Accommodation', 'Part-time', 2200000,
 'Tue-Sat 09:00-18:00', 20, 37.5343, 126.9942),

('short-seoul-03', '편의점 야간 알바', 'GS25 강남역점', 'seoul', '서울', '1', '강남구',
 '서울 강남구 강남대로 390', 'Store', 'Part-time', 2300000,
 '3 nights per week', 10, 37.4979, 127.0276),

('short-seoul-04', '음식점 홀 서빙', '광화문 설렁탕', 'seoul', '서울', '1', '종로구',
 '서울 종로구 세종대로 178', 'Food Service', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 18, 37.5726, 126.9769),

('short-seoul-05', '이벤트 행사 도우미', '코엑스 이벤트팀', 'seoul', '서울', '1', '강남구',
 '서울 강남구 영동대로 513', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 25, 37.5126, 127.0594),

('short-seoul-06', '마트 캐셔', '이마트 서초점', 'seoul', '서울', '1', '서초구',
 '서울 서초구 반포대로 45', 'Shopping', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 12, 37.5040, 127.0050),

('short-seoul-07', '카페 바리스타', '스타벅스 홍대입구점', 'seoul', '서울', '1', '마포구',
 '서울 마포구 양화로 188', 'Food and Beverage', 'Part-time', 2200000,
 'Mon-Fri 08:00-17:00', 22, 37.5572, 126.9246),

('short-seoul-08', '물류센터 포장 스태프', 'CJ대한통운 송파허브', 'seoul', '서울', '1', '송파구',
 '서울 송파구 올림픽로 269', 'Logistics', 'Short contract', 2400000,
 '5 mornings per week', 30, 37.5145, 127.1059),

('short-seoul-09', '관광 안내 도우미', '서울관광재단 명동안내소', 'seoul', '서울', '1', '중구',
 '서울 중구 명동길 14', 'Tourism', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 20, 37.5636, 126.9849),

('short-seoul-10', '판매직 스태프', '롯데백화점 본점', 'seoul', '서울', '1', '중구',
 '서울 중구 남대문로 81', 'Shopping', 'Part-time', 2200000,
 'Thu-Mon 11:00-20:00', 18, 37.5641, 126.9811),

-- ========== 부산 (busan, region_id='6') ==========
('short-busan-01', '카페 홀 알바', '파란달 커피 해운대점', 'busan', '부산', '6', '해운대구',
 '부산 해운대구 해운대해변로 264', 'Food and Beverage', 'Part-time', 2096270,
 'Mon-Fri 10:00-17:00', 15, 35.1587, 129.1604),

('short-busan-02', '게스트하우스 청소·정리', '남포동 바다뷰 게스트하우스', 'busan', '부산', '6', '중구',
 '부산 중구 광복로 21', 'Accommodation', 'Part-time', 2096270,
 '5 mornings per week', 20, 35.0981, 129.0296),

('short-busan-03', '편의점 주간 알바', 'CU 서면역점', 'busan', '부산', '6', '부산진구',
 '부산 부산진구 서면로 39', 'Store', 'Part-time', 2096270,
 'Mon-Fri 08:00-17:00', 10, 35.1583, 129.0591),

('short-busan-04', '횟집 홀서빙', '동래횟집', 'busan', '부산', '6', '동래구',
 '부산 동래구 온천장로 63', 'Food Service', 'Part-time', 2200000,
 'Tue-Sun 10:00-19:00', 25, 35.2037, 129.0794),

('short-busan-05', '해변 카페 서빙', '광안리 선셋카페', 'busan', '부산', '6', '수영구',
 '부산 수영구 광안해변로 232', 'Food and Beverage', 'Part-time', 2200000,
 'Wed-Sun 09:00-18:00', 18, 35.1530, 129.1186),

('short-busan-06', '항구 물류 보조', '부산신항 물류센터', 'busan', '부산', '6', '강서구',
 '부산 강서구 신항로 55', 'Logistics', 'Short contract', 2500000,
 'Mon-Fri 08:00-17:00', 40, 35.0817, 128.8219),

('short-busan-07', '관광 행사 스태프', '부산국제영화제 사무국', 'busan', '부산', '6', '해운대구',
 '부산 해운대구 수영강변대로 120', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 20, 35.1617, 129.1623),

('short-busan-08', '마트 판매 보조', '홈플러스 해운대점', 'busan', '부산', '6', '해운대구',
 '부산 해운대구 센텀남대로 59', 'Shopping', 'Part-time', 2096270,
 'Thu-Mon 11:00-20:00', 15, 35.1693, 129.1316),

('short-busan-09', '관광지 안내 알바', '부산시 관광정보센터', 'busan', '부산', '6', '중구',
 '부산 중구 광복로 49', 'Tourism', 'Part-time', 2096270,
 'Tue-Sat 09:00-18:00', 22, 35.1011, 129.0320),

('short-busan-10', '음식점 주방 보조', '국제시장 분식집', 'busan', '부산', '6', '중구',
 '부산 중구 국제시장1길 22', 'Food Service', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 18, 35.0996, 129.0307),

-- ========== 대구 (daegu, region_id='4') ==========
('short-daegu-01', '카페 바리스타 알바', '봉리단길 커피웍스', 'daegu', '대구', '4', '남구',
 '대구 남구 봉덕로 51', 'Food and Beverage', 'Part-time', 2096270,
 'Tue-Sat 09:00-18:00', 20, 35.8562, 128.5943),

('short-daegu-02', '게스트하우스 안내 데스크', '동성로 스테이', 'daegu', '대구', '4', '중구',
 '대구 중구 동성로3길 16', 'Accommodation', 'Part-time', 2096270,
 'Mon-Fri 10:00-17:00', 15, 35.8693, 128.5984),

('short-daegu-03', '편의점 야간 스태프', 'GS25 수성못점', 'daegu', '대구', '4', '수성구',
 '대구 수성구 달구벌대로528길 20', 'Store', 'Part-time', 2200000,
 '3 nights per week', 12, 35.8560, 128.6284),

('short-daegu-04', '전통시장 음식점 보조', '서문시장 칼국수', 'daegu', '대구', '4', '중구',
 '대구 중구 큰장로26길 45', 'Food Service', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 18, 35.8696, 128.5804),

('short-daegu-05', '이벤트 도우미', '대구엑스코 행사팀', 'daegu', '대구', '4', '북구',
 '대구 북구 엑스코로 10', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 30, 35.9090, 128.6134),

('short-daegu-06', '마트 판매 스태프', '이마트 달서점', 'daegu', '대구', '4', '달서구',
 '대구 달서구 달구벌대로 1600', 'Shopping', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 22, 35.8441, 128.5304),

('short-daegu-07', '물류 분류 스태프', '롯데 대구물류센터', 'daegu', '대구', '4', '달성군',
 '대구 달성군 화원읍 화원로 100', 'Logistics', 'Short contract', 2400000,
 '5 mornings per week', 35, 35.8007, 128.5246),

('short-daegu-08', '관광 안내 알바', '대구문화예술회관 인포메이션', 'daegu', '대구', '4', '달서구',
 '대구 달서구 공원순환로 201', 'Tourism', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 25, 35.8509, 128.5399),

('short-daegu-09', '음료 제조·포장 알바', '대구 전통음료 공방', 'daegu', '대구', '4', '중구',
 '대구 중구 국채보상로 673', 'Food and Beverage', 'Part-time', 2096270,
 'Wed-Sun 09:00-18:00', 18, 35.8692, 128.5941),

('short-daegu-10', '카페 홀·서빙 스태프', '수성못 뷰카페', 'daegu', '대구', '4', '수성구',
 '대구 수성구 수성못둘레길 50', 'Food and Beverage', 'Part-time', 2200000,
 'Thu-Mon 11:00-20:00', 15, 35.8574, 128.6295),

-- ========== 인천 (incheon, region_id='2') ==========
('short-incheon-01', '공항 카페 알바', '인천공항 커피빈', 'incheon', '인천', '2', '중구',
 '인천 중구 공항로 272', 'Food and Beverage', 'Part-time', 2300000,
 'Mon-Fri 08:00-17:00', 30, 37.4490, 126.4510),

('short-incheon-02', '호텔 객실 정리 알바', '인천 파라다이스 호텔', 'incheon', '인천', '2', '중구',
 '인천 중구 영종해안북로321번길 186', 'Accommodation', 'Part-time', 2200000,
 '5 mornings per week', 35, 37.4913, 126.5091),

('short-incheon-03', '편의점 야간 알바', 'CU 부평역점', 'incheon', '인천', '2', '부평구',
 '인천 부평구 부평대로 168', 'Store', 'Part-time', 2200000,
 '3 nights per week', 15, 37.4906, 126.7222),

('short-incheon-04', '차이나타운 음식점 홀 서빙', '공화춘 차이나타운점', 'incheon', '인천', '2', '중구',
 '인천 중구 차이나타운로 33', 'Food Service', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 25, 37.4757, 126.6171),

('short-incheon-05', '물류센터 입출고 보조', '인천항 스마트물류센터', 'incheon', '인천', '2', '연수구',
 '인천 연수구 컨벤시아대로 165', 'Logistics', 'Short contract', 2500000,
 'Mon-Fri 08:00-17:00', 20, 37.3885, 126.6454),

('short-incheon-06', '관광 안내 스태프', '송도 관광안내소', 'incheon', '인천', '2', '연수구',
 '인천 연수구 아트센터대로 175', 'Tourism', 'Part-time', 2096270,
 'Tue-Sat 09:00-18:00', 20, 37.3883, 126.6459),

('short-incheon-07', '카페 바리스타 알바', '을왕리 해변 카페', 'incheon', '인천', '2', '중구',
 '인천 중구 을왕동 을왕리해변로 1', 'Food and Beverage', 'Part-time', 2200000,
 'Wed-Sun 09:00-18:00', 40, 37.4460, 126.3930),

('short-incheon-08', '행사 진행 보조', '인천 아트플랫폼', 'incheon', '인천', '2', '중구',
 '인천 중구 제물량로218번길 3', 'Event', 'Short contract', 2400000,
 'Fri-Sun 12:00-21:00', 22, 37.4745, 126.6215),

('short-incheon-09', '마트 판매 보조', '롯데마트 부평점', 'incheon', '인천', '2', '부평구',
 '인천 부평구 부평문화로 61', 'Shopping', 'Part-time', 2096270,
 'Thu-Mon 11:00-20:00', 18, 37.4887, 126.7215),

('short-incheon-10', '음식점 주방 보조', '신포국제시장 닭강정', 'incheon', '인천', '2', '중구',
 '인천 중구 신포로23번길 10', 'Food Service', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 20, 37.4743, 126.6239),

-- ========== 광주 (gwangju, region_id='5') ==========
('short-gwangju-01', '카페 서빙 알바', '양림동 카페거리 드립 카페', 'gwangju', '광주', '5', '남구',
 '광주 남구 천변좌로 338', 'Food and Beverage', 'Part-time', 2096270,
 'Tue-Sat 09:00-18:00', 18, 35.1404, 126.9096),

('short-gwangju-02', '게스트하우스 청소 알바', '동구 한옥 스테이', 'gwangju', '광주', '5', '동구',
 '광주 동구 충장로 104', 'Accommodation', 'Part-time', 2096270,
 '5 mornings per week', 20, 35.1462, 126.9210),

('short-gwangju-03', '편의점 야간 알바', 'GS25 상무점', 'gwangju', '광주', '5', '서구',
 '광주 서구 상무대로 916', 'Store', 'Part-time', 2200000,
 '3 nights per week', 15, 35.1540, 126.8531),

('short-gwangju-04', '음식점 홀 서빙', '남도한정식 골목집', 'gwangju', '광주', '5', '동구',
 '광주 동구 금남로 5가 127', 'Food Service', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 20, 35.1468, 126.9200),

('short-gwangju-05', '문화행사 보조', '국립아시아문화전당 운영팀', 'gwangju', '광주', '5', '동구',
 '광주 동구 문화전당로 38', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 22, 35.1470, 126.9159),

('short-gwangju-06', '마트 판매 보조', '이마트 광주점', 'gwangju', '광주', '5', '서구',
 '광주 서구 화운로 99', 'Shopping', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 25, 35.1499, 126.8632),

('short-gwangju-07', '물류 분류 알바', '광주 택배 허브센터', 'gwangju', '광주', '5', '광산구',
 '광주 광산구 산정동 809', 'Logistics', 'Short contract', 2400000,
 '5 mornings per week', 30, 35.1861, 126.8015),

('short-gwangju-08', '관광 안내 도우미', '광주관광안내소', 'gwangju', '광주', '5', '동구',
 '광주 동구 제봉로 104', 'Tourism', 'Part-time', 2096270,
 'Mon-Fri 10:00-17:00', 18, 35.1472, 126.9211),

('short-gwangju-09', '카페 바리스타 보조', '광주 1913 송정역시장 카페', 'gwangju', '광주', '5', '광산구',
 '광주 광산구 송정공원로8번길 13', 'Food and Beverage', 'Part-time', 2200000,
 'Wed-Sun 09:00-18:00', 22, 35.1380, 126.7938),

('short-gwangju-10', '로컬푸드 판매 알바', '광주 로컬푸드직매장', 'gwangju', '광주', '5', '북구',
 '광주 북구 운암동 611', 'Shopping', 'Part-time', 2096270,
 'Mon-Fri 08:00-17:00', 20, 35.1750, 126.9060),

-- ========== 대전 (daejeon, region_id='3') ==========
('short-daejeon-01', '카페 홀 알바', '성심당 대전역점 카페', 'daejeon', '대전', '3', '동구',
 '대전 동구 중앙로 215', 'Food and Beverage', 'Part-time', 2096270,
 'Mon-Fri 10:00-17:00', 15, 36.3351, 127.4349),

('short-daejeon-02', '게스트하우스 프론트 알바', '유성 온천 게스트하우스', 'daejeon', '대전', '3', '유성구',
 '대전 유성구 온천로 55', 'Accommodation', 'Part-time', 2096270,
 'Tue-Sat 09:00-18:00', 20, 36.3622, 127.3387),

('short-daejeon-03', '편의점 야간 스태프', 'CU 둔산점', 'daejeon', '대전', '3', '서구',
 '대전 서구 둔산로 100', 'Store', 'Part-time', 2200000,
 '3 nights per week', 12, 36.3516, 127.3869),

('short-daejeon-04', '분식집 홀 서빙', '중앙시장 순대골목집', 'daejeon', '대전', '3', '중구',
 '대전 중구 중앙로 166', 'Food Service', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 18, 36.3287, 127.4275),

('short-daejeon-05', '과학 행사 보조', '대전엑스포과학공원', 'daejeon', '대전', '3', '유성구',
 '대전 유성구 대덕대로 480', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 25, 36.3754, 127.3839),

('short-daejeon-06', '마트 판매 스태프', '롯데마트 대전터미널점', 'daejeon', '대전', '3', '동구',
 '대전 동구 중앙로 215', 'Shopping', 'Part-time', 2096270,
 'Thu-Mon 11:00-20:00', 18, 36.3348, 127.4344),

('short-daejeon-07', '물류 분류 보조', '한진 대전허브터미널', 'daejeon', '대전', '3', '대덕구',
 '대전 대덕구 회덕동 357', 'Logistics', 'Short contract', 2400000,
 '5 mornings per week', 30, 36.3871, 127.4204),

('short-daejeon-08', '관광 안내 알바', '대전시 관광안내소', 'daejeon', '대전', '3', '중구',
 '대전 중구 대종로 480', 'Tourism', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 20, 36.3290, 127.4283),

('short-daejeon-09', '카페 바리스타 알바', '성심당 케이크부티크', 'daejeon', '대전', '3', '중구',
 '대전 중구 대종로 480', 'Food and Beverage', 'Part-time', 2200000,
 'Wed-Sun 09:00-18:00', 15, 36.3289, 127.4277),

('short-daejeon-10', '음식점 배달 보조', '유성 해물탕 음식점', 'daejeon', '대전', '3', '유성구',
 '대전 유성구 계룡로 140', 'Food Service', 'Part-time', 2096270,
 'Mon-Fri 10:00-17:00', 20, 36.3591, 127.3445),

-- ========== 제주 (jeju, region_id='39') ==========
('short-jeju-01', '카페 홀 서빙 알바', '애월 오션뷰 카페', 'jeju', '제주', '39', '제주시',
 '제주 제주시 애월읍 일주서로 6726', 'Food and Beverage', 'Part-time', 2200000,
 'Tue-Sat 09:00-18:00', 25, 33.4622, 126.3218),

('short-jeju-02', '게스트하우스 안내·청소', '함덕 바다뷰 게스트하우스', 'jeju', '제주', '39', '제주시',
 '제주 제주시 조천읍 함덕리 168', 'Accommodation', 'Part-time', 2096270,
 '5 mornings per week', 30, 33.5435, 126.6706),

('short-jeju-03', '편의점 주간 알바', 'CU 제주공항점', 'jeju', '제주', '39', '제주시',
 '제주 제주시 공항로 2', 'Store', 'Part-time', 2200000,
 'Mon-Fri 08:00-17:00', 15, 33.5107, 126.5219),

('short-jeju-04', '해산물 음식점 홀 서빙', '성산 해녀의집', 'jeju', '제주', '39', '서귀포시',
 '제주 서귀포시 성산읍 일출로 284', 'Food Service', 'Part-time', 2200000,
 'Wed-Sun 09:00-18:00', 40, 33.4580, 126.9419),

('short-jeju-05', '관광 행사 스태프', '제주관광공사 이벤트팀', 'jeju', '제주', '39', '제주시',
 '제주 제주시 연삼로 95', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 20, 33.4994, 126.5292),

('short-jeju-06', '렌터카 차량 정비 보조', '제주 렌터카 서비스센터', 'jeju', '제주', '39', '제주시',
 '제주 제주시 도령로 51', 'Logistics', 'Short contract', 2400000,
 'Mon-Fri 08:00-17:00', 18, 33.5013, 126.5114),

('short-jeju-07', '관광지 안내 알바', '성산일출봉 관광안내소', 'jeju', '제주', '39', '서귀포시',
 '제주 서귀포시 성산읍 일출로 284', 'Tourism', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 45, 33.4580, 126.9419),

('short-jeju-08', '마트 판매 보조', '이마트 제주점', 'jeju', '제주', '39', '제주시',
 '제주 제주시 남광로 153', 'Shopping', 'Part-time', 2096270,
 'Thu-Mon 11:00-20:00', 18, 33.5068, 126.5246),

('short-jeju-09', '카페 바리스타 알바', '서귀포 이중섭거리 카페', 'jeju', '제주', '39', '서귀포시',
 '제주 서귀포시 이중섭로 29', 'Food and Beverage', 'Part-time', 2200000,
 'Wed-Sun 09:00-18:00', 35, 33.2499, 126.5635),

('short-jeju-10', '관광농원 체험 보조', '제주 감귤농원 체험센터', 'jeju', '제주', '39', '서귀포시',
 '제주 서귀포시 남원읍 신흥로 53', 'Tourism', 'Short contract', 2096270,
 'Mon-Sat 09:00-18:00', 30, 33.3100, 126.7036),

-- ========== 강릉 (gangneung, region_id='32') ==========
('short-gangneung-01', '카페 홀 알바', '강릉 커피거리 바리스타 카페', 'gangneung', '강릉', '32', '강릉시',
 '강원 강릉시 초당순두부길 28', 'Food and Beverage', 'Part-time', 2096270,
 'Tue-Sat 09:00-18:00', 15, 37.7739, 128.9131),

('short-gangneung-02', '게스트하우스 청소 알바', '경포대 해변 게스트하우스', 'gangneung', '강릉', '32', '강릉시',
 '강원 강릉시 경포로 289', 'Accommodation', 'Part-time', 2096270,
 '5 mornings per week', 20, 37.7974, 128.9006),

('short-gangneung-03', '편의점 주간 알바', 'GS25 강릉터미널점', 'gangneung', '강릉', '32', '강릉시',
 '강원 강릉시 용지로 72', 'Store', 'Part-time', 2096270,
 'Mon-Fri 08:00-17:00', 12, 37.7498, 128.9019),

('short-gangneung-04', '초당순두부 식당 홀 서빙', '원조 초당 할머니순두부', 'gangneung', '강릉', '32', '강릉시',
 '강원 강릉시 초당순두부길 99', 'Food Service', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 18, 37.7752, 128.9145),

('short-gangneung-05', '해변 행사 스태프', '강릉 커피축제 운영팀', 'gangneung', '강릉', '32', '강릉시',
 '강원 강릉시 해안로 406', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 22, 37.7880, 128.9110),

('short-gangneung-06', '스키장 리프트 보조', '하이원 스키장', 'gangneung', '강릉', '32', '강릉시',
 '강원 강릉시 성산면 대기리 산 221', 'Event', 'Short contract', 2400000,
 'Mon-Fri 08:00-17:00', 45, 37.6474, 128.7459),

('short-gangneung-07', '관광 안내 알바', '강릉시 관광안내소', 'gangneung', '강릉', '32', '강릉시',
 '강원 강릉시 경강로 2045', 'Tourism', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 15, 37.7516, 128.9018),

('short-gangneung-08', '수산물 판매 보조', '주문진 수산시장', 'gangneung', '강릉', '32', '강릉시',
 '강원 강릉시 주문진읍 주문로 228', 'Shopping', 'Part-time', 2096270,
 'Wed-Sun 09:00-18:00', 30, 37.8985, 128.8172),

('short-gangneung-09', '카페 바리스타 알바', '안목 해변 오션뷰 카페', 'gangneung', '강릉', '32', '강릉시',
 '강원 강릉시 창해로 307', 'Food and Beverage', 'Part-time', 2200000,
 'Thu-Mon 11:00-20:00', 18, 37.7686, 128.9460),

('short-gangneung-10', '물류 분류 보조', 'CJ대한통운 강릉지점', 'gangneung', '강릉', '32', '강릉시',
 '강원 강릉시 남부로 126', 'Logistics', 'Short contract', 2300000,
 '5 mornings per week', 20, 37.7473, 128.8975),

-- ========== 전주 (jeonju, region_id='37') ==========
('short-jeonju-01', '한옥마을 카페 알바', '전주 한옥 카페 고향', 'jeonju', '전주', '37', '완산구',
 '전북 전주시 완산구 태조로 15', 'Food and Beverage', 'Part-time', 2096270,
 'Tue-Sat 09:00-18:00', 15, 35.8155, 127.1531),

('short-jeonju-02', '한옥 게스트하우스 청소 알바', '전주 한옥스테이 달빛', 'jeonju', '전주', '37', '완산구',
 '전북 전주시 완산구 기린대로 99', 'Accommodation', 'Part-time', 2096270,
 '5 mornings per week', 18, 35.8178, 127.1512),

('short-jeonju-03', '편의점 야간 알바', 'CU 전주역점', 'jeonju', '전주', '37', '덕진구',
 '전북 전주시 덕진구 백제대로 570', 'Store', 'Part-time', 2096270,
 '3 nights per week', 20, 35.8427, 127.1492),

('short-jeonju-04', '비빔밥 음식점 홀 서빙', '전주 가족회관 비빔밥', 'jeonju', '전주', '37', '완산구',
 '전북 전주시 완산구 어진길 29', 'Food Service', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 18, 35.8151, 127.1528),

('short-jeonju-05', '문화행사 보조', '전주국제영화제 사무국', 'jeonju', '전주', '37', '완산구',
 '전북 전주시 완산구 전주객사3길 22', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 20, 35.8196, 127.1499),

('short-jeonju-06', '공예품 판매 보조', '전주 공예품전시관', 'jeonju', '전주', '37', '완산구',
 '전북 전주시 완산구 태조로 10', 'Shopping', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 15, 35.8153, 127.1532),

('short-jeonju-07', '물류 분류 알바', '전주 스마트 물류센터', 'jeonju', '전주', '37', '덕진구',
 '전북 전주시 덕진구 반룡로 99', 'Logistics', 'Short contract', 2300000,
 '5 mornings per week', 30, 35.8710, 127.1280),

('short-jeonju-08', '관광 안내 알바', '전주관광안내소', 'jeonju', '전주', '37', '완산구',
 '전북 전주시 완산구 태조로 18', 'Tourism', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 15, 35.8157, 127.1529),

('short-jeonju-09', '카페 바리스타 알바', '경기전 뒷길 카페', 'jeonju', '전주', '37', '완산구',
 '전북 전주시 완산구 태조로 44', 'Food and Beverage', 'Part-time', 2200000,
 'Wed-Sun 09:00-18:00', 16, 35.8173, 127.1534),

('short-jeonju-10', '전통주 양조장 보조', '전주 전통주 체험관', 'jeonju', '전주', '37', '완산구',
 '전북 전주시 완산구 전주객사3길 39', 'Food Service', 'Part-time', 2096270,
 'Mon-Fri 10:00-17:00', 18, 35.8198, 127.1497),

-- ========== 경주 (gyeongju, region_id='35') ==========
('short-gyeongju-01', '카페 홀 알바', '황리단길 오션뷰 카페', 'gyeongju', '경주', '35', '경주시',
 '경북 경주시 포석로 1054', 'Food and Beverage', 'Part-time', 2096270,
 'Tue-Sat 09:00-18:00', 15, 35.8327, 129.2213),

('short-gyeongju-02', '한옥 게스트하우스 안내 알바', '경주 고도 한옥스테이', 'gyeongju', '경주', '35', '경주시',
 '경북 경주시 교동 17', 'Accommodation', 'Part-time', 2096270,
 '5 mornings per week', 18, 35.8378, 129.2157),

('short-gyeongju-03', '편의점 야간 알바', 'GS25 경주역점', 'gyeongju', '경주', '35', '경주시',
 '경북 경주시 태종로 780', 'Store', 'Part-time', 2096270,
 '3 nights per week', 12, 35.8562, 129.2247),

('short-gyeongju-04', '전통 음식점 홀 서빙', '경주 교동법주 음식점', 'gyeongju', '경주', '35', '경주시',
 '경북 경주시 교동 34', 'Food Service', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 18, 35.8382, 129.2148),

('short-gyeongju-05', '문화재 행사 도우미', '경주엑스포 운영팀', 'gyeongju', '경주', '35', '경주시',
 '경북 경주시 경감로 614', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 22, 35.8466, 129.2434),

('short-gyeongju-06', '기념품 판매 보조', '불국사 기념품샵', 'gyeongju', '경주', '35', '경주시',
 '경북 경주시 불국로 385', 'Shopping', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 25, 35.7903, 129.3318),

('short-gyeongju-07', '관광 안내 알바', '경주시 관광안내소', 'gyeongju', '경주', '35', '경주시',
 '경북 경주시 태종로 741', 'Tourism', 'Part-time', 2096270,
 'Mon-Fri 10:00-17:00', 15, 35.8556, 129.2240),

('short-gyeongju-08', '야간 관광지 포토존 운영', '첨성대 야간 조명 행사팀', 'gyeongju', '경주', '35', '경주시',
 '경북 경주시 인왕동 839-1', 'Event', 'Part-time', 2200000,
 '3 nights per week', 18, 35.8350, 129.2193),

('short-gyeongju-09', '카페 바리스타 알바', '황리단길 루프탑 카페', 'gyeongju', '경주', '35', '경주시',
 '경북 경주시 포석로 1068', 'Food and Beverage', 'Part-time', 2200000,
 'Wed-Sun 09:00-18:00', 16, 35.8323, 129.2218),

('short-gyeongju-10', '물류 분류 보조', '경주 통합물류센터', 'gyeongju', '경주', '35', '경주시',
 '경북 경주시 외동읍 서라벌대로 2655', 'Logistics', 'Short contract', 2300000,
 '5 mornings per week', 30, 35.8012, 129.3248),

-- ========== 여수 (yeosu, region_id='38') ==========
('short-yeosu-01', '카페 홀 알바', '여수 낭만포차 거리 카페', 'yeosu', '여수', '38', '여수시',
 '전남 여수시 낭만포차길 10', 'Food and Beverage', 'Part-time', 2096270,
 'Tue-Sat 09:00-18:00', 15, 34.7489, 127.7426),

('short-yeosu-02', '게스트하우스 안내 알바', '돌산 오션뷰 게스트하우스', 'yeosu', '여수', '38', '여수시',
 '전남 여수시 돌산읍 돌산로 77', 'Accommodation', 'Part-time', 2096270,
 '5 mornings per week', 25, 34.7095, 127.7540),

('short-yeosu-03', '편의점 야간 알바', 'CU 여수엑스포점', 'yeosu', '여수', '38', '여수시',
 '전남 여수시 오동도로 1', 'Store', 'Part-time', 2096270,
 '3 nights per week', 12, 34.7426, 127.7405),

('short-yeosu-04', '해산물 음식점 홀 서빙', '여수 갓김치 해물탕', 'yeosu', '여수', '38', '여수시',
 '전남 여수시 오동도로 24', 'Food Service', 'Part-time', 2200000,
 'Wed-Sun 09:00-18:00', 18, 34.7420, 127.7411),

('short-yeosu-05', '여수엑스포 행사 도우미', '여수세계박람회장 운영팀', 'yeosu', '여수', '38', '여수시',
 '전남 여수시 박람회길 1', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 20, 34.7421, 127.7320),

('short-yeosu-06', '수산물 판매 보조', '여수수산시장', 'yeosu', '여수', '38', '여수시',
 '전남 여수시 수산시장길 4', 'Shopping', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 15, 34.7487, 127.7382),

('short-yeosu-07', '관광지 안내 알바', '여수관광안내소', 'yeosu', '여수', '38', '여수시',
 '전남 여수시 오동도로 1', 'Tourism', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 12, 34.7431, 127.7410),

('short-yeosu-08', '물류 분류 보조', '여수 항만물류센터', 'yeosu', '여수', '38', '여수시',
 '전남 여수시 항만로 32', 'Logistics', 'Short contract', 2400000,
 '5 mornings per week', 25, 34.7550, 127.7610),

('short-yeosu-09', '카페 바리스타 알바', '오동도 앞 오션카페', 'yeosu', '여수', '38', '여수시',
 '전남 여수시 오동도로 10', 'Food and Beverage', 'Part-time', 2200000,
 'Thu-Mon 11:00-20:00', 15, 34.7440, 127.7418),

('short-yeosu-10', '야경 포토존 운영 알바', '하멜등대 야간 포토존', 'yeosu', '여수', '38', '여수시',
 '전남 여수시 하멜로 2', 'Event', 'Part-time', 2096270,
 '3 nights per week', 18, 34.7504, 127.7357),

-- ========== 속초 (sokcho, region_id='32') ==========
('short-sokcho-01', '카페 홀 알바', '속초 아바이마을 카페', 'sokcho', '속초', '32', '속초시',
 '강원 속초시 아바이마을길 12', 'Food and Beverage', 'Part-time', 2096270,
 'Tue-Sat 09:00-18:00', 15, 38.1953, 128.5812),

('short-sokcho-02', '게스트하우스 청소 알바', '속초 해변 게스트하우스', 'sokcho', '속초', '32', '속초시',
 '강원 속초시 영금정로 12', 'Accommodation', 'Part-time', 2096270,
 '5 mornings per week', 18, 38.2050, 128.5920),

('short-sokcho-03', '편의점 야간 알바', 'GS25 속초시외버스터미널점', 'sokcho', '속초', '32', '속초시',
 '강원 속초시 도청로 97', 'Store', 'Part-time', 2096270,
 '3 nights per week', 12, 38.2019, 128.5896),

('short-sokcho-04', '막국수 식당 홀 서빙', '속초 원조 막국수집', 'sokcho', '속초', '32', '속초시',
 '강원 속초시 번영로 55', 'Food Service', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 18, 38.2073, 128.5913),

('short-sokcho-05', '해변 행사 스태프', '속초 해수욕장 페스티벌', 'sokcho', '속초', '32', '속초시',
 '강원 속초시 해오름로 56', 'Event', 'Short contract', 2500000,
 'Fri-Sun 12:00-21:00', 20, 38.2098, 128.5977),

('short-sokcho-06', '수산물 판매 보조', '속초 중앙시장 수산코너', 'sokcho', '속초', '32', '속초시',
 '강원 속초시 중앙로 147', 'Shopping', 'Part-time', 2096270,
 'Mon-Sat 09:00-18:00', 15, 38.2038, 128.5927),

('short-sokcho-07', '관광 안내 알바', '속초관광안내소', 'sokcho', '속초', '32', '속초시',
 '강원 속초시 도청로 97', 'Tourism', 'Part-time', 2096270,
 'Tue-Sun 10:00-19:00', 12, 38.2023, 128.5899),

('short-sokcho-08', '스키장 리프트 안전 요원', '설악 워터피아 스키장', 'sokcho', '속초', '32', '속초시',
 '강원 속초시 한계령로 1084', 'Event', 'Short contract', 2400000,
 'Mon-Fri 08:00-17:00', 40, 38.1213, 128.4721),

('short-sokcho-09', '카페 바리스타 알바', '청초호 오션뷰 카페', 'sokcho', '속초', '32', '속초시',
 '강원 속초시 청초호반로 77', 'Food and Beverage', 'Part-time', 2200000,
 'Wed-Sun 09:00-18:00', 15, 38.1997, 128.5841),

('short-sokcho-10', '물류 분류 보조', '속초 통합물류센터', 'sokcho', '속초', '32', '속초시',
 '강원 속초시 조양동 산44', 'Logistics', 'Short contract', 2300000,
 '5 mornings per week', 22, 38.2107, 128.5787)

ON DUPLICATE KEY UPDATE
  `title`          = VALUES(`title`),
  `company`        = VALUES(`company`),
  `monthly_salary` = VALUES(`monthly_salary`),
  `working_days`   = VALUES(`working_days`);

-- ──────────────────────────────────────
-- Tags  (job_id → tag)
-- ──────────────────────────────────────

DELETE FROM `job_tags` WHERE `job_id` LIKE 'short-%';

INSERT INTO `job_tags` (`job_id`, `tag`) VALUES
-- seoul
('short-seoul-01','cafe'),('short-seoul-01','beginner'),('short-seoul-01','day-shift'),
('short-seoul-02','stay'),('short-seoul-02','guest-service'),('short-seoul-02','language-plus'),
('short-seoul-03','store'),('short-seoul-03','night'),('short-seoul-03','short-term'),
('short-seoul-04','local-food'),('short-seoul-04','meal-support'),('short-seoul-04','service'),
('short-seoul-05','event'),('short-seoul-05','team-work'),('short-seoul-05','weekend'),
('short-seoul-06','sales'),('short-seoul-06','standing'),('short-seoul-06','short-term'),
('short-seoul-07','cafe'),('short-seoul-07','morning'),('short-seoul-07','meal-support'),
('short-seoul-08','logistics'),('short-seoul-08','morning'),('short-seoul-08','standing'),
('short-seoul-09','tourism'),('short-seoul-09','language-plus'),('short-seoul-09','service'),
('short-seoul-10','sales'),('short-seoul-10','standing'),('short-seoul-10','service'),
-- busan
('short-busan-01','cafe'),('short-busan-01','beginner'),('short-busan-01','day-shift'),
('short-busan-02','stay'),('short-busan-02','cleaning'),('short-busan-02','morning'),
('short-busan-03','store'),('short-busan-03','day-shift'),('short-busan-03','short-term'),
('short-busan-04','local-food'),('short-busan-04','service'),('short-busan-04','weekend'),
('short-busan-05','cafe'),('short-busan-05','day-shift'),('short-busan-05','weekend'),
('short-busan-06','logistics'),('short-busan-06','morning'),('short-busan-06','driver-license-plus'),
('short-busan-07','event'),('short-busan-07','team-work'),('short-busan-07','weekend'),
('short-busan-08','sales'),('short-busan-08','standing'),('short-busan-08','service'),
('short-busan-09','tourism'),('short-busan-09','language-plus'),('short-busan-09','short-term'),
('short-busan-10','local-food'),('short-busan-10','beginner'),('short-busan-10','meal-support'),
-- daegu
('short-daegu-01','cafe'),('short-daegu-01','beginner'),('short-daegu-01','day-shift'),
('short-daegu-02','stay'),('short-daegu-02','desk'),('short-daegu-02','guest-service'),
('short-daegu-03','store'),('short-daegu-03','night'),('short-daegu-03','short-term'),
('short-daegu-04','local-food'),('short-daegu-04','service'),('short-daegu-04','meal-support'),
('short-daegu-05','event'),('short-daegu-05','team-work'),('short-daegu-05','weekend'),
('short-daegu-06','sales'),('short-daegu-06','standing'),('short-daegu-06','short-term'),
('short-daegu-07','logistics'),('short-daegu-07','morning'),('short-daegu-07','standing'),
('short-daegu-08','tourism'),('short-daegu-08','service'),('short-daegu-08','language-plus'),
('short-daegu-09','cafe'),('short-daegu-09','beginner'),('short-daegu-09','short-term'),
('short-daegu-10','cafe'),('short-daegu-10','day-shift'),('short-daegu-10','weekend'),
-- incheon
('short-incheon-01','cafe'),('short-incheon-01','morning'),('short-incheon-01','language-plus'),
('short-incheon-02','stay'),('short-incheon-02','hotel'),('short-incheon-02','cleaning'),
('short-incheon-03','store'),('short-incheon-03','night'),('short-incheon-03','short-term'),
('short-incheon-04','local-food'),('short-incheon-04','service'),('short-incheon-04','tourism'),
('short-incheon-05','logistics'),('short-incheon-05','morning'),('short-incheon-05','standing'),
('short-incheon-06','tourism'),('short-incheon-06','language-plus'),('short-incheon-06','service'),
('short-incheon-07','cafe'),('short-incheon-07','day-shift'),('short-incheon-07','beginner'),
('short-incheon-08','event'),('short-incheon-08','team-work'),('short-incheon-08','weekend'),
('short-incheon-09','sales'),('short-incheon-09','standing'),('short-incheon-09','service'),
('short-incheon-10','local-food'),('short-incheon-10','beginner'),('short-incheon-10','short-term'),
-- gwangju
('short-gwangju-01','cafe'),('short-gwangju-01','beginner'),('short-gwangju-01','day-shift'),
('short-gwangju-02','stay'),('short-gwangju-02','cleaning'),('short-gwangju-02','morning'),
('short-gwangju-03','store'),('short-gwangju-03','night'),('short-gwangju-03','short-term'),
('short-gwangju-04','local-food'),('short-gwangju-04','meal-support'),('short-gwangju-04','service'),
('short-gwangju-05','event'),('short-gwangju-05','team-work'),('short-gwangju-05','tourism'),
('short-gwangju-06','sales'),('short-gwangju-06','standing'),('short-gwangju-06','service'),
('short-gwangju-07','logistics'),('short-gwangju-07','morning'),('short-gwangju-07','standing'),
('short-gwangju-08','tourism'),('short-gwangju-08','language-plus'),('short-gwangju-08','service'),
('short-gwangju-09','cafe'),('short-gwangju-09','day-shift'),('short-gwangju-09','short-term'),
('short-gwangju-10','sales'),('short-gwangju-10','local-food'),('short-gwangju-10','morning'),
-- daejeon
('short-daejeon-01','cafe'),('short-daejeon-01','beginner'),('short-daejeon-01','day-shift'),
('short-daejeon-02','stay'),('short-daejeon-02','guest-service'),('short-daejeon-02','morning'),
('short-daejeon-03','store'),('short-daejeon-03','night'),('short-daejeon-03','short-term'),
('short-daejeon-04','local-food'),('short-daejeon-04','service'),('short-daejeon-04','meal-support'),
('short-daejeon-05','event'),('short-daejeon-05','team-work'),('short-daejeon-05','weekend'),
('short-daejeon-06','sales'),('short-daejeon-06','standing'),('short-daejeon-06','service'),
('short-daejeon-07','logistics'),('short-daejeon-07','morning'),('short-daejeon-07','driver-license-plus'),
('short-daejeon-08','tourism'),('short-daejeon-08','language-plus'),('short-daejeon-08','service'),
('short-daejeon-09','cafe'),('short-daejeon-09','day-shift'),('short-daejeon-09','short-term'),
('short-daejeon-10','local-food'),('short-daejeon-10','beginner'),('short-daejeon-10','service'),
-- jeju
('short-jeju-01','cafe'),('short-jeju-01','day-shift'),('short-jeju-01','beginner'),
('short-jeju-02','stay'),('short-jeju-02','cleaning'),('short-jeju-02','morning'),
('short-jeju-03','store'),('short-jeju-03','morning'),('short-jeju-03','short-term'),
('short-jeju-04','local-food'),('short-jeju-04','service'),('short-jeju-04','tourism'),
('short-jeju-05','event'),('short-jeju-05','team-work'),('short-jeju-05','tourism'),
('short-jeju-06','logistics'),('short-jeju-06','driver-license-plus'),('short-jeju-06','morning'),
('short-jeju-07','tourism'),('short-jeju-07','language-plus'),('short-jeju-07','service'),
('short-jeju-08','sales'),('short-jeju-08','standing'),('short-jeju-08','service'),
('short-jeju-09','cafe'),('short-jeju-09','day-shift'),('short-jeju-09','short-term'),
('short-jeju-10','tourism'),('short-jeju-10','local-food'),('short-jeju-10','beginner'),
-- gangneung
('short-gangneung-01','cafe'),('short-gangneung-01','beginner'),('short-gangneung-01','day-shift'),
('short-gangneung-02','stay'),('short-gangneung-02','cleaning'),('short-gangneung-02','morning'),
('short-gangneung-03','store'),('short-gangneung-03','morning'),('short-gangneung-03','short-term'),
('short-gangneung-04','local-food'),('short-gangneung-04','meal-support'),('short-gangneung-04','service'),
('short-gangneung-05','event'),('short-gangneung-05','team-work'),('short-gangneung-05','weekend'),
('short-gangneung-06','event'),('short-gangneung-06','standing'),('short-gangneung-06','short-term'),
('short-gangneung-07','tourism'),('short-gangneung-07','language-plus'),('short-gangneung-07','service'),
('short-gangneung-08','sales'),('short-gangneung-08','local-food'),('short-gangneung-08','morning'),
('short-gangneung-09','cafe'),('short-gangneung-09','day-shift'),('short-gangneung-09','weekend'),
('short-gangneung-10','logistics'),('short-gangneung-10','morning'),('short-gangneung-10','standing'),
-- jeonju
('short-jeonju-01','cafe'),('short-jeonju-01','beginner'),('short-jeonju-01','day-shift'),
('short-jeonju-02','stay'),('short-jeonju-02','cleaning'),('short-jeonju-02','morning'),
('short-jeonju-03','store'),('short-jeonju-03','night'),('short-jeonju-03','short-term'),
('short-jeonju-04','local-food'),('short-jeonju-04','meal-support'),('short-jeonju-04','service'),
('short-jeonju-05','event'),('short-jeonju-05','team-work'),('short-jeonju-05','tourism'),
('short-jeonju-06','sales'),('short-jeonju-06','tourism'),('short-jeonju-06','service'),
('short-jeonju-07','logistics'),('short-jeonju-07','morning'),('short-jeonju-07','standing'),
('short-jeonju-08','tourism'),('short-jeonju-08','language-plus'),('short-jeonju-08','service'),
('short-jeonju-09','cafe'),('short-jeonju-09','day-shift'),('short-jeonju-09','short-term'),
('short-jeonju-10','local-food'),('short-jeonju-10','beginner'),('short-jeonju-10','service'),
-- gyeongju
('short-gyeongju-01','cafe'),('short-gyeongju-01','beginner'),('short-gyeongju-01','day-shift'),
('short-gyeongju-02','stay'),('short-gyeongju-02','guest-service'),('short-gyeongju-02','morning'),
('short-gyeongju-03','store'),('short-gyeongju-03','night'),('short-gyeongju-03','short-term'),
('short-gyeongju-04','local-food'),('short-gyeongju-04','meal-support'),('short-gyeongju-04','tourism'),
('short-gyeongju-05','event'),('short-gyeongju-05','team-work'),('short-gyeongju-05','tourism'),
('short-gyeongju-06','sales'),('short-gyeongju-06','tourism'),('short-gyeongju-06','service'),
('short-gyeongju-07','tourism'),('short-gyeongju-07','language-plus'),('short-gyeongju-07','service'),
('short-gyeongju-08','event'),('short-gyeongju-08','night'),('short-gyeongju-08','short-term'),
('short-gyeongju-09','cafe'),('short-gyeongju-09','day-shift'),('short-gyeongju-09','weekend'),
('short-gyeongju-10','logistics'),('short-gyeongju-10','morning'),('short-gyeongju-10','standing'),
-- yeosu
('short-yeosu-01','cafe'),('short-yeosu-01','beginner'),('short-yeosu-01','day-shift'),
('short-yeosu-02','stay'),('short-yeosu-02','cleaning'),('short-yeosu-02','morning'),
('short-yeosu-03','store'),('short-yeosu-03','night'),('short-yeosu-03','short-term'),
('short-yeosu-04','local-food'),('short-yeosu-04','service'),('short-yeosu-04','meal-support'),
('short-yeosu-05','event'),('short-yeosu-05','team-work'),('short-yeosu-05','tourism'),
('short-yeosu-06','sales'),('short-yeosu-06','local-food'),('short-yeosu-06','morning'),
('short-yeosu-07','tourism'),('short-yeosu-07','language-plus'),('short-yeosu-07','service'),
('short-yeosu-08','logistics'),('short-yeosu-08','morning'),('short-yeosu-08','driver-license-plus'),
('short-yeosu-09','cafe'),('short-yeosu-09','day-shift'),('short-yeosu-09','short-term'),
('short-yeosu-10','event'),('short-yeosu-10','night'),('short-yeosu-10','short-term'),
-- sokcho
('short-sokcho-01','cafe'),('short-sokcho-01','beginner'),('short-sokcho-01','day-shift'),
('short-sokcho-02','stay'),('short-sokcho-02','cleaning'),('short-sokcho-02','morning'),
('short-sokcho-03','store'),('short-sokcho-03','night'),('short-sokcho-03','short-term'),
('short-sokcho-04','local-food'),('short-sokcho-04','meal-support'),('short-sokcho-04','service'),
('short-sokcho-05','event'),('short-sokcho-05','team-work'),('short-sokcho-05','weekend'),
('short-sokcho-06','sales'),('short-sokcho-06','local-food'),('short-sokcho-06','morning'),
('short-sokcho-07','tourism'),('short-sokcho-07','language-plus'),('short-sokcho-07','service'),
('short-sokcho-08','event'),('short-sokcho-08','standing'),('short-sokcho-08','short-term'),
('short-sokcho-09','cafe'),('short-sokcho-09','day-shift'),('short-sokcho-09','weekend'),
('short-sokcho-10','logistics'),('short-sokcho-10','morning'),('short-sokcho-10','standing');
