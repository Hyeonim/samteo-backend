# Chat API

Base path: `/api/chat`

```http
POST /api/chat
Authorization: Bearer {jwt}
```

삼터 도우미에게 질문을 전달한다. 로그인 사용자의 최신 플래너가 있으면 지역, 일자리,
숙소, 예산 정보를 답변 문맥으로 사용한다. 도우미는 읽기 전용이며 데이터를 변경하지 않는다.
