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
ON CONFLICT (username, nickname) DO NOTHING;

-- 더미 데이터: artist_profile (아티스트 20명)
-- 장르: CLASSICAL, POP, ROCK, BLACK_MUSIC, JAZZ_BLUES, FOLK_COUNTRY, ELECTRONIC, WORLD_MUSIC, ETC
-- 역할: VOCAL, KEYBOARD, GUITAR, PERCUSSION, STRING, WIND, ETC
INSERT INTO artist_profile (user_id, artist_name, profile_image_url, headline, bio, urls) VALUES
(1, '성진', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/sungjin.jpg', '밴드 보컬/기타리스트', '밴드에서 보컬과 기타를 담당하고 있습니다. 록 음악에 대한 열정으로 무대를 불태웁니다.', ARRAY['https://instagram.com/sungjin_band', 'https://youtube.com/@sungjin_music']),
(2, '이성호', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/sungho.jpg', '록 기타리스트', '20년 경력의 세션 기타리스트입니다. 록, 메탈, 블루스 모두 가능합니다.', ARRAY['https://instagram.com/rockstar_sam', 'https://soundcloud.com/sungho-guitar']),
(3, '박유나', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/yuna.jpg', '클래식 바이올리니스트', '서울예고 출신 바이올리니스트입니다. 클래식부터 크로스오버까지 다양한 레퍼토리를 소화합니다.', ARRAY['https://instagram.com/violin_queen', 'https://youtube.com/@yuna_violin']),
(4, '최민호', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/minho.jpg', '록/재즈 드러머', '록과 재즈를 넘나드는 드럼 연주가 가능합니다. 펑크와 퓨전 장르에 강점이 있습니다.', ARRAY['https://instagram.com/beatmaker_minho', 'https://youtube.com/@minho_drums']),
(5, '정소라', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/sora.jpg', '재즈 보컬리스트', '뉴욕에서 재즈 보컬을 전공했습니다. 따뜻하고 감성적인 목소리가 특징입니다.', ARRAY['https://instagram.com/soulvoice_sora', 'https://spotify.com/artist/sora']),
(6, '강현우', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/hyunwoo.jpg', '재즈/R&B 베이시스트', '재즈와 R&B를 넘나드는 베이스 연주자입니다. 그루브감 있는 연주가 강점입니다.', ARRAY['https://instagram.com/groovemaster_hw', 'https://youtube.com/@hyunwoo_bass']),
(7, '송민지', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/minji.jpg', '클래식/팝 첼리스트', '첼로의 깊은 울림으로 감동을 전하는 연주자입니다. 클래식과 팝을 넘나듭니다.', ARRAY['https://instagram.com/cello_lover', 'https://youtube.com/@minji_cello']),
(8, '한지원', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/jiwon.jpg', '일렉트로닉 뮤직 프로듀서', '일렉트로닉 음악 전문 프로듀서입니다. 클럽 음악부터 앰비언트까지 제작 가능합니다.', ARRAY['https://instagram.com/electronic_jw', 'https://soundcloud.com/jiwon-edm']),
(9, '윤서준', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/seojun.jpg', '재즈 트럼펫 연주자', '부드러운 트럼펫 선율이 매력적인 재즈 뮤지션입니다.', ARRAY['https://instagram.com/trumpet_man', 'https://youtube.com/@seojun_trumpet']),
(10, '임채영', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/chaeyoung.jpg', '클래식 플루티스트', '국립교향악단 출신 플루트 연주자입니다. 맑고 청아한 소리가 특징입니다.', ARRAY['https://instagram.com/flute_girl', 'https://youtube.com/@chaeyoung_flute']),
(11, '오태양', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/taeyang.jpg', '힙합 보컬리스트', '한국 힙합씬에서 활동 중인 래퍼입니다. 가사 작성과 편곡도 가능합니다.', ARRAY['https://instagram.com/hiphop_holic', 'https://soundcloud.com/taeyang-rap']),
(12, '신예린', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/yerin.jpg', '팝 보컬리스트', 'K-POP과 팝 발라드를 주로 부르는 보컬리스트입니다. 폭넓은 음역대가 강점입니다.', ARRAY['https://instagram.com/pop_diva', 'https://youtube.com/@yerin_official']),
(13, '배지훈', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/jihoon.jpg', '팝/일렉트로닉 프로듀서', '팝과 일렉트로닉 장르의 작곡과 편곡을 전문으로 하는 프로듀서입니다.', ARRAY['https://instagram.com/composer_jh', 'https://soundcloud.com/jihoon-music']),
(14, '권나리', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/nari.jpg', '클래식 하프 연주자', '오케스트라와 솔로 활동을 병행하는 하피스트입니다. 천상의 소리를 들려드립니다.', ARRAY['https://instagram.com/harp_angel', 'https://youtube.com/@nari_harp']),
(15, '전민수', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/minsu.jpg', '재즈 색소폰 연주자', '재즈와 블루스 색소폰 연주자입니다. 감성적인 연주가 특징입니다.', ARRAY['https://instagram.com/sax_blues', 'https://youtube.com/@minsu_sax']),
(16, '문하영', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/hayoung.jpg', '클래식 비올라 연주자', '실내악과 오케스트라 활동을 주로 하는 비올라 연주자입니다.', ARRAY['https://instagram.com/viola_girl', 'https://youtube.com/@hayoung_viola']),
(17, '안동원', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/dongwon.jpg', '펑크 록 기타리스트', '에너지 넘치는 펑크 록 연주가 특기인 기타리스트입니다.', ARRAY['https://instagram.com/punk_rocker', 'https://soundcloud.com/dongwon-rock']),
(18, '서지수', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/jisu.jpg', '컨트리 싱어송라이터', '어쿠스틱 기타와 함께하는 컨트리 음악을 만듭니다.', ARRAY['https://instagram.com/country_song', 'https://youtube.com/@jisu_country']),
(19, '남유빈', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/yubin.jpg', '월드뮤직 타악기 연주자', '다양한 타악기를 다루는 퍼커션 전문 연주자입니다. 월드뮤직에 특화되어 있습니다.', ARRAY['https://instagram.com/percussion_king', 'https://youtube.com/@yubin_percussion']),
(20, '홍상현', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/profiles/sanghyun.jpg', '록 일렉트릭 기타리스트', '록과 메탈을 주로 연주하는 기타리스트입니다. 화려한 테크닉이 특징입니다.', ARRAY['https://instagram.com/elec_guitar', 'https://youtube.com/@sanghyun_guitar'])
ON CONFLICT (user_id) DO NOTHING;

-- 더미 데이터: artist_genre
INSERT INTO artist_genre (artist_profile_id, genre_code) VALUES
(1, 'ROCK'), (2, 'ROCK'), (3, 'CLASSICAL'), (4, 'ROCK'), (4, 'JAZZ_BLUES'),
(5, 'JAZZ_BLUES'), (6, 'JAZZ_BLUES'), (6, 'BLACK_MUSIC'), (7, 'CLASSICAL'), (7, 'POP'),
(8, 'ELECTRONIC'), (9, 'JAZZ_BLUES'), (10, 'CLASSICAL'), (11, 'BLACK_MUSIC'), (12, 'POP'),
(13, 'POP'), (13, 'ELECTRONIC'), (14, 'CLASSICAL'), (15, 'JAZZ_BLUES'), (16, 'CLASSICAL'),
(17, 'ROCK'), (18, 'FOLK_COUNTRY'), (19, 'WORLD_MUSIC'), (20, 'ROCK')
ON CONFLICT (artist_profile_id, genre_code) DO NOTHING;

-- 더미 데이터: artist_role
INSERT INTO artist_role (artist_profile_id, role_code) VALUES
(1, 'VOCAL'), (1, 'GUITAR'), (2, 'GUITAR'), (3, 'STRING'), (4, 'PERCUSSION'), (5, 'VOCAL'),
(6, 'GUITAR'), (7, 'STRING'), (8, 'KEYBOARD'), (8, 'ETC'), (9, 'WIND'),
(10, 'WIND'), (11, 'VOCAL'), (12, 'VOCAL'), (13, 'KEYBOARD'), (13, 'ETC'),
(14, 'STRING'), (15, 'WIND'), (16, 'STRING'), (17, 'GUITAR'), (18, 'VOCAL'),
(18, 'GUITAR'), (19, 'PERCUSSION'), (20, 'GUITAR')
ON CONFLICT (artist_profile_id, role_code) DO NOTHING;

-- 더미 데이터: filmography (성진 - DAY6 앨범)
INSERT INTO filmography (artist_profile_id, title, description, released_at, media_url) VALUES
(1, 'The Day', 'DAY6 데뷔 EP 앨범', '2015-09-07', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/the_day.jpg'),
(1, 'Daydream', '1st 미니 앨범', '2016-03-30', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/daydream.jpg'),
(1, 'Sunrise', '1st 정규 앨범 - Every DAY6 프로젝트', '2017-06-07', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/sunrise.jpg'),
(1, 'Moonrise', '2nd 정규 앨범 - Every DAY6 프로젝트', '2017-12-06', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/moonrise.jpg'),
(1, 'Shoot Me : Youth Part 1', '3rd 미니 앨범', '2018-06-26', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/shoot_me.jpg'),
(1, 'Remember Us : Youth Part 2', '4th 미니 앨범', '2018-12-10', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/remember_us.jpg'),
(1, 'The Book of Us : Gravity', '5th 미니 앨범', '2019-07-15', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/gravity.jpg'),
(1, 'The Book of Us : Entropy', '6th 미니 앨범', '2019-10-22', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/entropy.jpg'),
(1, 'The Book of Us : The Demon', '3rd 정규 앨범', '2020-05-11', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/the_demon.jpg'),
(1, 'The Book of Us : Negentropy - Chaos swallowed up in love', '7th 미니 앨범', '2021-04-19', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/negentropy.jpg'),
(1, 'Fourever', '8th 미니 앨범', '2024-03-18', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/fourever.jpg'),
(1, 'Band Aid', '9th 미니 앨범', '2024-09-02', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/band_aid.jpg'),
(1, 'The Decade', '4th 정규 앨범 - 10주년 기념', '2025-09-05', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/albums/the_decade.jpg')
ON CONFLICT (artist_profile_id, title) DO NOTHING;

-- 더미 데이터: concert_history (성진 - DAY6 주요 투어)
INSERT INTO concert_history (artist_profile_id, work_title, role_code, started_on, ended_on, proof_url) VALUES
(1, 'DAY6 1st Concert "D-Day"', ARRAY['VOCAL', 'GUITAR'], '2017-02-24', '2017-02-26', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/concerts/1st_Dday.jpg'),
(1, 'DAY6 Youth Tour', ARRAY['VOCAL', 'GUITAR'], '2018-07-01', '2018-12-31', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/concerts/youth_tour.jpg'),
(1, 'DAY6 Gravity Tour', ARRAY['VOCAL', 'GUITAR'], '2019-08-01', '2019-12-31', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/concerts/gravity_tour.jpg'),
(1, 'DAY6 1st World Tour "Youth"', ARRAY['VOCAL', 'GUITAR'], '2019-01-01', '2019-06-30', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/concerts/worldtour_youth.jpg'),
(1, 'DAY6 "The Book of Us" Concert', ARRAY['VOCAL', 'GUITAR'], '2020-01-10', '2020-01-12', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/concerts/the_book_of.jpg'),
(1, 'DAY6 2nd World Tour "Right Through Me"', ARRAY['VOCAL', 'GUITAR'], '2022-06-01', '2022-12-31', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/concerts/2nd_righr_through.jpg'),
(1, 'DAY6 3rd World Tour "Forever Young"', ARRAY['VOCAL', 'GUITAR'], '2024-09-20', '2025-05-18', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/concerts/3rd_forever_young.jpg'),
(1, 'DAY6 10th Anniversary Tour "The Decade"', ARRAY['VOCAL', 'GUITAR'], '2025-08-30', '2026-12-31', 'https://artner-koolpis.s3.ap-northeast-2.amazonaws.com/concerts/10th_anniversary_decade.webp')
ON CONFLICT (artist_profile_id, work_title) DO NOTHING;

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
ON CONFLICT (owner_id, title) DO NOTHING;

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
ON CONFLICT (project_id, artist_id) DO NOTHING;


-- 더미 데이터: venue_admin_profile (공연장 운영자 5명)
INSERT INTO venue_admin_profile (user_id, business_reg_number, description) VALUES
(21, '123-45-67890', '서울 최고의 재즈 클럽입니다. 정통 재즈부터 모던 재즈까지 다양한 공연을 개최합니다.'),
(22, '234-56-78901', '홍대 대표 라이브 재즈 클럽으로 젊은 재즈 뮤지션들의 무대입니다.'),
(23, '345-67-89012', '락 음악 전문 공연장으로 에너지 넘치는 록 공연을 선보입니다.'),
(24, '456-78-90123', '클래식 음악 전용 콘서트홀로 우수한 음향 시설을 자랑합니다.'),
(25, '567-89-01234', '신진 인디 뮤지션들을 위한 소규모 라이브 공연장입니다.')
ON CONFLICT (user_id) DO NOTHING;

-- 더미 데이터: venues (공연장 108개)
INSERT INTO venues (admin_profile_id, name, region, province_code, district_code, address, image_url, seat_capacity, base_rental_fee, facility_type, description, kopis_venue_id, source) VALUES
(1, '수성아트피아 (용지홀)', '대구광역시 수성구', NULL, NULL, '대구광역시 수성구 무학로 180(지산동)', NULL, 1159, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(1, '수성아트피아 (무학홀)', '대구광역시 수성구', NULL, NULL, '대구광역시 수성구 무학로 180(지산동)', NULL, 301, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(1, '대구메트로아트센터', '대구광역시 수성구', NULL, NULL, '대구광역시 수성구 달구벌대로 2950 대공원역 지하2층', NULL, 202, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(1, '아트센터 달', '대구광역시 수성구', NULL, NULL, '대구광역시 수성구 천을로 173, 6층(매호동)', NULL, 209, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(1, '공간울림 연주홀', '대구광역시 수성구', NULL, NULL, '대구광역시 수성구 범안로64길 18-9(삼덕동)', NULL, 120, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(1, '공간울림 야외공연장', '대구광역시 수성구', NULL, NULL, '대구광역시 수성구 범안로64길 18-9(삼덕동)', NULL, 120, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(1, '소담아트홀', '대구광역시 수성구', NULL, NULL, '대구광역시 수성구 청수로38길 56, 2층', NULL, 60, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(1, '대구생활문화센터 어울림홀', '대구광역시 수성구', NULL, NULL, '대구광역시 수성구 만촌로 153', NULL, 80, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(1, '해남문화예술회관', '전라남도 해남군', '전라남도', NULL, '해남읍 군청길 4', NULL, 700, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(1, '승달문화예술회관 대공연장', '전라남도 강진군', '전라남도', NULL, '창포로 8', NULL, 502, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(1, '영광예술의전당', '전라남도 영광군', '전라남도', NULL, '영광읍 천년로 13길 2-34', NULL, 579, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(1, '장성문화예술회관 소공연장', '전라남도 장성군', '전라남도', NULL, '장성읍 문화로 110', NULL, 199, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(1, '장성문화예술회관 대공연장', '전라남도 장성군', '전라남도', NULL, '장성읍 문화로 111', NULL, 684, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(1, '진도향토문화회관', '전라남도 진도군', '전라남도', NULL, '진도읍 진도대로 7197', NULL, 630, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(1, '포항문화예술회관 대공연장', '경상북도 포항시', '경상북도', NULL, '남구 희망대로 850', NULL, 972, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(1, '포항시청문화동 대잠홀 공연장', '경상북도 포항시', '경상북도', NULL, '남구 시청로 1', NULL, 590, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(1, '포항시립중앙아트홀', '경상북도 포항시', '경상북도', NULL, '북구 서동로 83 ', NULL, 270, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(1, '포항시청소년수련관 청소년극장', '경상북도 포항시', '경상북도', NULL, '북구 삼호로 533', NULL, 323, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(1, '효자아트홀', '경상북도 포항시', '경상북도', NULL, '남구 행복길 120 효자아트홀', NULL, 731, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(1, '경상북도교육청문화원', '경상북도 포항시', '경상북도', NULL, '북구 호로50', NULL, 1146, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(1, '아라떼소극장', '경상북도 포항시', '경상북도', NULL, '남구 상도로26번길5', NULL, 96, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(1, '100씨어터(100theater)', '경상북도 포항시', '경상북도', NULL, '북구 불종로73 석영빌딩 5층', NULL, 163, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(2, '경주예술의전당 대공연장', '경상북도 경주시', '경상북도', NULL, '알천북로 1', NULL, 1053, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(2, '경주예술의전당 소공연장', '경상북도 경주시', '경상북도', NULL, '알천북로 1', NULL, 330, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(2, '서라벌문화회관', '경상북도 경주시', '경상북도', NULL, '금성로236', NULL, 494, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(2, '문화센터 공연장', '경상북도 경주시', '경상북도', NULL, '경감로 614', NULL, 741, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(2, '신라밀레니엄파크 메인공연장', '경상북도 경주시', '경상북도', NULL, '엑스포로 55-12', NULL, 975, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(2, '신라밀레니엄파크 화랑공연장', '경상북도 경주시', '경상북도', NULL, '엑스포로 55-12', NULL, 805, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(2, '김천시문화예술회관', '경상북도 김천시', '경상북도', NULL, '운동장길 3', NULL, 920, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(2, '김천시립문화회관', '경상북도 김천시', '경상북도', NULL, '김천로 200', NULL, 421, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL');
(2, '경북도청 동락관', '경상북도 예천군', '경상북도', NULL, '도청대로455', NULL, 886, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(2, '안동문화예술의전당(웅부홀)', '경상북도 안동시', '경상북도', NULL, '축제장길66', NULL, 994, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(2, '안동문화예술의전당(백조홀)', '경상북도 안동시', '경상북도', NULL, '축제장길66', NULL, 276, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(2, '구미문화예술회관', '경상북도 구미시', '경상북도', NULL, '송정대로 89', NULL, 1211, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(2, '강동문화복지회관', '경상북도 구미시', '경상북도', NULL, '인동가산로392', NULL, 667, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(2, '레미어린이공연장', '경상북도 구미시', '경상북도', NULL, '구미대로22길11', NULL, 184, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(2, '공터 다', '경상북도 구미시', '경상북도', NULL, '금오시장로 4 아트센터 DA 3F', NULL, 100, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL');
(2, '영주문화예술회관', '경상북도 영주시', '경상북도', NULL, '가흥로 257(가흥동)', NULL, 498, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(2, '영주시민회관', '경상북도 영주시', '경상북도', NULL, '선비로 213', NULL, 490, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(2, '영천시민회관', '경상북도 영천시', '경상북도', NULL, '시청로 17', NULL, 791, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL');
(2, '문경문화예술회관', '경상북도 문경시', '경상북도', NULL, '신흥로 85', NULL, 824, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(2, '문희아트홀', '경상북도 문경시', '경상북도', NULL, '신흥로 103', NULL, 310, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀');
(2, '삼국유사교육문화회관 공연장', '경상북도 군위군', '경상북도', NULL, '군위읍 군청로 158', NULL, 457, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(2, '의성문화회관 문소홀', '경상북도 의성군', '경상북도', NULL, '의성읍 충효로68', NULL, 998, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(3, '청송문화예술회관', '경상북도 청송군', '경상북도', NULL, '청송읍 금월로 244-30', NULL, 435, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(3, '진보문화체육센터', '경상북도 청송군', '경상북도', NULL, '진보면 진보로 187', NULL, 480, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(3, '예주문화예술회관', '경상북도 영덕군', '경상북도', NULL, '영해면 318만세길 36', NULL, 610, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(3, '대가야문화누리 우륵홀', '경상북도 고령군', '경상북도', NULL, '대가야읍 왕릉로 30', NULL, 638, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(3, '대가야문화누리 가야금홀', '경상북도 고령군', '경상북도', NULL, '대가야읍 왕릉로 30', NULL, 140, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(3, '칠곡군 교육문화회관 대공연장', '경상북도 칠곡군', '경상북도', NULL, '왜관읍 관문로 1길', NULL, 710, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(3, '예천군문화회관', '경상북도 예천군', '경상북도', NULL, '예천읍 충효로 209-15', NULL, 604, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(3, '울진문화예술회관 공연장', '경상북도 울진군', '경상북도', NULL, '후포면 후포삼율로 194-14', NULL, 492, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(3, '성산아트홀 대극장', '경상남도 창원시', '경상남도', NULL, '의창구 중앙대로 181', NULL, 1690, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(3, '성산아트홀 소극장', '경상남도 창원시', '경상남도', NULL, '의창구 중앙대로 181', NULL, 510, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(3, '3·15아트센터 대극장', '경상남도 창원시', '경상남도', NULL, '마산회원구 삼호로 135', NULL, 1182, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(3, '3·15아트센터 소극장', '경상남도 창원시', '경상남도', NULL, '마산회원구 삼호로 135', NULL, 455, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(3, '진해문화센터 구민회관 공연장', '경상남도 창원시', '경상남도', NULL, '진해구 진해대로 325', NULL, 390, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(3, '도파니아트홀', '경상남도 창원시', '경상남도', NULL, '의창구 창이대로113번길 20', NULL, 90, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(3, '범블비 이벤트 아트홀', '경상남도 창원시', '경상남도', NULL, '의창구 용지로169번길 15, 602호', NULL, 153, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(3, '뉴코아아울렛소극장', '경상남도 창원시', '경상남도', NULL, '의창구 창원대로397번길 6, 4층 263호', NULL, 162, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(3, '경상남도문화예술회관 대공연장', '경상남도 창원시', '경상남도', NULL, '강남로 215', NULL, 1528, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(3, '진주시 전통예술회관 공연장', '경상남도 진주시', '경상남도', NULL, '남강로1번길 96-6', NULL, 218, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(3, '현장아트홀', '경상남도 진주시', '경상남도', NULL, '진주대로 1038', NULL, 134, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(3, 'VK아트홀', '경상남도 진주시', '경상남도', NULL, '내동면 내동로 213', NULL, 60, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(3, '날개', '경상남도 진주시', '경상남도', NULL, '진양호로 257, 2층 (평거동)', NULL, 60, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(3, '통영시민문화회관 대극장', '경상남도 통영시', '경상남도', NULL, '남망공원길 29', NULL, 880, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(4, '통영시민문화회관 소극장', '경상남도 통영시', '경상남도', NULL, '남망공원길 29', NULL, 290, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(4, '통영국제음악당 콘서트홀', '경상남도 통영시', '경상남도', NULL, '큰발개1길 38', NULL, 1309, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(4, '통영국제음악당 블랙박스', '경상남도 통영시', '경상남도', NULL, '큰발개1길 38', NULL, 254, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(4, '사천시문화예술회관(대공연장)', '경상남도 사천시', '경상남도', NULL, '삼천포대교로 471', NULL, 834, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(4, '사천시문화예술회관(소공연장)', '경상남도 사천시', '경상남도', NULL, '삼천포대교로 471', NULL, 192, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(4, '사천세계문화컨텐츠 상설공연장', '경상남도 사천시', '경상남도', NULL, '실안동 1244-2', NULL, 480, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장');
(4, '김해문화원', '경상남도 김해시', '경상남도', NULL, '분성로 225(외동)', NULL, 218, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(4, '김해시립공연장(칠암도서관)', '경상남도 김해시', '경상남도', NULL, '활천로285번길 14(삼방동)', NULL, 310, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(4, '장유도서관 공연장', '경상남도 김해시', '경상남도', NULL, '대청로176번길 7(대청동)', NULL, 342, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(4, '김해문화의전당 마루홀', '경상남도 김해시', '경상남도', NULL, '김해대로 2060(내동)', NULL, 1464, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(4, '진영한빛도서관 누리마을', '경상남도 김해시', '경상남도', NULL, '진영읍 김해대로 440', NULL, 369, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(4, '철광산공연장(김해가야테마파크)', '경상남도 김해시', '경상남도', NULL, '가야테마길161(어방동)', NULL, 448, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(4, '공간EASY', '경상남도 김해시', '경상남도', NULL, '내외중앙로 35, 4층', NULL, 120, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(4, '김해서부문화센터 하늬홀', '경상남도 김해시', '경상남도', NULL, '율하2로 210(율하동)', NULL, 691, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(4, '명배우 봉하극장 콜로노스', '경상남도 김해시', '경상남도', NULL, '진영읍 본산1로 56번길 30', NULL, 98, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(4, '회현동 소극장', '경상남도 김해시', '경상남도', NULL, '분성로 259, 4층(봉황동)', NULL, 80, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(4, '밀양아리랑아트센터 대공연장', '경상남도 밀양시', '경상남도', NULL, '밀양대공원로 112', NULL, 810, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(4, '밀양아리랑아트센터 소공연장', '경상남도 밀양시', '경상남도', NULL, '밀양대공원로 112', NULL, 256, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(4, '거제문화예술회관 대극장', '경상남도 거제시', '경상남도', NULL, '장승로 145 (장승포동)', NULL, 1209, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(4, '거제문화예술회관 소극장', '경상남도 거제시', '경상남도', NULL, '장승로 145 (장승포동)', NULL, 430, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(4, '거제시청소년수련관 공연장', '경상남도 거제시', '경상남도', NULL, '계룡로 175 (고현동)', NULL, 404, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(4, '거제월드아트서커스', '경상남도 거제시', '경상남도', NULL, '일운면 구조라로 73', NULL, 450, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(5, '양산문화예술회관 대공연장', '경상남도 양산시', '경상남도', NULL, '중앙로 39-1(남부동)', NULL, 834, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(5, '의령군민문화회관 공연장', '경상남도 의령군', '경상남도', NULL, '의령읍 의병로24길 31-1', NULL, 354, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(5, '함안문화예술회관', '경상남도 함안군', '경상남도', NULL, '가야읍 함안대로 619-1', NULL, 510, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(5, '함안문화원 대공연장', '경상남도 함안군', '경상남도', NULL, '가야읍 선왕길 1-1', NULL, 180, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(5, '창녕문화예술회관 대공연장', '경상남도 창녕군', '경상남도', NULL, '창녕읍 우포2로 1189-25', NULL, 496, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(5, '창녕문화예술회관 소공연장', '경상남도 창녕군', '경상남도', NULL, '창녕읍 우포2로 1189-25', NULL, 196, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(5, '고성군문화체육센터 공연장', '경상남도 고성군', '경상남도', NULL, '고성읍 송학고분로 193', NULL, 271, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(5, '남해문화센터', '경상남도 남해군', '경상남도', NULL, '남해읍 선소로 12', NULL, 212, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(5, '하동문화예술회관 대공연장', '경상남도 하동군', '경상남도', NULL, '섬진강대로 2222', NULL, 646, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(5, '하동문화예술회관 소공연장', '경상남도 하동군', '경상남도', NULL, '섬진강대로 2222', NULL, 200, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(5, '산청군문화예술회관(대공연장)', '경상남도 산청군', '경상남도', NULL, '금서면 친환경로2631번길 12', NULL, 497, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(5, '함양문화예술회관 대공연장', '경상남도 함양군', '경상남도', NULL, '함양읍 필봉산길 55', NULL, 486, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(5, '함양문화예술회관 소공연장', '경상남도 함양군', '경상남도', NULL, '함양읍 필봉산길 55', NULL, 205, 0, '기타', '다양한 공연을 위한 현대적인 시설을 갖춘 공연장', NULL, 'INTERNAL'),
(5, '거창문화센터 공연장', '경상남도 거창군', '경상남도', NULL, '거창읍 수남로 2181', NULL, 710, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(5, '거창문화원 상살미홀', '경상남도 거창군', '경상남도', NULL, '거창읍 수남로 2193-40', NULL, 252, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(5, '합천군문화예술회관', '경상남도 합천군', '경상남도', NULL, '합천읍 황강체육공원로 93', NULL, 340, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL'),
(5, '문예회관 대극장', '제주특별자치도 제주시', '제주특별자치도', NULL, '동광로 69', NULL, 828, 0, '기타', '지역 문화 발전에 기여하는 예술 공간', NULL, 'INTERNAL'),
(5, '문예회관 소극장', '제주특별자치도 제주시', '제주특별자치도', NULL, '동광로 69', NULL, 170, 0, '기타', '편안하고 아늑한 분위기의 소극장', NULL, 'INTERNAL'),
(5, '제주아트센터', '제주특별자치도 제주시', '제주특별자치도', NULL, '오남로 231', NULL, 1184, 0, '기타', '클래식 공연에 최적화된 아름다운 홀', NULL, 'INTERNAL'),
(5, '서귀포예술의전당', '제주특별자치도 서귀포시', '제주특별자치도', NULL, '태평로 270', NULL, 992, 0, '기타', '최신 음향 및 조명 시설을 갖춘 다목적 홀', NULL, 'INTERNAL')
ON CONFLICT (name, address) DO NOTHING;