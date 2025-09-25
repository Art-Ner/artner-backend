-- 기존 더미 데이터 모두 삭제 (외래키 제약조건 때문에 순서 중요)
DELETE FROM project_members;
DELETE FROM projects;
DELETE FROM concert_history;
DELETE FROM filmography;
DELETE FROM artist_role;
DELETE FROM artist_genre;
DELETE FROM artist_profile;
DELETE FROM users;

-- 더미 데이터: users (30명 추가)
-- oauth_provider_type: 'KAKAO', 'GOOGLE'
-- user_role: 'USER', 'ADMIN'
INSERT INTO users (email, phone, oauth_provider, username, nickname, role, created_at, updated_at) VALUES
-- 아티스트 유저들 (1-20)
('parksungjin@gmail.com', '010-1234-5001', 'GOOGLE', '박성진', '성진', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('leesungho@kakao.com', '010-1234-5002', 'KAKAO', '이성호', '록스타샘', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('parkyuna@gmail.com', '010-1234-5003', 'GOOGLE', '박유나', '바이올린퀸', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('choiminho@kakao.com', '010-1234-5004', 'KAKAO', '최민호', '비트메이커', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jungsora@gmail.com', '010-1234-5005', 'GOOGLE', '정소라', '소울보이스', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('kanghyunwoo@kakao.com', '010-1234-5006', 'KAKAO', '강현우', '그루브마스터', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('songminji@gmail.com', '010-1234-5007', 'GOOGLE', '송민지', '첼로러버', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('hanjiwon@kakao.com', '010-1234-5008', 'KAKAO', '한지원', '일렉트로닉', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('yoonseojun@gmail.com', '010-1234-5009', 'GOOGLE', '윤서준', '트럼펫맨', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('limchaeyoung@kakao.com', '010-1234-5010', 'KAKAO', '임채영', '플루트걸', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ohtaeyang@gmail.com', '010-1234-5011', 'GOOGLE', '오태양', '힙합홀릭', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('shinyerin@kakao.com', '010-1234-5012', 'KAKAO', '신예린', '팝디바', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('baejihoon@gmail.com', '010-1234-5013', 'GOOGLE', '배지훈', '작곡왕', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('kwonnari@kakao.com', '010-1234-5014', 'KAKAO', '권나리', '하프천사', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jeonminsu@gmail.com', '010-1234-5015', 'GOOGLE', '전민수', '색소폰블루스', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('moonhayoung@kakao.com', '010-1234-5016', 'KAKAO', '문하영', '비올라소녀', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ahndongwon@gmail.com', '010-1234-5017', 'GOOGLE', '안동원', '펑크로커', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('seojisu@kakao.com', '010-1234-5018', 'KAKAO', '서지수', '컨트리송', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('namyubin@gmail.com', '010-1234-5019', 'GOOGLE', '남유빈', '퍼커션킹', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('hongsanghyun@kakao.com', '010-1234-5020', 'KAKAO', '홍상현', '일렉기타짱', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 공연장 운영자들 (21-25)
('venuebluenote@gmail.com', '010-2000-1001', 'GOOGLE', '블루노트서울', '블루노트Seoul', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('venuejazzalley@kakao.com', '010-2000-1002', 'KAKAO', '재즈앨리', '재즈앨리관리자', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('venuerockhouse@gmail.com', '010-2000-1003', 'GOOGLE', '락하우스', '락하우스코리아', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('venueclassichall@kakao.com', '010-2000-1004', 'KAKAO', '클래식홀', '클래식홀매니저', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('venueindiestage@gmail.com', '010-2000-1005', 'GOOGLE', '인디스테이지', '인디스테이지공식', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 일반 유저들 (26-28)
('userfan01@gmail.com', '010-3000-2001', 'GOOGLE', '김팬덤', '음악덕후', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('userfan02@kakao.com', '010-3000-2002', 'KAKAO', '이관객', '공연중독자', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('userfan03@gmail.com', '010-3000-2003', 'GOOGLE', '박관람', '페스티벌고고', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 관리자 (29-30)
('adminsystem@artner.kr', '010-9000-0001', 'GOOGLE', '시스템관리자', 'ArtnerAdmin', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('adminsupport@artner.kr', '010-9000-0002', 'KAKAO', '고객지원팀', 'ArtnerSupport', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (oauth_provider, email) DO NOTHING;
-- 더미 데이터: artist_profile (아티스트 20명)
-- 장르: CLASSICAL, POP, ROCK, BLACK_MUSIC, JAZZ_BLUES, FOLK_COUNTRY, ELECTRONIC, WORLD_MUSIC, ETC
-- 역할: VOCAL, KEYBOARD, GUITAR, PERCUSSION, STRING, WIND, ETC
INSERT INTO artist_profile (user_id, artist_name, profile_image_url, headline, bio, urls) VALUES
(1, '성진', NULL, '밴드 보컬/기타리스트', '밴드에서 보컬과 기타를 담당하고 있습니다. 록 음악에 대한 열정으로 무대를 불태웁니다.', ARRAY['https://instagram.com/sungjin_band', 'https://youtube.com/@sungjin_music']),
(2, '이성호', NULL, '록 기타리스트', '20년 경력의 세션 기타리스트입니다. 록, 메탈, 블루스 모두 가능합니다.', ARRAY['https://instagram.com/rockstar_sam', 'https://soundcloud.com/sungho-guitar']),
(3, '박유나', NULL, '클래식 바이올리니스트', '서울예고 출신 바이올리니스트입니다. 클래식부터 크로스오버까지 다양한 레퍼토리를 소화합니다.', ARRAY['https://instagram.com/violin_queen', 'https://youtube.com/@yuna_violin']),
(4, '최민호', NULL, '록/재즈 드러머', '록과 재즈를 넘나드는 드럼 연주가 가능합니다. 펑크와 퓨전 장르에 강점이 있습니다.', ARRAY['https://instagram.com/beatmaker_minho', 'https://youtube.com/@minho_drums']),
(5, '정소라', NULL, '재즈 보컬리스트', '뉴욕에서 재즈 보컬을 전공했습니다. 따뜻하고 감성적인 목소리가 특징입니다.', ARRAY['https://instagram.com/soulvoice_sora', 'https://spotify.com/artist/sora']),
(6, '강현우', NULL, '재즈/R&B 베이시스트', '재즈와 R&B를 넘나드는 베이스 연주자입니다. 그루브감 있는 연주가 강점입니다.', ARRAY['https://instagram.com/groovemaster_hw', 'https://youtube.com/@hyunwoo_bass']),
(7, '송민지', NULL, '클래식/팝 첼리스트', '첼로의 깊은 울림으로 감동을 전하는 연주자입니다. 클래식과 팝을 넘나듭니다.', ARRAY['https://instagram.com/cello_lover', 'https://youtube.com/@minji_cello']),
(8, '한지원', NULL, '일렉트로닉 뮤직 프로듀서', '일렉트로닉 음악 전문 프로듀서입니다. 클럽 음악부터 앰비언트까지 제작 가능합니다.', ARRAY['https://instagram.com/electronic_jw', 'https://soundcloud.com/jiwon-edm']),
(9, '윤서준', NULL, '재즈 트럼펫 연주자', '부드러운 트럼펫 선율이 매력적인 재즈 뮤지션입니다.', ARRAY['https://instagram.com/trumpet_man', 'https://youtube.com/@seojun_trumpet']),
(10, '임채영', NULL, '클래식 플루티스트', '국립교향악단 출신 플루트 연주자입니다. 맑고 청아한 소리가 특징입니다.', ARRAY['https://instagram.com/flute_girl', 'https://youtube.com/@chaeyoung_flute']),
(11, '오태양', NULL, '힙합 보컬리스트', '한국 힙합씬에서 활동 중인 래퍼입니다. 가사 작성과 편곡도 가능합니다.', ARRAY['https://instagram.com/hiphop_holic', 'https://soundcloud.com/taeyang-rap']),
(12, '신예린', NULL, '팝 보컬리스트', 'K-POP과 팝 발라드를 주로 부르는 보컬리스트입니다. 폭넓은 음역대가 강점입니다.', ARRAY['https://instagram.com/pop_diva', 'https://youtube.com/@yerin_official']),
(13, '배지훈', NULL, '팝/일렉트로닉 프로듀서', '팝과 일렉트로닉 장르의 작곡과 편곡을 전문으로 하는 프로듀서입니다.', ARRAY['https://instagram.com/composer_jh', 'https://soundcloud.com/jihoon-music']),
(14, '권나리', NULL, '클래식 하프 연주자', '오케스트라와 솔로 활동을 병행하는 하피스트입니다. 천상의 소리를 들려드립니다.', ARRAY['https://instagram.com/harp_angel', 'https://youtube.com/@nari_harp']),
(15, '전민수', NULL, '재즈 색소폰 연주자', '재즈와 블루스 색소폰 연주자입니다. 감성적인 연주가 특징입니다.', ARRAY['https://instagram.com/sax_blues', 'https://youtube.com/@minsu_sax']),
(16, '문하영', NULL, '클래식 비올라 연주자', '실내악과 오케스트라 활동을 주로 하는 비올라 연주자입니다.', ARRAY['https://instagram.com/viola_girl', 'https://youtube.com/@hayoung_viola']),
(17, '안동원', NULL, '펑크 록 기타리스트', '에너지 넘치는 펑크 록 연주가 특기인 기타리스트입니다.', ARRAY['https://instagram.com/punk_rocker', 'https://soundcloud.com/dongwon-rock']),
(18, '서지수', NULL, '컨트리 싱어송라이터', '어쿠스틱 기타와 함께하는 컨트리 음악을 만듭니다.', ARRAY['https://instagram.com/country_song', 'https://youtube.com/@jisu_country']),
(19, '남유빈', NULL, '월드뮤직 타악기 연주자', '다양한 타악기를 다루는 퍼커션 전문 연주자입니다. 월드뮤직에 특화되어 있습니다.', ARRAY['https://instagram.com/percussion_king', 'https://youtube.com/@yubin_percussion']),
(20, '홍상현', NULL, '록 일렉트릭 기타리스트', '록과 메탈을 주로 연주하는 기타리스트입니다. 화려한 테크닉이 특징입니다.', ARRAY['https://instagram.com/elec_guitar', 'https://youtube.com/@sanghyun_guitar'])
ON CONFLICT DO NOTHING;

-- 더미 데이터: artist_genre
INSERT INTO artist_genre (artist_profile_id, genre_code) VALUES
(1, 'ROCK'), (2, 'ROCK'), (3, 'CLASSICAL'), (4, 'ROCK'), (4, 'JAZZ_BLUES'),
(5, 'JAZZ_BLUES'), (6, 'JAZZ_BLUES'), (6, 'BLACK_MUSIC'), (7, 'CLASSICAL'), (7, 'POP'),
(8, 'ELECTRONIC'), (9, 'JAZZ_BLUES'), (10, 'CLASSICAL'), (11, 'BLACK_MUSIC'), (12, 'POP'),
(13, 'POP'), (13, 'ELECTRONIC'), (14, 'CLASSICAL'), (15, 'JAZZ_BLUES'), (16, 'CLASSICAL'),
(17, 'ROCK'), (18, 'FOLK_COUNTRY'), (19, 'WORLD_MUSIC'), (20, 'ROCK')
ON CONFLICT DO NOTHING;

-- 더미 데이터: artist_role
INSERT INTO artist_role (artist_profile_id, role_code) VALUES
(1, 'VOCAL'), (1, 'GUITAR'), (2, 'GUITAR'), (3, 'STRING'), (4, 'PERCUSSION'), (5, 'VOCAL'),
(6, 'GUITAR'), (7, 'STRING'), (8, 'KEYBOARD'), (8, 'ETC'), (9, 'WIND'),
(10, 'WIND'), (11, 'VOCAL'), (12, 'VOCAL'), (13, 'KEYBOARD'), (13, 'ETC'),
(14, 'STRING'), (15, 'WIND'), (16, 'STRING'), (17, 'GUITAR'), (18, 'VOCAL'),
(18, 'GUITAR'), (19, 'PERCUSSION'), (20, 'GUITAR')
ON CONFLICT DO NOTHING;

-- 더미 데이터: filmography (성진 - DAY6 앨범)
INSERT INTO filmography (artist_profile_id, title, description, released_at, media_url) VALUES
(1, 'The Day', 'DAY6 데뷔 EP 앨범', '2015-09-07', NULL),
(1, 'Daydream', '1st 미니 앨범', '2016-03-30', NULL),
(1, 'Sunrise', '1st 정규 앨범 - Every DAY6 프로젝트', '2017-06-07', NULL),
(1, 'Moonrise', '2nd 정규 앨범 - Every DAY6 프로젝트', '2017-12-06', NULL),
(1, 'Shoot Me : Youth Part 1', '3rd 미니 앨범', '2018-06-26', NULL),
(1, 'Remember Us : Youth Part 2', '4th 미니 앨범', '2018-12-10', NULL),
(1, 'The Book of Us : Gravity', '5th 미니 앨범', '2019-07-15', NULL),
(1, 'The Book of Us : Entropy', '6th 미니 앨범', '2019-10-22', NULL),
(1, 'The Book of Us : The Demon', '3rd 정규 앨범', '2020-05-11', NULL),
(1, 'The Book of Us : Negentropy - Chaos swallowed up in love', '7th 미니 앨범', '2021-04-19', NULL),
(1, 'Fourever', '8th 미니 앨범', '2024-03-18', NULL),
(1, 'Band Aid', '9th 미니 앨범', '2024-09-02', NULL),
(1, 'The Decade', '4th 정규 앨범 - 10주년 기념', '2025-09-05', NULL)
ON CONFLICT DO NOTHING;

-- 더미 데이터: concert_history (성진 - DAY6 주요 투어)
INSERT INTO concert_history (artist_profile_id, work_title, role_code, started_on, ended_on, proof_url) VALUES
(1, 'DAY6 1st Concert "D-Day"', ARRAY['VOCAL', 'GUITAR'], '2017-02-24', '2017-02-26', NULL),
(1, 'DAY6 Youth Tour', ARRAY['VOCAL', 'GUITAR'], '2018-07-01', '2018-12-31', NULL),
(1, 'DAY6 Gravity Tour', ARRAY['VOCAL', 'GUITAR'], '2019-08-01', '2019-12-31', NULL),
(1, 'DAY6 1st World Tour "Youth"', ARRAY['VOCAL', 'GUITAR'], '2019-01-01', '2019-06-30', NULL),
(1, 'DAY6 "The Book of Us" Concert', ARRAY['VOCAL', 'GUITAR'], '2020-01-10', '2020-01-12', NULL),
(1, 'DAY6 2nd World Tour "Right Through Me"', ARRAY['VOCAL', 'GUITAR'], '2022-06-01', '2022-12-31', NULL),
(1, 'DAY6 3rd World Tour "Forever Young"', ARRAY['VOCAL', 'GUITAR'], '2024-09-20', '2025-05-18', NULL),
(1, 'DAY6 10th Anniversary Tour "The Decade"', ARRAY['VOCAL', 'GUITAR'], '2025-08-30', '2026-12-31', NULL)
ON CONFLICT DO NOTHING;

-- 기존 프로젝트 관련 데이터 삭제 (외래키 제약조건 때문에 순서 중요)
DELETE FROM project_members;
DELETE FROM projects;

-- 더미 데이터: projects (1회성 공연 모집 프로젝트 15개)
INSERT INTO projects (owner_id, title, concept, target_region, target_genre, expected_scale, status, created_at, updated_at) VALUES
-- 모집 중인 1회성 공연들 (RECRUITING)
(1, '홍대 클럽 원나잇 록 공연', '12월 28일 홍대 클럽에서 열릴 록 공연을 위한 세션 멤버를 모집합니다. 드러머와 베이시스트가 필요합니다. 90년대 록을 중심으로 3-4곡 연주 예정입니다.', '서울 마포구', 'ROCK', '소극장 (100-200석)', 'RECRUITING', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP),

(5, '강남 재즈바 단독 공연', '1월 15일 강남 재즈바에서 재즈 보컬 단독 공연을 진행합니다. 피아노 트리오(피아노, 베이스, 드럼) 세션을 찾고 있습니다. 재즈 스탠다드 위주로 2시간 공연 예정입니다.', '서울 강남구', 'JAZZ_BLUES', '소극장 (80-120석)', 'RECRUITING', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP),

(8, '신촌 클럽 EDM 파티', '1월 20일 신촌 클럽에서 EDM 파티 공연을 합니다. 함께 무대에 설 프로듀서나 DJ를 모집합니다. 하우스, 테크노 장르 중심으로 4시간 공연입니다.', '서울 서대문구', 'ELECTRONIC', '소극장 (200-300석)', 'RECRUITING', CURRENT_TIMESTAMP - INTERVAL '1 week', CURRENT_TIMESTAMP),

(3, '예술의전당 크로스오버 공연', '2월 5일 예술의전당 리사이틀홀에서 클래식 크로스오버 공연을 진행합니다. 현악기(첼로, 비올라) 연주자와 피아니스트를 찾고 있습니다. 90분 단독 공연입니다.', '서울 서초구', 'CLASSICAL', '중극장 (300-500석)', 'RECRUITING', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP),

(11, '이태원 힙합 쇼케이스', '12월 30일 이태원 클럽에서 힙합 쇼케이스 공연을 진행합니다. 함께 무대에 설 래퍼와 DJ를 모집합니다. 각자 15분씩 솔로 무대 + 합동 무대로 구성됩니다.', '서울 용산구', 'BLACK_MUSIC', '소극장 (150-250석)', 'RECRUITING', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP),

(18, '경리단길 어쿠스틱 공연', '1월 12일 경리단길 카페에서 어쿠스틱 공연을 합니다. 하모니카나 바이올린으로 함께 연주할 뮤지션을 찾고 있습니다. 포크/컨트리 장르 위주 2시간 공연입니다.', '서울 용산구', 'FOLK_COUNTRY', '소극장 (50-80석)', 'RECRUITING', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP),

-- 모집 완료된 1회성 공연들 (RECRUITED)
(2, '건대 라이브 카페 록 공연', '1월 8일 건대 라이브 카페에서 록 공연을 진행합니다. 멤버 구성이 완료되어 현재 연습 중입니다. 클래식 록과 모던 록 레퍼토리로 2시간 공연 예정입니다.', '서울 광진구', 'ROCK', '소극장 (120-180席)', 'RECRUITED', CURRENT_TIMESTAMP - INTERVAL '2 weeks', CURRENT_TIMESTAMP - INTERVAL '3 days'),

(6, '압구정 재즈클럽 퓨전 공연', '1월 18일 압구정 재즈클럽에서 재즈 퓨전 공연을 진행합니다. 베이스 중심의 그루브한 연주로 2시간 30분 공연합니다. 멤버 구성 완료되었습니다.', '서울 강남구', 'JAZZ_BLUES', '소극장 (100-150석)', 'RECRUITED', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '1 day'),

(13, '홍익대 앞 버스킹 공연', '12월 29일 홍익대 앞 걷고싶은거리에서 버스킹 공연을 진행합니다. 팝송과 K-POP 커버를 중심으로 3시간 공연 예정입니다. 멤버 구성 완료했습니다.', '서울 마포구', 'POP', '야외 공연 (제한없음)', 'RECRUITED', CURRENT_TIMESTAMP - INTERVAL '1 week', CURRENT_TIMESTAMP - INTERVAL '2 days'),

-- 종료된 1회성 공연들 (CLOSED)
(4, '강북 문화센터 드럼 공연', '12월 15일 강북 문화센터에서 진행된 드럼 솔로 공연입니다. 다양한 장르의 드럼 연주를 선보였습니다. 관객들의 뜨거운 호응을 받았습니다.', '서울 강북구', 'ROCK', '중극장 (200-300석)', 'CLOSED', CURRENT_TIMESTAMP - INTERVAL '1 month', CURRENT_TIMESTAMP - INTERVAL '1 week'),

(7, '동대문 소극장 첼로 독주회', '12월 10일 동대문 소극장에서 진행된 첼로 독주회입니다. 바로크부터 현대곡까지 다양한 레퍼토리로 90분 공연을 성공적으로 마쳤습니다.', '서울 중구', 'CLASSICAL', '소극장 (80-120석)', 'CLOSED', CURRENT_TIMESTAMP - INTERVAL '3 weeks', CURRENT_TIMESTAMP - INTERVAL '5 days'),

(9, '청담동 재즈바 트럼펫 공연', '12월 5일 청담동 재즈바에서 진행된 트럼펫 중심의 재즈 콰르텟 공연입니다. 스탠다드 재즈와 즉흥연주로 2시간 30분 공연했습니다.', '서울 강남구', 'JAZZ_BLUES', '소극장 (60-100석)', 'CLOSED', CURRENT_TIMESTAMP - INTERVAL '2 months', CURRENT_TIMESTAMP - INTERVAL '2 weeks'),

(12, '논현동 K-POP 커버 공연', '11월 28일 논현동 라이브 카페에서 진행된 K-POP 커버 공연입니다. 라이브 밴드로 인기곡들을 재해석하여 관객들과 함께 즐겼습니다.', '서울 강남구', 'POP', '소극장 (150-200석)', 'CLOSED', CURRENT_TIMESTAMP - INTERVAL '6 weeks', CURRENT_TIMESTAMP - INTERVAL '3 weeks'),

(19, '종로 전통공연장 월드뮤직', '12월 1일 종로 전통공연장에서 진행된 한국 전통악기와 세계 타악기의 융합 공연입니다. 문화적 경계를 넘나드는 90분 공연으로 마무리했습니다.', '서울 종로구', 'WORLD_MUSIC', '중극장 (200-300석)', 'CLOSED', CURRENT_TIMESTAMP - INTERVAL '4 weeks', CURRENT_TIMESTAMP - INTERVAL '1 week'),

(17, '상수동 클럽 펑크 록 공연', '11월 20일 상수동 클럽에서 진행된 펑크 록 공연입니다. 고에너지 공연으로 젊은 관객들과 뜨거운 호응을 나눴습니다. 3시간 공연으로 성황리에 마쳤습니다.', '서울 마포구', 'ROCK', '소극장 (150-250석)', 'CLOSED', CURRENT_TIMESTAMP - INTERVAL '5 weeks', CURRENT_TIMESTAMP - INTERVAL '10 days')
ON CONFLICT DO NOTHING;

-- 더미 데이터: project_members (1회성 공연 참여 멤버들)
INSERT INTO project_members (project_id, artist_id, joined_at) VALUES
-- 프로젝트 1 (홍대 클럽 록 공연) - 성진(보컬/기타) + 민호(드럼) + 현우(베이스)
(1, 1, CURRENT_TIMESTAMP - INTERVAL '3 days'), -- owner
(1, 4, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(1, 6, CURRENT_TIMESTAMP - INTERVAL '1 day'),

-- 프로젝트 2 (강남 재즈바 공연) - 소라(보컬) + 지훈(피아노) + 현우(베이스)
(2, 5, CURRENT_TIMESTAMP - INTERVAL '5 days'), -- owner
(2, 13, CURRENT_TIMESTAMP - INTERVAL '4 days'),
(2, 6, CURRENT_TIMESTAMP - INTERVAL '3 days'),

-- 프로젝트 3 (신촌 클럽 EDM 파티) - 지원(DJ/프로듀서) + 지훈(프로듀서)
(3, 8, CURRENT_TIMESTAMP - INTERVAL '1 week'), -- owner
(3, 13, CURRENT_TIMESTAMP - INTERVAL '5 days'),

-- 프로젝트 4 (예술의전당 크로스오버) - 유나(바이올린) + 민지(첼로) + 나리(하프)
(4, 3, CURRENT_TIMESTAMP - INTERVAL '2 days'), -- owner
(4, 7, CURRENT_TIMESTAMP - INTERVAL '1 day'),
(4, 14, CURRENT_TIMESTAMP - INTERVAL '12 hours'),

-- 프로젝트 5 (이태원 힙합 쇼케이스) - 태양(래퍼) + 지원(DJ)
(5, 11, CURRENT_TIMESTAMP - INTERVAL '4 days'), -- owner
(5, 8, CURRENT_TIMESTAMP - INTERVAL '3 days'),

-- 프로젝트 6 (경리단길 어쿠스틱) - 지수(싱어송라이터) + 채영(플루트)
(6, 18, CURRENT_TIMESTAMP - INTERVAL '6 days'), -- owner
(6, 10, CURRENT_TIMESTAMP - INTERVAL '4 days'),

-- 프로젝트 7 (건대 라이브 카페 록) - 성호(기타) + 성진(보컬) + 민호(드럼) + 현우(베이스)
(7, 2, CURRENT_TIMESTAMP - INTERVAL '2 weeks'), -- owner
(7, 1, CURRENT_TIMESTAMP - INTERVAL '10 days'),
(7, 4, CURRENT_TIMESTAMP - INTERVAL '9 days'),
(7, 6, CURRENT_TIMESTAMP - INTERVAL '8 days'),

-- 프로젝트 8 (압구정 재즈클럽 퓨전) - 현우(베이스) + 소라(보컬) + 민호(드럼)
(8, 6, CURRENT_TIMESTAMP - INTERVAL '10 days'), -- owner
(8, 5, CURRENT_TIMESTAMP - INTERVAL '7 days'),
(8, 4, CURRENT_TIMESTAMP - INTERVAL '6 days'),

-- 프로젝트 9 (홍익대 앞 버스킹) - 지훈(키보드/프로듀서) + 예린(보컬)
(9, 13, CURRENT_TIMESTAMP - INTERVAL '1 week'), -- owner
(9, 12, CURRENT_TIMESTAMP - INTERVAL '5 days')
ON CONFLICT DO NOTHING;
