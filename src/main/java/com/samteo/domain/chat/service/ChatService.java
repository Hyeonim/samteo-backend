package com.samteo.domain.chat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.samteo.domain.chat.dto.ChatMessageDto;
import com.samteo.domain.chat.dto.ChatRequest;
import com.samteo.domain.chat.dto.ChatResponse;
import com.samteo.domain.myplanner.entity.PersonalPlanner;
import com.samteo.domain.myplanner.repository.PersonalPlannerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatService {

    private static final int MAX_MESSAGE_LENGTH = 1_000;
    private static final int MAX_HISTORY = 8;
    private static final List<String> DEFAULT_SUGGESTIONS = List.of(
            "내 플래너 요약해줘",
            "예상 잔액을 설명해줘",
            "단기와 장기의 차이는?"
    );

    private final PersonalPlannerRepository plannerRepository;
    private final String apiKey;
    private final String model;

    public ChatService(
            PersonalPlannerRepository plannerRepository,
            @Value("${openai.api-key:}") String apiKey,
            @Value("${openai.model:gpt-4.1-mini}") String model) {
        this.plannerRepository = plannerRepository;
        this.apiKey = apiKey;
        this.model = model;
    }

    public ChatResponse reply(Long userId, ChatRequest request) {
        String message = normalizeMessage(request == null ? null : request.message());
        if (message.isBlank()) {
            return new ChatResponse("궁금한 내용을 입력해 주세요.", "guide", DEFAULT_SUGGESTIONS);
        }

        Optional<PersonalPlanner> planner = plannerRepository.findFirstByUserIdOrderByUpdatedAtDesc(userId);
        if (apiKey == null || apiKey.isBlank()) {
            return fallbackReply(message, planner);
        }

        try {
            String answer = requestOpenAi(message, request, planner);
            if (answer == null || answer.isBlank()) return fallbackReply(message, planner);
            return new ChatResponse(answer.trim(), "ai", DEFAULT_SUGGESTIONS);
        } catch (Exception ignored) {
            return fallbackReply(message, planner);
        }
    }

    private String requestOpenAi(String message, ChatRequest request, Optional<PersonalPlanner> planner) {
        RestClient client = RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        body.put("instructions", systemPrompt(planner));
        body.put("input", conversationInput(message, request));
        body.put("max_output_tokens", 500);

        JsonNode response = client.post()
                .uri("/responses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(JsonNode.class);
        return extractOutputText(response);
    }

    private List<Map<String, String>> conversationInput(String message, ChatRequest request) {
        List<Map<String, String>> input = new ArrayList<>();
        List<ChatMessageDto> history = request == null || request.history() == null
                ? List.of()
                : request.history();
        int start = Math.max(0, history.size() - MAX_HISTORY);
        for (int i = start; i < history.size(); i++) {
            ChatMessageDto item = history.get(i);
            if (item == null || item.content() == null) continue;
            String role = "assistant".equals(item.role()) ? "assistant" : "user";
            input.add(Map.of("role", role, "content", normalizeMessage(item.content())));
        }
        input.add(Map.of("role", "user", "content", message));
        return input;
    }

    private String systemPrompt(Optional<PersonalPlanner> planner) {
        String context = planner.map(this::plannerContext)
                .orElse("사용자에게 저장된 플래너가 없습니다.");
        return """
                당신은 지역 체류 서비스 SAMTEO의 읽기 전용 상담원입니다.
                한국어로 친절하고 간결하게 답하세요. 제공된 플래너와 서비스 정보만 사용하고,
                존재하지 않는 일자리·숙소·이벤트를 만들어내지 마세요. 금융·법률 확정 조언을 하지 마세요.
                데이터 수정, 저장, 삭제를 수행했다고 말하지 마세요. 사용자가 변경을 요청하면 현재는
                화면에서 직접 수정해야 한다고 안내하세요. 답변은 5문장 이내를 우선합니다.

                최근 플래너 정보:
                """ + context;
    }

    private String plannerContext(PersonalPlanner planner) {
        return String.format(Locale.KOREA,
                "제목=%s, 지역=%s, 체류유형=%s, 예상급여=%,d원, 숙박비=%,d원, 기본생활비=%,d원, 예상잔액=%,d원",
                safe(planner.getTitle()), safe(planner.getRegionName()), safe(planner.getPlannerType()),
                number(planner.getTotalSalary()), number(planner.getAccommodationCost()),
                number(planner.getFixedExpense()), number(planner.getDisposableIncome()));
    }

    private ChatResponse fallbackReply(String message, Optional<PersonalPlanner> planner) {
        String lowered = message.toLowerCase(Locale.KOREA);
        if (lowered.contains("단기") || lowered.contains("장기")) {
            return new ChatResponse(
                    "단기 체류는 짧은 기간의 아르바이트와 지역 경험에, 장기 체류는 채용 공고와 안정적인 정착 계획에 초점을 둡니다. 일자리 검증 단계 상단에서 유형을 바로 전환해 비교할 수 있어요.",
                    "guide", DEFAULT_SUGGESTIONS);
        }
        if (lowered.contains("플래너") || lowered.contains("잔액") || lowered.contains("급여") || lowered.contains("숙박")) {
            if (planner.isEmpty()) {
                return new ChatResponse("아직 저장된 플래너가 없습니다. 먼저 플래너를 만들면 급여·숙박비·예상 잔액을 함께 설명해 드릴게요.", "guide", DEFAULT_SUGGESTIONS);
            }
            PersonalPlanner value = planner.get();
            String answer = String.format(Locale.KOREA,
                    "%s 플래너는 %s 체류 기준이며, 예상 급여는 %,d원입니다. 숙박비 %,d원과 기본 생활비 %,d원을 반영한 예상 잔액은 %,d원이에요. 추가 수입·지출은 내 플래너의 ‘자세히 보기’에서 조정할 수 있습니다.",
                    safe(value.getTitle()), safe(value.getRegionName()), number(value.getTotalSalary()),
                    number(value.getAccommodationCost()), number(value.getFixedExpense()),
                    number(value.getDisposableIncome()));
            return new ChatResponse(answer, "planner", DEFAULT_SUGGESTIONS);
        }
        if (lowered.contains("이벤트") || lowered.contains("축제")) {
            return new ChatResponse("이벤트 메뉴에서 지역별 축제와 행사를 확인할 수 있어요. 플래너의 빈 시간에 맞는 행사를 찾은 뒤 일정에 추가해 보세요.", "guide", DEFAULT_SUGGESTIONS);
        }
        if (lowered.contains("일자리") || lowered.contains("알바")) {
            return new ChatResponse("일자리 메뉴에서는 공공 채용과 단기 알바를 확인할 수 있어요. 플래너에서는 선택한 지역과 체류 유형에 맞춰 일자리를 비교할 수 있습니다.", "guide", DEFAULT_SUGGESTIONS);
        }
        return new ChatResponse("삼터 이용 방법, 단기·장기 체류, 일자리, 이벤트 또는 내 플래너 금액에 대해 질문해 주세요.", "guide", DEFAULT_SUGGESTIONS);
    }

    private String extractOutputText(JsonNode response) {
        if (response == null) return null;
        for (JsonNode output : response.path("output")) {
            for (JsonNode content : output.path("content")) {
                if ("output_text".equals(content.path("type").asText())) {
                    return content.path("text").asText();
                }
            }
        }
        return null;
    }

    private String normalizeMessage(String value) {
        String normalized = value == null ? "" : value.trim();
        return normalized.length() > MAX_MESSAGE_LENGTH
                ? normalized.substring(0, MAX_MESSAGE_LENGTH)
                : normalized;
    }

    private long number(Long value) {
        return value == null ? 0 : value;
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "미입력" : value;
    }
}
