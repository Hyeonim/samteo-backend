package com.samteo.global.config;

import com.samteo.domain.planner.entity.Accommodation;
import com.samteo.domain.planner.entity.Job;
import com.samteo.domain.planner.repository.AccommodationRepository;
import com.samteo.domain.planner.repository.JobRepository;
import com.samteo.domain.region.entity.Region;
import com.samteo.domain.region.repository.RegionRepository;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserLegacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RegionRepository regionRepository;
    private final JobRepository jobRepository;
    private final AccommodationRepository accommodationRepository;
    private final UserLegacyRepository userRepository;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            seedRegions();
            seedJobs();
            seedAccommodations();
            seedUsers();
        };
    }

    private void seedRegions() {
        if (regionRepository.count() > 0) return;
        regionRepository.saveAll(List.of(
            Region.builder().id("서울").name("서울").summary("대한민국 수도, 다양한 단기 알바 기회").hotPlaceScore(98).housingCostScore(35)
                .centerLatitude(new BigDecimal("37.5665")).centerLongitude(new BigDecimal("126.9780"))
                .tags(List.of("지하철", "관광", "카페")).recommendationReasons(List.of("다양한 알바", "교통 편리")).build(),
            Region.builder().id("부산").name("부산").summary("해양 도시, 리조트·수산물 알바").hotPlaceScore(92).housingCostScore(58)
                .centerLatitude(new BigDecimal("35.1796")).centerLongitude(new BigDecimal("129.0756"))
                .tags(List.of("해변", "리조트", "시장")).recommendationReasons(List.of("숙식 제공 많음", "관광 성수기")).build(),
            Region.builder().id("대구").name("대구").summary("동성로 중심 상권, 지하철 편리").hotPlaceScore(85).housingCostScore(65)
                .centerLatitude(new BigDecimal("35.8714")).centerLongitude(new BigDecimal("128.6014"))
                .tags(List.of("지하철", "상권", "카페")).recommendationReasons(List.of("생활비 저렴", "교통 편리")).build(),
            Region.builder().id("제주").name("제주").summary("관광 특수, 카페·농업 알바").hotPlaceScore(95).housingCostScore(52)
                .centerLatitude(new BigDecimal("33.4996")).centerLongitude(new BigDecimal("126.5312"))
                .tags(List.of("관광", "카페", "농업")).recommendationReasons(List.of("숙소 제공 많음", "자연 환경")).build(),
            Region.builder().id("강릉").name("강릉").summary("커피거리·해변 명소, 카페 알바 풍부").hotPlaceScore(88).housingCostScore(60)
                .centerLatitude(new BigDecimal("37.7519")).centerLongitude(new BigDecimal("128.8761"))
                .tags(List.of("카페", "해변", "리조트")).recommendationReasons(List.of("커피 문화", "관광지")).build(),
            Region.builder().id("전주").name("전주").summary("한옥마을 관광, 문화 알바").hotPlaceScore(87).housingCostScore(68)
                .centerLatitude(new BigDecimal("35.8242")).centerLongitude(new BigDecimal("127.1480"))
                .tags(List.of("한옥", "문화", "음식")).recommendationReasons(List.of("전통 문화", "저렴한 생활비")).build(),
            Region.builder().id("경주").name("경주").summary("유네스코 유산 도시, 관광 알바").hotPlaceScore(84).housingCostScore(70)
                .centerLatitude(new BigDecimal("35.8562")).centerLongitude(new BigDecimal("129.2247"))
                .tags(List.of("역사", "관광", "카페")).recommendationReasons(List.of("숙식 제공", "관광 특수")).build(),
            Region.builder().id("인천").name("인천").summary("공항·항구 근무, 글로벌 알바").hotPlaceScore(82).housingCostScore(55)
                .centerLatitude(new BigDecimal("37.4563")).centerLongitude(new BigDecimal("126.7052"))
                .tags(List.of("공항", "항구", "면세점")).recommendationReasons(List.of("고임금", "외국어 우대")).build(),
            Region.builder().id("여수").name("여수").summary("해양 관광, 엑스포·케이블카 알바").hotPlaceScore(90).housingCostScore(62)
                .centerLatitude(new BigDecimal("34.7604")).centerLongitude(new BigDecimal("127.6622"))
                .tags(List.of("해양", "관광", "야경")).recommendationReasons(List.of("숙식 제공", "관광 성수기")).build(),
            Region.builder().id("속초").name("속초").summary("설악산·해변, 리조트 알바").hotPlaceScore(86).housingCostScore(63)
                .centerLatitude(new BigDecimal("38.2070")).centerLongitude(new BigDecimal("128.5918"))
                .tags(List.of("산", "해변", "시장")).recommendationReasons(List.of("숙식 제공", "자연 환경")).build(),
            Region.builder().id("광주").name("광주").summary("문화 예술 도시, 도슨트·카페").hotPlaceScore(80).housingCostScore(66)
                .centerLatitude(new BigDecimal("35.1595")).centerLongitude(new BigDecimal("126.8526"))
                .tags(List.of("문화", "카페", "음식")).recommendationReasons(List.of("문화 알바", "저렴한 생활비")).build(),
            Region.builder().id("대전").name("대전").summary("과학 도시, 성심당·온천 알바").hotPlaceScore(78).housingCostScore(67)
                .centerLatitude(new BigDecimal("36.3504")).centerLongitude(new BigDecimal("127.3845"))
                .tags(List.of("과학", "베이커리", "온천")).recommendationReasons(List.of("KTX 교통", "저렴한 물가")).build()
        ));
    }

    private void seedJobs() {
        if (jobRepository.count() > 0) return;
        jobRepository.saveAll(List.of(
            // 서울
            job("job-1","홍대 대형 베이커리 카페","홍대입구역 도보 3분","서울","서울 마포구","카페","단기 알바",1700000,"주 5일",5,37.5563,126.9236,"☕",true,List.of("🚇 지하철 직결","⏰ 주 5일","💴 시급 10,030원")),
            job("job-2","강남 호텔 프런트 스태프","강남역 인근 비즈니스호텔","서울","서울 강남구","호텔","단기 알바",2200000,"주 5일",10,37.4979,127.0276,"🏨",false,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 11,500원")),
            job("job-3","이태원 다국어 관광안내","외국인 관광객 대상 안내","서울","서울 용산구","관광안내","단기 알바",1500000,"주 5일",8,37.5345,126.9944,"🗺️",false,List.of("🌍 영어 우대","⏰ 주 5일","💴 시급 10,500원")),
            job("job-30","성수동 편집샵 판매 스태프","성수동 핫플 편집샵","서울","서울 성동구","판매","단기 알바",1800000,"주 5일",7,37.5447,127.0567,"👗",false,List.of("🚇 지하철 직결","⏰ 주 5일","💴 시급 10,500원")),
            job("job-31","마포 물류센터 분류 스태프","마포구 소형 물류센터","서울","서울 마포구","물류","단기 알바",2000000,"주 5일",15,37.5596,126.9082,"📦",false,List.of("🌙 야간 가능","⏰ 주 5일","💴 시급 11,200원")),
            job("job-32","명동 뷰티샵 체험 스태프","명동 외국인 대상 K-뷰티","서울","서울 중구","판매","단기 알바",1650000,"주 5일",5,37.5631,126.9855,"💄",false,List.of("🌍 외국어 우대","⏰ 주 5일","💴 시급 10,200원")),
            // 부산
            job("job-4","해운대 리조트 스태프","해운대 특급 리조트","부산","부산 해운대구","리조트","단기 알바",2100000,"주 5일",15,35.1632,129.1601,"🏖️",true,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 11,000원")),
            job("job-5","광안리 서핑샵 스태프","서핑 장비 대여·강습 보조","부산","부산 수영구","레저","단기 알바",1400000,"주 6일",20,35.1531,129.1196,"🏄",false,List.of("🏄 경험자 우대","⏰ 주 6일","💴 시급 10,030원")),
            job("job-6","자갈치시장 수산물 판매","부산 대표 수산시장","부산","부산 중구","시장","단기 알바",1600000,"주 5일",10,35.0974,129.0303,"🦐",false,List.of("🐟 수산물 경험","⏰ 주 5일","💴 시급 10,030원")),
            job("job-33","감천문화마을 공방 스태프","부산 감천문화마을 내 공방","부산","부산 사하구","문화관광","단기 알바",1450000,"주 5일",20,35.0974,129.0108,"🎨",false,List.of("🎨 예술 관심","⏰ 주 5일","💴 시급 10,030원")),
            job("job-34","부산역 인근 게스트하우스","부산역 5분 거리 게스트하우스","부산","부산 동구","숙박","단기 알바",1700000,"주 5일",5,35.1143,129.0391,"🏠",false,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 10,030원")),
            job("job-35","기장 멸치 수산물 판매","기장 수산시장 새벽 근무","부산","부산 기장군","시장","단기 알바",1550000,"주 6일",35,35.2444,129.2193,"🐠",false,List.of("🐟 수산물","⏰ 주 6일","💴 시급 10,030원")),
            // 대구
            job("job-7","동성로 대형 베이커리 카페","시내버스 706번 환승 없음","대구","대구 중구","카페","단기 알바",1600000,"주 5일",22,35.8704,128.5927,"☕",true,List.of("🚌 환승 없음","⏰ 주 5일","💴 시급 10,030원")),
            job("job-8","팔공산 한옥스테이 관광안내","팔공산 케이블카 인근","대구","대구 동구","관광안내","단기 알바",2100000,"주 5일",45,35.9116,128.6632,"⛩️",false,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 11,000원")),
            job("job-9","수성못 역세권 편의점","지하철 3호선 환승 1회","대구","대구 수성구","편의점","단기 알바",800000,"주 2일",38,35.8562,128.6298,"🏪",false,List.of("🚇 환승 1회","⏰ 주 2일","💴 시급 10,030원")),
            job("job-36","반월당 비즈니스호텔 하우스키핑","반월당역 도보 3분","대구","대구 중구","호텔","단기 알바",1750000,"주 5일",8,35.8665,128.5943,"🛏️",false,List.of("🚇 지하철 직결","⏰ 주 5일","💴 시급 10,500원")),
            job("job-37","서문시장 전통음식 판매","대구 서문시장 납작만두·수제비","대구","대구 중구","시장","단기 알바",1400000,"주 6일",18,35.8697,128.5763,"🥟",false,List.of("🥟 음식 경험","⏰ 주 6일","💴 시급 10,030원")),
            // 제주
            job("job-10","애월 오션뷰 카페 스태프","제주 애월 해안도로 카페","제주","제주 애월읍","카페","단기 알바",1800000,"주 5일",30,33.4619,126.3235,"🌊",true,List.of("🏠 숙소제공","⏰ 주 5일","💴 시급 10,030원")),
            job("job-11","성산일출봉 관광안내 스태프","유네스코 세계유산 안내","제주","제주 서귀포시","관광안내","단기 알바",1900000,"주 5일",40,33.4584,126.9423,"🌋",false,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 11,000원")),
            job("job-12","한림공원 매표·안내","한림공원 입장권 판매 및 안내","제주","제주 한림읍","관광안내","단기 알바",1500000,"주 5일",35,33.4134,126.2599,"🌿",false,List.of("🎫 매표 경험","⏰ 주 5일","💴 시급 10,030원")),
            job("job-38","제주시 농장 귤 수확 알바","제주 한림 귤 농장","제주","제주 한림읍","농업","단기 알바",1600000,"주 6일",40,33.4141,126.2649,"🍊",false,List.of("🍊 귤 수확","⏰ 주 6일","💴 시급 10,500원")),
            job("job-39","중문 리조트 풀빌라 스태프","중문관광단지 풀빌라 리조트","제주","제주 서귀포시","리조트","단기 알바",2200000,"주 5일",20,33.2441,126.4122,"🏊",false,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 12,000원")),
            job("job-40","협재 해수욕장 수상레저","협재해변 수상 레저 대여·강습","제주","제주 한림읍","레저","단기 알바",1700000,"주 6일",35,33.3936,126.2393,"🚣",false,List.of("🏄 레저 경험","⏰ 주 6일","💴 시급 10,500원")),
            // 강릉
            job("job-13","안목해변 스페셜티 카페","강릉 커피거리 대표 카페","강릉","강릉 경포동","카페","단기 알바",1600000,"주 5일",15,37.7714,128.9457,"☕",true,List.of("☕ 바리스타","⏰ 주 5일","💴 시급 10,030원")),
            job("job-14","경포대 리조트 스태프","경포 해변 앞 리조트 숙식 제공","강릉","강릉 경포동","리조트","단기 알바",2000000,"주 5일",20,37.7978,128.9066,"🏖️",false,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 10,800원")),
            job("job-41","강릉중앙시장 초당순두부 가게","강릉 초당순두부 음식점","강릉","강릉 포남동","시장","단기 알바",1350000,"주 6일",10,37.7506,128.8986,"🍲",false,List.of("🥣 요리 경험","⏰ 주 6일","💴 시급 10,030원")),
            job("job-42","정동진 해돋이 리조트","정동진 오션뷰 리조트 숙식 제공","강릉","강릉 정동진","리조트","단기 알바",1950000,"주 5일",20,37.6864,129.0629,"🌅",false,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 10,800원")),
            // 전주
            job("job-15","한옥마을 한복 대여샵","전주 한옥마을 내 한복 대여","전주","전주 완산구","문화관광","단기 알바",1500000,"주 5일",10,35.8151,127.1544,"👘",true,List.of("👘 한복 경험","⏰ 주 5일","💴 시급 10,030원")),
            job("job-16","전주 전통음식 체험관","비빔밥·콩나물국밥 체험 보조","전주","전주 완산구","문화관광","단기 알바",1700000,"주 5일",12,35.8148,127.1531,"🍱",false,List.of("🍚 요리 경험","⏰ 주 5일","💴 시급 10,500원")),
            job("job-43","한옥마을 대나무 카페","전주 한옥마을 내 대나무 테마 카페","전주","전주 완산구","카페","단기 알바",1550000,"주 5일",12,35.8148,127.1540,"🎋",false,List.of("☕ 바리스타","⏰ 주 5일","💴 시급 10,030원")),
            job("job-44","전주 교동아트마켓 플리마켓","주말 플리마켓 운영 보조","전주","전주 완산구","문화관광","단기 알바",1300000,"주 2일",10,35.8160,127.1527,"🛍️",false,List.of("🎪 플리마켓","⏰ 주 2일","💴 시급 10,030원")),
            // 경주
            job("job-17","불국사 관광안내 스태프","유네스코 세계유산 불국사 안내","경주","경주 토함산","관광안내","단기 알바",1900000,"주 5일",35,35.7901,129.3322,"⛩️",true,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 11,000원")),
            job("job-18","황리단길 카페 스태프","경주 핫플레이스 황리단길 카페","경주","경주 황남동","카페","단기 알바",1500000,"주 5일",15,35.8397,129.2104,"☕",false,List.of("🚌 버스 직결","⏰ 주 5일","💴 시급 10,030원")),
            job("job-45","경주 보문관광단지 리조트","보문호 오션뷰 리조트 숙식","경주","경주 보문동","리조트","단기 알바",2000000,"주 5일",15,35.8481,129.2892,"🏨",false,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 11,000원")),
            job("job-46","첨성대 주변 야시장 스태프","경주 야시장 먹거리·기념품 판매","경주","경주 인왕동","시장","단기 알바",1350000,"주 5일",5,35.8374,129.2188,"🏮",false,List.of("🌙 야간 근무","⏰ 주 5일","💴 시급 10,030원")),
            // 인천
            job("job-19","인천공항 면세점 스태프","인천국제공항 면세점 판매","인천","인천 중구","면세점","단기 알바",2300000,"주 5일",30,37.4602,126.4407,"✈️",true,List.of("✈️ 공항 근무","⏰ 주 5일","💴 시급 12,000원")),
            job("job-20","송도 컨벤시아 행사 스태프","국제행사·전시회 운영 보조","인천","인천 연수구","행사","단기 알바",1800000,"주 5일",20,37.3943,126.6579,"🎪",false,List.of("🌍 외국어 우대","⏰ 주 5일","💴 시급 10,500원")),
            job("job-47","인천항 국제여객터미널 스태프","인천항 국제여객선 탑승 안내","인천","인천 중구","관광안내","단기 알바",2000000,"주 5일",20,37.4680,126.6006,"🚢",false,List.of("🚢 여객선","⏰ 주 5일","💴 시급 11,000원")),
            job("job-48","차이나타운 음식점 서빙","인천 차이나타운 유명 짜장면집","인천","인천 중구","카페","단기 알바",1500000,"주 6일",10,37.4758,126.6171,"🍜",false,List.of("🍜 요리 경험","⏰ 주 6일","💴 시급 10,030원")),
            // 여수
            job("job-21","여수 엑스포 해양공원 스태프","여수 해양공원 관람객 안내","여수","여수 수정동","관광안내","단기 알바",1800000,"주 5일",10,34.7390,127.7360,"🦑",true,List.of("🚌 버스 직결","⏰ 주 5일","💴 시급 10,500원")),
            job("job-22","돌산도 펜션 운영 스태프","오션뷰 펜션 예약·청소·서비스","여수","여수 돌산읍","숙박","단기 알바",1900000,"주 5일",25,34.6931,127.7636,"🏠",false,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 10,800원")),
            job("job-49","여수 케이블카 운영 스태프","여수 해상케이블카 탑승·안내","여수","여수 돌산","관광안내","단기 알바",1850000,"주 5일",15,34.7265,127.7520,"🚡",false,List.of("🚡 케이블카","⏰ 주 5일","💴 시급 10,500원")),
            job("job-50","오동도 선착장 관광 보트","오동도 유람선 승선 안내·매표","여수","여수 오동도","레저","단기 알바",1700000,"주 5일",10,34.7401,127.7659,"⛵",false,List.of("⛵ 해양 경험","⏰ 주 5일","💴 시급 10,030원")),
            // 속초
            job("job-23","설악산 케이블카 운영 스태프","설악산 케이블카 탑승·안내 숙식","속초","속초 설악동","관광안내","단기 알바",2000000,"주 5일",20,38.1198,128.4654,"🏔️",true,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 11,000원")),
            job("job-24","속초 중앙시장 닭강정 판매","속초 유명 닭강정 가게","속초","속초 중앙동","시장","단기 알바",1500000,"주 6일",10,38.2047,128.5919,"🍗",false,List.of("🍗 식품 경험","⏰ 주 6일","💴 시급 10,030원")),
            job("job-51","속초 아바이마을 순대 판매","속초 아바이마을 오징어순대","속초","속초 청호동","시장","단기 알바",1400000,"주 6일",15,38.2087,128.5818,"🦑",false,List.of("🦑 식품 경험","⏰ 주 6일","💴 시급 10,030원")),
            job("job-52","청초호 오션뷰 카페","청초호 호수뷰 감성 카페","속초","속초 조양동","카페","단기 알바",1600000,"주 5일",10,38.1986,128.5937,"☕",false,List.of("☕ 바리스타","⏰ 주 5일","💴 시급 10,030원")),
            // 광주
            job("job-25","국립아시아문화전당 도슨트","아시아문화전당 전시 안내","광주","광주 동구","문화관광","단기 알바",1800000,"주 5일",10,35.1481,126.9178,"🎨",true,List.of("🎨 예술 관심","⏰ 주 5일","💴 시급 10,500원")),
            job("job-26","양림동 역사마을 관광안내","광주 근대역사 마을 안내 및 해설","광주","광주 남구","관광안내","단기 알바",1600000,"주 5일",15,35.1403,126.9102,"🏛️",false,List.of("🏛️ 역사 관심","⏰ 주 5일","💴 시급 10,030원")),
            job("job-53","동명동 카페거리 바리스타","광주 감성 카페거리 스페셜티 카페","광주","광주 동구","카페","단기 알바",1550000,"주 5일",15,35.1453,126.9244,"☕",false,List.of("☕ 바리스타","⏰ 주 5일","💴 시급 10,030원")),
            job("job-54","광주 송정리 시장 먹거리","광주 송정리역전 5일장 판매","광주","광주 광산구","시장","단기 알바",1300000,"주 5일",5,35.1401,126.7927,"🍢",false,List.of("🍢 음식 경험","⏰ 주 5일","💴 시급 10,030원")),
            // 대전
            job("job-27","엑스포 과학공원 스태프","과학공원 시설 운영·관람객 안내","대전","대전 유성구","관광안내","단기 알바",1700000,"주 5일",20,36.3741,127.3874,"🔬",true,List.of("🔬 과학 관심","⏰ 주 5일","💴 시급 10,030원")),
            job("job-28","유성온천 스파·호텔 스태프","유성온천 특급호텔 숙식 제공","대전","대전 유성구","호텔","단기 알바",1900000,"주 5일",15,36.3626,127.3463,"♨️",false,List.of("🍱 숙식제공","⏰ 주 5일","💴 시급 10,800원")),
            job("job-29","성심당 베이커리 판매 스태프","대전 명물 성심당 분점 판매","대전","대전 중구","카페","단기 알바",1500000,"주 5일",10,36.3267,127.4274,"🍞",false,List.of("🍞 베이커리","⏰ 주 5일","💴 시급 10,030원"))
        ));
    }

    private Job job(String id, String title, String company, String regionId, String district,
                    String category, String empType, int salary, String workingDays, int commute,
                    double lat, double lng, String emoji, boolean best, List<String> tags) {
        return Job.builder()
                .id(id).title(title).company(company).regionId(regionId).district(district)
                .category(category).employmentType(empType).monthlySalary(salary)
                .workingDays(workingDays).commuteMinutes(commute)
                .latitude(BigDecimal.valueOf(lat)).longitude(BigDecimal.valueOf(lng))
                .emoji(emoji).best(best).tags(tags).build();
    }

    private void seedAccommodations() {
        if (accommodationRepository.count() > 0) return;
        accommodationRepository.saveAll(List.of(
            Accommodation.builder().id("acc-1").name("대구 도심 호스텔").regionId("대구").district("중구")
                .address("중구 동인동 · 반월당역 도보 4분").monthlyPrice(450000).deposit(100000)
                .distanceKm(new BigDecimal("1.2")).commuteMinutes(12)
                .latitude(new BigDecimal("35.8680")).longitude(new BigDecimal("128.5910"))
                .color("#3B82F6").rank(1)
                .tags(List.of("숙소 별도","지하철 12분")).build(),
            Accommodation.builder().id("acc-2").name("동성로 스마트 고시텔").regionId("대구").district("중구")
                .address("중구 남일동 · 중앙로역 도보 2분").monthlyPrice(380000).deposit(100000)
                .distanceKm(new BigDecimal("0.8")).commuteMinutes(22)
                .latitude(new BigDecimal("35.8715")).longitude(new BigDecimal("128.5865"))
                .color("#10B981").rank(null)
                .tags(List.of("숙소 별도","도보 22분")).build(),
            Accommodation.builder().id("acc-3").name("수성 제이즈 스테이").regionId("대구").district("수성구")
                .address("수성구 황금동 · 수성못역 도보 5분").monthlyPrice(600000).deposit(200000)
                .distanceKm(new BigDecimal("3.1")).commuteMinutes(28)
                .latitude(new BigDecimal("35.8563")).longitude(new BigDecimal("128.6297"))
                .color("#F97316").rank(null)
                .tags(List.of("숙식 제공","버스 28분")).build(),
            Accommodation.builder().id("acc-4").name("팔공산 게스트하우스").regionId("대구").district("동구")
                .address("동구 둔산동 · 팔공산 버스 정류장 5분").monthlyPrice(500000).deposit(100000)
                .distanceKm(new BigDecimal("2.5")).commuteMinutes(35)
                .latitude(new BigDecimal("35.9120")).longitude(new BigDecimal("128.6630"))
                .color("#EF4444").rank(null)
                .tags(List.of("숙식 제공","한달살기 전용")).build(),
            Accommodation.builder().id("acc-5").name("반월당 메트로 게하").regionId("대구").district("중구")
                .address("중구 덕산동 · 반월당역 환승센터").monthlyPrice(580000).deposit(200000)
                .distanceKm(new BigDecimal("1.8")).commuteMinutes(18)
                .latitude(new BigDecimal("35.8658")).longitude(new BigDecimal("128.5948"))
                .color("#8B5CF6").rank(null)
                .tags(List.of("숙소 별도","버스 18분")).build()
        ));
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;
        userRepository.save(User.builder()
                .email("demo@samteo.local").nickname("Samteo Demo")
                .provider("LOCAL").providerId("demo")
                .profileImageUrl(null).role("USER").build());
    }
}
