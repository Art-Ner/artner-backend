-- pgvector (requires superuser once per database; safe to keep here for dev)
CREATE EXTENSION IF NOT EXISTS vector;

-- 1) ENUM 타입 정의
CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');
CREATE TYPE oauth_provider_type AS ENUM ('KAKAO','GOOGLE');
CREATE TYPE project_status AS ENUM ('RECRUITING', 'RECRUITED', 'CLOSED');
CREATE TYPE collab_status AS ENUM ('PENDING', 'ACCEPTED', 'REJECTED');
CREATE TYPE booking_status AS ENUM ('REQUESTED', 'APPROVED', 'REJECTED', 'CANCELLED');
CREATE TYPE performance_status AS ENUM ('DRAFT','PUBLISHED');
CREATE TYPE ticket_status AS ENUM ('RESERVED', 'PAID', 'CANCELLED', 'REFUNDED');
CREATE TYPE bookmark_target_type AS ENUM ('PROJECT', 'VENUE', 'PERFORMANCE', 'USER');
CREATE TYPE genre_code AS ENUM (
  'CLASSICAL',          -- 클래식
  'POP',                -- 팝
  'ROCK',               -- 락
  'BLACK_MUSIC',        -- 흑인음악 (R&B, 소울, 힙합, 펑크 등)
  'JAZZ_BLUES',         -- 재즈/블루스
  'FOLK_COUNTRY',       -- 포크/컨트리
  'ELECTRONIC',         -- 전자음악
  'WORLD_MUSIC',        -- 월드뮤직
  'ETC'                 -- 기타
);
CREATE TYPE role_code AS ENUM (
  'VOCAL',              -- 보컬
  'KEYBOARD',           -- 건반악기
  'GUITAR',             -- 진짜 기타
  'PERCUSSION',         -- 타악기
  'STRING',             -- 현악기
  'WIND',               -- 관악기
  'ETC'                 -- 기타
);

-- 2) 사용자/프로필
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(190) UNIQUE,
  phone VARCHAR(30),
  oauth_provider oauth_provider_type NOT NULL, -- kakao, google 등
  username VARCHAR(100) NOT NULL,
  nickname VARCHAR(100) NOT NULL,
  role user_role NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uq_oauth UNIQUE (oauth_provider, email)
);

-- 업데이트 트리거 함수
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- users 테이블에 업데이트 트리거 적용
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE artist_profile (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  artist_name VARCHAR(100) NOT NULL,
  profile_image_url VARCHAR(255), -- S3 경로
  headline VARCHAR(150) NOT NULL,
  bio TEXT,
  urls TEXT[] DEFAULT '{}' NOT NULL,  -- 배열로 변경됨
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE venue_admin_profile (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  profile_image_url VARCHAR(255), -- S3 경로
  business_reg_number VARCHAR(10) NOT NULL, -- 사업자 등록 번호
  description TEXT,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 아티스트 속성 N:M
CREATE TABLE artist_genre (
  artist_profile_id BIGINT NOT NULL,
  genre_code genre_code NOT NULL,
  PRIMARY KEY (artist_profile_id, genre_code),
  FOREIGN KEY (artist_profile_id) REFERENCES artist_profile(id) ON DELETE CASCADE
);

CREATE TABLE artist_role (
  artist_profile_id BIGINT NOT NULL,
  role_code role_code NOT NULL,
  PRIMARY KEY (artist_profile_id, role_code),
  FOREIGN KEY (artist_profile_id) REFERENCES artist_profile(id) ON DELETE CASCADE
);

-- 3) 포트폴리오/이력
CREATE TABLE filmography (  -- 앨범
  id BIGSERIAL PRIMARY KEY,
  artist_profile_id BIGINT NOT NULL,
  title VARCHAR(150) NOT NULL,
  description TEXT,
  released_at DATE NOT NULL,
  media_url VARCHAR(255),  -- S3 경로
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (artist_profile_id) REFERENCES artist_profile(id) ON DELETE CASCADE
);

CREATE INDEX ix_filmography_artist_profile ON filmography(artist_profile_id, created_at);

CREATE TRIGGER update_filmography_updated_at
    BEFORE UPDATE ON filmography
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE concert_history ( -- 공연이력
  id BIGSERIAL PRIMARY KEY,
  artist_profile_id BIGINT NOT NULL,
  work_title VARCHAR(200) NOT NULL,
  role_code role_code[] NOT NULL, -- 여러 역할을 배열로 저장
  started_on DATE,
  ended_on DATE,
  proof_url VARCHAR(255), -- 참고 urla
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (artist_profile_id) REFERENCES artist_profile(id) ON DELETE CASCADE
);

CREATE INDEX ix_concert_history_artist_profile ON concert_history(artist_profile_id, started_on);

CREATE TRIGGER update_concert_history_updated_at
    BEFORE UPDATE ON concert_history
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- 4) 팀빌딩(아이디어 → 모집 → 지원/수락)
CREATE TABLE projects (
  id BIGSERIAL PRIMARY KEY,
  owner_id BIGINT NOT NULL, -- 기획자
  title VARCHAR(150) NOT NULL,
  concept TEXT NOT NULL, -- 공연 아이디어/기획서 요약
  target_region VARCHAR(100),
  target_genre genre_code NOT NULL,
  expected_scale VARCHAR(50), -- 좌석 규모 등 자유기입
  status project_status NOT NULL DEFAULT 'RECRUITING',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  concept_embedding vector(1536), -- OpenAI 임베딩을 위한 벡터 컬럼
  FOREIGN KEY (owner_id) REFERENCES artist_profile(id) ON DELETE CASCADE
);

