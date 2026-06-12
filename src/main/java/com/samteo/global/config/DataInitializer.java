package com.samteo.global.config;

import com.samteo.domain.planner.entity.Accommodation;
import com.samteo.domain.planner.entity.Job;
import com.samteo.domain.planner.repository.AccommodationRepository;
import com.samteo.domain.planner.repository.JobRepository;
import com.samteo.domain.region.entity.Region;
import com.samteo.domain.region.repository.RegionRepository;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private static final List<CitySeed> CITIES = List.of(
            new CitySeed("seoul", "서울", "수도권 중심 상권과 다양한 단기 일자리가 밀집한 도시입니다.", new BigDecimal("37.5665"), new BigDecimal("126.9780"), List.of(
                    new DistrictSeed("seoul-jongno", "종로구", "종로·북촌 문화권"),
                    new DistrictSeed("seoul-gangnam", "강남구", "강남역·코엑스 상권"),
                    new DistrictSeed("seoul-mapo", "마포구", "홍대·합정 상권"),
                    new DistrictSeed("seoul-yongsan", "용산구", "이태원·한강 생활권")
            )),
            new CitySeed("busan", "부산", "해변 관광과 항만 물류 일자리를 함께 볼 수 있는 도시입니다.", new BigDecimal("35.1796"), new BigDecimal("129.0756"), List.of(
                    new DistrictSeed("busan-haeundae", "해운대구", "해운대 관광지구"),
                    new DistrictSeed("busan-suyeong", "수영구", "광안리 해변 상권"),
                    new DistrictSeed("busan-junggu", "중구", "남포동·자갈치 상권"),
                    new DistrictSeed("busan-busanjin", "부산진구", "서면 중심 상권")
            )),
            new CitySeed("daegu", "대구", "도심 상권과 교통 거점 기반의 체류형 일자리가 있는 도시입니다.", new BigDecimal("35.8714"), new BigDecimal("128.6014"), List.of(
                    new DistrictSeed("junggu", "중구", "동성로·대구역 중심"),
                    new DistrictSeed("donggu", "동구", "동대구역·팔공산 생활권"),
                    new DistrictSeed("suseong", "수성구", "수성못·주거 상권"),
                    new DistrictSeed("dalseo", "달서구", "성서산단·생활 상권"),
                    new DistrictSeed("bukgu", "북구", "대학가·복합 상권")
            )),
            new CitySeed("jeju", "제주", "숙박·관광·렌터카 중심의 시즌 일자리가 많은 도시입니다.", new BigDecimal("33.4996"), new BigDecimal("126.5312"), List.of(
                    new DistrictSeed("jeju-jeju", "제주시", "공항·시내 생활권"),
                    new DistrictSeed("jeju-aewol", "애월읍", "해안 카페 거리"),
                    new DistrictSeed("jeju-seogwipo", "서귀포시", "중문·리조트 권역"),
                    new DistrictSeed("jeju-seongsan", "성산읍", "성산일출봉 관광권")
            )),
            new CitySeed("gangneung", "강릉", "해변 카페와 숙박업 중심의 체류형 일자리가 있는 도시입니다.", new BigDecimal("37.7519"), new BigDecimal("128.8761"), List.of(
                    new DistrictSeed("gangneung-gyo", "교동", "강릉역 생활권"),
                    new DistrictSeed("gangneung-anmok", "안목동", "안목해변 카페 거리"),
                    new DistrictSeed("gangneung-gyeongpo", "경포동", "경포대 관광권"),
                    new DistrictSeed("gangneung-jumunjin", "주문진읍", "항구·해변 상권")
            )),
            new CitySeed("jeonju", "전주", "한옥마을과 전통 음식 관광 일자리가 강한 도시입니다.", new BigDecimal("35.8242"), new BigDecimal("127.1480"), List.of(
                    new DistrictSeed("jeonju-wansan", "완산구", "한옥마을 생활권"),
                    new DistrictSeed("jeonju-deokjin", "덕진구", "전북대·터미널 상권"),
                    new DistrictSeed("jeonju-jungang", "중앙동", "객리단길 상권"),
                    new DistrictSeed("jeonju-hyoja", "효자동", "신시가지 생활권")
            )),
            new CitySeed("gyeongju", "경주", "역사 관광지와 숙박 서비스 일자리가 연결된 도시입니다.", new BigDecimal("35.8562"), new BigDecimal("129.2247"), List.of(
                    new DistrictSeed("gyeongju-hwangnam", "황남동", "황리단길 상권"),
                    new DistrictSeed("gyeongju-bomun", "보문동", "보문관광단지"),
                    new DistrictSeed("gyeongju-bulguk", "불국동", "불국사 관광권"),
                    new DistrictSeed("gyeongju-dongcheon", "동천동", "시청 생활권")
            )),
            new CitySeed("incheon", "인천", "공항·송도·항만 기반의 서비스와 물류 일자리가 있는 도시입니다.", new BigDecimal("37.4563"), new BigDecimal("126.7052"), List.of(
                    new DistrictSeed("incheon-yeonsu", "연수구", "송도 국제도시"),
                    new DistrictSeed("incheon-junggu", "중구", "공항·차이나타운 권역"),
                    new DistrictSeed("incheon-namdong", "남동구", "남동공단 생활권"),
                    new DistrictSeed("incheon-bupyeong", "부평구", "부평역 상권")
            )),
            new CitySeed("yeosu", "여수", "해양 관광과 식음료 서비스 일자리가 있는 도시입니다.", new BigDecimal("34.7604"), new BigDecimal("127.6622"), List.of(
                    new DistrictSeed("yeosu-jungang", "중앙동", "이순신광장 상권"),
                    new DistrictSeed("yeosu-dolsan", "돌산읍", "돌산 관광권"),
                    new DistrictSeed("yeosu-soho", "소호동", "소호 해변 생활권"),
                    new DistrictSeed("yeosu-ungcheon", "웅천동", "마리나 생활권")
            )),
            new CitySeed("sokcho", "속초", "설악산과 해변 숙박권을 함께 보는 관광 도시입니다.", new BigDecimal("38.2070"), new BigDecimal("128.5918"), List.of(
                    new DistrictSeed("sokcho-joyang", "조양동", "속초해변 생활권"),
                    new DistrictSeed("sokcho-cheongho", "청호동", "아바이마을 권역"),
                    new DistrictSeed("sokcho-noryang", "노학동", "설악산 진입권"),
                    new DistrictSeed("sokcho-geumho", "금호동", "중앙시장 상권")
            )),
            new CitySeed("gwangju", "광주", "문화 예술과 도심 서비스 일자리가 균형 잡힌 도시입니다.", new BigDecimal("35.1595"), new BigDecimal("126.8526"), List.of(
                    new DistrictSeed("gwangju-dong", "동구", "충장로·문화전당 권역"),
                    new DistrictSeed("gwangju-seo", "서구", "상무지구 상권"),
                    new DistrictSeed("gwangju-buk", "북구", "전남대 생활권"),
                    new DistrictSeed("gwangju-gwangsan", "광산구", "첨단·수완 생활권")
            )),
            new CitySeed("daejeon", "대전", "교통 중심성과 연구단지 기반 서비스 일자리가 있는 도시입니다.", new BigDecimal("36.3504"), new BigDecimal("127.3845"), List.of(
                    new DistrictSeed("daejeon-yuseong", "유성구", "유성온천·연구단지"),
                    new DistrictSeed("daejeon-junggu", "중구", "은행동 중심 상권"),
                    new DistrictSeed("daejeon-seogu", "서구", "둔산동 행정 상권"),
                    new DistrictSeed("daejeon-daedeok", "대덕구", "산업단지 생활권")
            ))
    );

    private static final List<JobTemplate> JOB_TEMPLATES = List.of(
            new JobTemplate("카페 매장 스태프", "Samteo Cafe", "Food and Beverage", "Part-time", 1650000, "Mon-Fri 10:00-17:00", List.of("cafe", "beginner", "day-shift")),
            new JobTemplate("게스트하우스 운영 보조", "Local Stay", "Accommodation", "Short contract", 1850000, "Tue-Sat 09:00-18:00", List.of("stay", "cleaning", "guest-service")),
            new JobTemplate("관광 안내 데스크", "Travel Lounge", "Tourism", "Contract", 2100000, "Wed-Sun 09:00-18:00", List.of("tourism", "language-plus", "weekend")),
            new JobTemplate("편의점 야간 스태프", "Samteo Retail", "Store", "Part-time", 1200000, "3 nights per week", List.of("night", "store", "short-term")),
            new JobTemplate("로컬 식당 홀 스태프", "Local Table", "Food Service", "Part-time", 1750000, "Thu-Mon 11:00-20:00", List.of("meal-support", "service", "local-food")),
            new JobTemplate("물류 피킹 보조", "Urban Fulfillment", "Logistics", "Short contract", 1950000, "Mon-Fri 08:00-17:00", List.of("logistics", "meal-support", "standing")),
            new JobTemplate("축제 현장 운영 스태프", "Festival Crew", "Event", "Short contract", 1550000, "Fri-Sun 12:00-21:00", List.of("event", "weekend", "team-work")),
            new JobTemplate("렌터카 접수 보조", "Mobility Desk", "Mobility", "Contract", 2000000, "Mon-Sat 09:00-18:00", List.of("desk", "driver-license-plus", "tourism")),
            new JobTemplate("기념품샵 판매 스태프", "Local Goods", "Shopping", "Part-time", 1600000, "Tue-Sun 10:00-19:00", List.of("sales", "tourism", "beginner")),
            new JobTemplate("호텔 조식 보조", "Morning Hotel", "Accommodation", "Part-time", 1450000, "5 mornings per week", List.of("morning", "hotel", "meal-support"))
    );

    private final RegionRepository regionRepository;
    private final JobRepository jobRepository;
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;

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
        List<Region> regions = new ArrayList<>();
        for (CitySeed city : CITIES) {
            for (int i = 0; i < city.districts().size(); i++) {
                DistrictSeed district = city.districts().get(i);
                regions.add(Region.builder()
                        .id(district.id())
                        .name(city.name() + " " + district.name())
                        .summary(city.summary() + " " + district.description() + " 일대를 중심으로 체류 동선을 설계할 수 있습니다.")
                        .hotPlaceScore(96 - (i * 4))
                        .housingCostScore(58 + (i * 7) % 35)
                        .centerLatitude(city.latitude().add(BigDecimal.valueOf(i).multiply(new BigDecimal("0.0060"))))
                        .centerLongitude(city.longitude().add(BigDecimal.valueOf(i).multiply(new BigDecimal("0.0060"))))
                        .tags(List.of(city.id(), district.name(), "work-stay", "transit"))
                        .recommendationReasons(List.of(
                                district.description() + " 주변 일자리 접근성이 좋습니다.",
                                "숙소와 일자리의 이동 시간을 함께 비교하기 좋습니다."
                        ))
                        .build());
            }
        }
        regionRepository.saveAll(regions);
    }

    private void seedJobs() {
        if (jobRepository.count() > 0) return;
        List<Job> jobs = new ArrayList<>();
        for (CitySeed city : CITIES) {
            for (int i = 0; i < 50; i++) {
                DistrictSeed district = city.districts().get(i % city.districts().size());
                JobTemplate template = JOB_TEMPLATES.get(i % JOB_TEMPLATES.size());
                int sequence = i + 1;
                jobs.add(Job.builder()
                        .id("job-" + city.id() + "-" + String.format("%03d", sequence))
                        .title(city.name() + " " + district.name() + " " + template.title())
                        .company(template.company() + " " + city.name() + " " + sequence)
                        .cityId(city.id())
                        .cityName(city.name())
                        .regionId(district.id())
                        .district(district.name())
                        .address(city.name() + " " + district.name() + " " + sampleRoadName(sequence) + " " + (10 + sequence))
                        .category(template.category())
                        .employmentType(template.employmentType())
                        .monthlySalary(template.monthlySalary() + (sequence % 8) * 50000)
                        .workingDays(template.workingDays())
                        .commuteMinutes(15 + (sequence * 7) % 45)
                        .latitude(city.latitude().add(BigDecimal.valueOf(sequence % 13).multiply(new BigDecimal("0.0011"))))
                        .longitude(city.longitude().add(BigDecimal.valueOf(sequence % 11).multiply(new BigDecimal("0.0011"))))
                        .tags(template.tags())
                        .build());
            }
        }
        jobRepository.saveAll(jobs);
    }

    private void seedAccommodations() {
        if (accommodationRepository.count() > 0) return;
        List<Accommodation> accommodations = new ArrayList<>();
        for (CitySeed city : CITIES) {
            for (int i = 0; i < city.districts().size(); i++) {
                DistrictSeed district = city.districts().get(i);
                for (int j = 1; j <= 2; j++) {
                    int sequence = i * 2 + j;
                    accommodations.add(Accommodation.builder()
                            .id("acc-" + city.id() + "-" + district.id() + "-" + j)
                            .name(city.name() + " " + district.name() + " 스테이 " + j)
                            .regionId(district.id())
                            .district(district.name())
                            .address(city.name() + " " + district.name() + " " + sampleRoadName(sequence) + " " + (20 + sequence))
                            .monthlyPrice(380000 + ((sequence + city.id().length()) % 8) * 45000)
                            .deposit(100000 + (sequence % 5) * 50000)
                            .distanceKm(new BigDecimal("0.8").add(BigDecimal.valueOf(sequence).multiply(new BigDecimal("0.35"))))
                            .commuteMinutes(12 + (sequence * 5) % 35)
                            .latitude(city.latitude().add(BigDecimal.valueOf(sequence).multiply(new BigDecimal("0.0015"))))
                            .longitude(city.longitude().add(BigDecimal.valueOf(sequence).multiply(new BigDecimal("0.0015"))))
                            .tags(List.of("short-term", "private-room", sequence % 2 == 0 ? "station" : "budget"))
                            .build());
                }
            }
        }
        accommodationRepository.saveAll(accommodations);
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            return;
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userRepository.save(User.createLocal(
                "demo@samteo.local",
                "Samteo Demo",
                passwordEncoder.encode("1234")
        ));
    }

    private String sampleRoadName(int sequence) {
        List<String> roads = List.of("중앙로", "청년로", "시장길", "해변로", "문화로", "역전로", "공원로", "구암로", "관광로", "상생길");
        return roads.get(sequence % roads.size());
    }

    private record CitySeed(String id, String name, String summary, BigDecimal latitude, BigDecimal longitude, List<DistrictSeed> districts) {
    }

    private record DistrictSeed(String id, String name, String description) {
    }

    private record JobTemplate(String title, String company, String category, String employmentType, int monthlySalary, String workingDays, List<String> tags) {
    }
}
