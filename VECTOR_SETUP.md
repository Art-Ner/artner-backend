# pgvector + OpenAI Embeddings 추천 시스템 설정 가이드

## 1. 데이터베이스 설정

PostgreSQL에서 다음 SQL을 실행하세요:

```sql
-- pgvector 확장 설치
CREATE EXTENSION IF NOT EXISTS vector;

-- projects 테이블에 임베딩 컬럼 추가
ALTER TABLE projects ADD COLUMN IF NOT EXISTS concept_embedding vector(1536);

-- 벡터 유사도 검색을 위한 HNSW 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_projects_embedding_hnsw
ON projects USING hnsw (concept_embedding vector_cosine_ops);
```

## 2. 환경 변수 설정

`.env` 파일에 OpenAI API 키를 추가하세요:

```bash
# OpenAI Configuration
OPENAI_API_KEY=your_openai_api_key_here
```

## 3. 구현된 기능

### 3.1 OpenAI Embeddings API 연동
- `text-embedding-3-small` 모델 사용 (1536차원)
- 프로젝트: title + concept + genre + region 조합으로 임베딩 생성
- 아티스트: headline + bio + genres + roles 조합으로 임베딩 생성

### 3.2 벡터 기반 추천
- PostgreSQL의 `<=>` 연산자를 사용한 코사인 유사도 계산
- 사용자 선호도 벡터와 프로젝트 임베딩 간 유사도 기반 추천

### 3.3 폴백 전략
- 임베딩이 없는 경우: 기존 규칙 기반 추천 (장르/지역/인기도)
- OpenAI API 실패 시: 자동으로 기존 추천 시스템으로 전환
- 신규 유저: 인기 프로젝트 기반 추천

## 4. API 엔드포인트

### 4.1 임베딩 생성 (관리자용)
```bash
# 모든 프로젝트 임베딩 생성
POST /api/admin/embeddings/projects/generate-all

# 특정 프로젝트 임베딩 재생성
POST /api/admin/embeddings/projects/{projectId}/regenerate

# 서비스 상태 확인
GET /api/admin/embeddings/health
```

### 4.2 추천 API (기존 유지)
```bash
# 사용자 맞춤 추천 (벡터 기반 → 규칙 기반 폴백)
GET /api/recommendations

# 배치 추천 생성
POST /api/recommendations/batch
```

## 5. 사용 방법

### 5.1 초기 설정
1. 데이터베이스 마이그레이션 실행
2. OpenAI API 키 설정
3. 기존 프로젝트들의 임베딩 생성:
   ```bash
   curl -X POST "http://localhost:8080/api/admin/embeddings/projects/generate-all" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

### 5.2 새 프로젝트 생성 시
- 프로젝트 생성 후 자동으로 임베딩 생성됨
- `ProjectEmbeddingService.generateAndSaveProjectEmbedding()` 호출

### 5.3 추천 요청
- 기존 `/api/recommendations` 엔드포인트 사용
- 자동으로 벡터 기반 → 규칙 기반 순으로 폴백

## 6. 모니터링

### 6.1 로그 확인
- 벡터 기반 추천 성공/실패 로그
- OpenAI API 호출 실패 로그
- 폴백 전략 실행 로그

### 6.2 성능 모니터링
- 임베딩 생성 시간
- 벡터 유사도 검색 시간
- 추천 응답 시간

## 7. 주의사항

- OpenAI API 요금: text-embedding-3-small 모델은 1M 토큰당 $0.02
- 임베딩 저장 공간: 벡터(1536차원) × 프로젝트 수
- pgvector 인덱스 업데이트 시간: 대량 데이터 시 고려 필요

## 8. 향후 개선 가능 사항

1. **사용자 임베딩 캐싱**: Redis에 사용자별 선호도 벡터 저장
2. **하이브리드 추천**: 벡터 + 규칙 기반 점수 조합
3. **실시간 임베딩**: 프로젝트 생성/수정 시 즉시 임베딩 업데이트
4. **A/B 테스트**: 벡터 vs 규칙 기반 추천 성능 비교