CREATE INDEX ix_projects_status ON projects(status, created_at);

-- 벡터 유사도 검색을 위한 HNSW 인덱스
CREATE INDEX idx_projects_embedding_hnsw ON projects
  USING hnsw (concept_embedding vector_cosine_ops);

CREATE TRIGGER update_projects_updated_at
    BEFORE UPDATE ON projects
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- 팀 참여자에 대한 정보
CREATE TABLE project_members (
  id BIGSERIAL PRIMARY KEY,
  project_id BIGINT NOT NULL,
  artist_id BIGINT NOT NULL,
  joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uq_member UNIQUE (project_id, artist_id),
  FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
  FOREIGN KEY (artist_id) REFERENCES artist_profile(id) ON DELETE CASCADE
);

-- 프로젝트 협업 요청
CREATE TABLE project_collab_requests (
  id           BIGSERIAL PRIMARY KEY,
  project_id   BIGINT NOT NULL,
  requester_id BIGINT NOT NULL,                 -- 신청자
  status       collab_status NOT NULL DEFAULT 'PENDING',
  message      TEXT,                             -- 소개/신청 메시지 (선택)
  created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  decided_at   TIMESTAMP,                        -- 수락/거절 시각
  decided_by   BIGINT,                           -- 결정자(보통 project.owner_id)

  CONSTRAINT fk_pcr_project    FOREIGN KEY (project_id)   REFERENCES projects(id) ON DELETE CASCADE,
  CONSTRAINT fk_pcr_requester  FOREIGN KEY (requester_id) REFERENCES artist_profile(id)    ON DELETE CASCADE,
  CONSTRAINT fk_pcr_decider    FOREIGN KEY (decided_by)   REFERENCES artist_profile(id)    ON DELETE SET NULL,

  -- 한 유저는 같은 프로젝트에 '하나의 요청'만 (재요청은 기존걸 재사용)
  CONSTRAINT uq_pcr UNIQUE (project_id, requester_id)

);

-- 조회/권한용 인덱스
CREATE INDEX ix_pcr_project_status   ON project_collab_requests(project_id, status, created_at);
CREATE INDEX ix_pcr_requester        ON project_collab_requests(requester_id, created_at);

-- 5) 메시지
CREATE TABLE conversations (
  id BIGSERIAL PRIMARY KEY,
  user_low_id BIGINT NOT NULL, -- 대화 저장 중복 방지
  user_high_id BIGINT NOT NULL,
  archived_by_low BOOLEAN NOT NULL DEFAULT FALSE,   -- user_low가 나갔는지
  archived_by_high BOOLEAN NOT NULL DEFAULT FALSE,  -- user_high가 나갔는지
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,
  CONSTRAINT chk_user_order CHECK (user_low_id < user_high_id),
  CONSTRAINT uq_direct_pair UNIQUE (user_low_id, user_high_id),
  CONSTRAINT fk_conv_user_low FOREIGN KEY (user_low_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_conv_user_high FOREIGN KEY (user_high_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX ix_conv_users ON conversations(user_low_id, user_high_id);
CREATE INDEX ix_conv_type_ct ON conversations(created_at);

CREATE TABLE messages (
  id BIGSERIAL PRIMARY KEY,
  conversation_id BIGINT NOT NULL,
  sender_id BIGINT NOT NULL,
  body TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_msg_conv FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,
  CONSTRAINT fk_msg_sender FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX ix_msg_conv_created ON messages(conversation_id, created_at);

-- 6) 공연장/대관
-- 시/도 (광역단위)
CREATE TABLE region_province (
  code VARCHAR(4) PRIMARY KEY,         -- 예: '11'
  name VARCHAR(50) NOT NULL            -- 예: '서울특별시'
);

-- 구/군 (시/도 하위)
CREATE TABLE region_district (
  province_code VARCHAR(4) NOT NULL,   -- 상위 시/도 코드
  code VARCHAR(16) NOT NULL,           -- 예: '11680' (여유 길이)
  name VARCHAR(50) NOT NULL,           -- 예: '강남구'
  PRIMARY KEY (province_code, code),
  CONSTRAINT fk_district_province FOREIGN KEY (province_code)
    REFERENCES region_province(code)
);

-- 공연장/대관
CREATE TABLE venues (
  id BIGSERIAL PRIMARY KEY,
  admin_profile_id BIGINT, -- 공연장 운영자(계정)
  name VARCHAR(150) NOT NULL,
  region VARCHAR(100) NOT NULL,
  province_code VARCHAR(4), -- sidonm 코드
  district_code VARCHAR(16), -- gugunnm 코드
  address VARCHAR(255), -- 상세주소 (사업자 등록 시만 필수)
  image_url TEXT, -- 공연장 이미지
  seat_capacity INTEGER, -- 좌석 수 (사업자 등록용)
  base_rental_fee INTEGER, -- 대관료 (사업자 등록용)
  facility_type VARCHAR(100), -- fcltychartr (시설특성)
  description TEXT,
  kopis_venue_id VARCHAR(64), -- KOPIS 매핑
  source VARCHAR(20) NOT NULL DEFAULT 'INTERNAL', -- 'KOPIS' or 'INTERNAL'
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_venues_admin_profile FOREIGN KEY (admin_profile_id)
    REFERENCES venue_admin_profile(id) ON DELETE CASCADE,
  CONSTRAINT chk_venues_seat_pos   CHECK (seat_capacity > 0),
  CONSTRAINT chk_venues_fee_nonneg CHECK (base_rental_fee >= 0)
);

CREATE INDEX ix_venues_region ON venues(region);
CREATE INDEX ix_venues_kopis ON venues(kopis_venue_id);

CREATE TRIGGER update_venues_updated_at
    BEFORE UPDATE ON venues
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- 공연장 가용/블럭 슬롯: venue_availability
CREATE TABLE venue_availability (
  id BIGSERIAL PRIMARY KEY,
  venue_id BIGINT NOT NULL,
  is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
  start_dt TIMESTAMP NOT NULL,
  end_dt TIMESTAMP NOT NULL,
  note VARCHAR(255),
  FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE,

  CONSTRAINT chk_va_range CHECK (end_dt > start_dt)
);

CREATE INDEX ix_va_venue_time ON venue_availability(venue_id, start_dt, end_dt);

CREATE TABLE bookings (
  id BIGSERIAL PRIMARY KEY,
  requested_by BIGINT NOT NULL,
  venue_id BIGINT NOT NULL,
  project_id BIGINT NOT NULL,
  start_dt TIMESTAMP NOT NULL,
  end_dt TIMESTAMP NOT NULL,
  status booking_status NOT NULL DEFAULT 'REQUESTED',
  decided_at TIMESTAMP, -- APPROVED, REJECTED, CANCELLED 될 때
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE,
  FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
  FOREIGN KEY (requested_by) REFERENCES users(id) ON DELETE CASCADE,

  CONSTRAINT chk_bookings_range CHECK (end_dt > start_dt)
);

CREATE INDEX ix_bookings_venue ON bookings(venue_id, start_dt, end_dt);

-- 7) 공연
CREATE TABLE performances (
  id BIGSERIAL PRIMARY KEY,
  owner_id BIGINT NOT NULL,              -- 공연 생성자 (ArtistProfile ID)
  project_id BIGINT,
  venue_id BIGINT,
  title VARCHAR(150) NOT NULL,
  description TEXT NOT NULL, -- 공연 내용
  genre_code genre_code NOT NULL,
  running_time INTEGER,
  poster_url VARCHAR(255),
  start_dt TIMESTAMP NOT NULL,
  end_dt TIMESTAMP NOT NULL,

  status performance_status NOT NULL DEFAULT 'DRAFT',
  published_at  TIMESTAMP,                 -- status='PUBLISHED'일 때만 값 존재

  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (owner_id) REFERENCES artist_profile(id) ON DELETE CASCADE,
  FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
  FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE,

  CONSTRAINT chk_perf_range  CHECK (end_dt > start_dt),
  CONSTRAINT chk_runtime_pos CHECK (running_time IS NULL OR running_time > 0),
  CONSTRAINT chk_status_pubat CHECK (
    (status = 'DRAFT'::performance_status     AND published_at IS NULL) OR
    (status = 'PUBLISHED'::performance_status AND published_at IS NOT NULL)
  )
);

-- 인덱스
CREATE INDEX ix_performances_time        ON performances(start_dt, end_dt);
CREATE INDEX ix_performances_project     ON performances(project_id);
CREATE INDEX ix_performances_venue       ON performances(venue_id);
CREATE INDEX ix_perf_published_start     ON performances(start_dt) WHERE status = 'PUBLISHED'::performance_status;

-- 트리거
CREATE TRIGGER update_performances_updated_at
BEFORE UPDATE ON performances
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- 예매
CREATE TABLE tickets (
  id                 BIGSERIAL PRIMARY KEY,
  performance_id     BIGINT NOT NULL,
  buyer_id           BIGINT NOT NULL,

  price              INTEGER NOT NULL,
  status             ticket_status NOT NULL DEFAULT 'RESERVED',

  -- ✅ HOLD(좌석 선점) 만료 시각
  hold_expires_at    TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL '10 minutes'),

  -- ✅ 결제/취소 시각은 상태 전이 때만 세팅 → NULL 허용
  purchased_at       TIMESTAMP,
  cancelled_at       TIMESTAMP,

  -- ✅ 결제 연동 최소 필드(별도 payments 없이 중복/웹훅 대응)
  external_payment_id VARCHAR(120),      -- PG 트랜잭션 키 (예: paymentKey/imp_uid)
  idempotency_key     UUID,              -- 클라이언트 재시도 멱등키

  -- FK
  CONSTRAINT fk_tickets_performance FOREIGN KEY (performance_id)
    REFERENCES performances(id) ON DELETE CASCADE,
  CONSTRAINT fk_tickets_buyer FOREIGN KEY (buyer_id)
    REFERENCES users(id) ON DELETE CASCADE,

  -- 제약
  CONSTRAINT chk_ticket_price_nonneg CHECK (price >= 0),
  CONSTRAINT uq_tickets_external_payment UNIQUE (external_payment_id),
  CONSTRAINT uq_tickets_idempotency     UNIQUE (idempotency_key)
);

-- ✅ 인덱스 (기존 ix_ticket_buyer 대체 권장)
-- 내가 산 티켓(결제완료 위주) 조회 최적화
CREATE INDEX ix_tickets_buyer_paid
  ON tickets (buyer_id, purchased_at)
  WHERE status = 'PAID';

-- HOLD 만료 스윕 대상 스캔
CREATE INDEX ix_tickets_hold_expiry
  ON tickets (hold_expires_at)
  WHERE status = 'RESERVED';

-- 8) 사회적 신뢰/소통
CREATE TABLE bookmarks (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  target_type bookmark_target_type NOT NULL,
  target_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uq_fav UNIQUE (user_id, target_type, target_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX ix_fav_user ON bookmarks(user_id, created_at);

CREATE TABLE notifications (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  kind VARCHAR(50) NOT NULL, -- ex) MATCH_SUGGEST, APP_ACCEPTED, TICKET_REMINDER
  title VARCHAR(150) NOT NULL,
  body TEXT NOT NULL,
  is_read BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX ix_noti_user ON notifications(user_id, is_read, created_at);

CREATE TABLE venue_reviews (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  venue_id BIGINT NOT NULL,
  rate NUMERIC(2,1) NOT NULL CHECK (rate >= 1.0 AND rate <= 5.0), -- 평점 범위 제한
  content VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uq_user_venue_review UNIQUE (user_id, venue_id), -- 중복 리뷰 방지
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE
);

CREATE TRIGGER update_venue_reviews_updated_at
    BEFORE UPDATE ON venue_reviews
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE user_reviews (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  target_user_id BIGINT NOT NULL,
  content VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (target_user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT chk_no_self_review CHECK (user_id != target_user_id), -- 자기 자신 리뷰 방지
	CONSTRAINT uq_user_to_target UNIQUE (user_id, target_user_id)    -- 한 유저 → 한 대상 유저 리뷰 1개 제한
);

CREATE TRIGGER update_user_reviews_updated_at
    BEFORE UPDATE ON user_reviews
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();