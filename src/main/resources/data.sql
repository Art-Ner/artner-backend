-- 더미 데이터: users (30명 추가)
-- oauth_provider_type: 'KAKAO', 'GOOGLE'
-- user_role: 'USER', 'ADMIN'

-- 기존 데이터와 충돌하지 않도록 ON CONFLICT DO NOTHING 사용
INSERT INTO users (email, phone, oauth_provider, username, nickname, role, created_at, updated_at) VALUES
-- 아티스트 유저들 (1-20)
('park.sungjin@gmail.com', '010-1234-5001', 'GOOGLE', '박성진', '성진', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('lee.sungho@kakao.com', '010-1234-5002', 'KAKAO', '이성호', '록스타샘', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('park.yuna@gmail.com', '010-1234-5003', 'GOOGLE', '박유나', '바이올린퀸', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('choi.minho@kakao.com', '010-1234-5004', 'KAKAO', '최민호', '비트메이커', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jung.sora@gmail.com', '010-1234-5005', 'GOOGLE', '정소라', '소울보이스', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('kang.hyunwoo@kakao.com', '010-1234-5006', 'KAKAO', '강현우', '그루브마스터', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('song.minji@gmail.com', '010-1234-5007', 'GOOGLE', '송민지', '첼로러버', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('han.jiwon@kakao.com', '010-1234-5008', 'KAKAO', '한지원', '일렉트로닉', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('yoon.seojun@gmail.com', '010-1234-5009', 'GOOGLE', '윤서준', '트럼펫맨', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('lim.chaeyoung@kakao.com', '010-1234-5010', 'KAKAO', '임채영', '플루트걸', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('oh.taeyang@gmail.com', '010-1234-5011', 'GOOGLE', '오태양', '힙합홀릭', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('shin.yerin@kakao.com', '010-1234-5012', 'KAKAO', '신예린', '팝디바', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('bae.jihoon@gmail.com', '010-1234-5013', 'GOOGLE', '배지훈', '작곡왕', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('kwon.nari@kakao.com', '010-1234-5014', 'KAKAO', '권나리', '하프천사', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jeon.minsu@gmail.com', '010-1234-5015', 'GOOGLE', '전민수', '색소폰블루스', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('moon.hayoung@kakao.com', '010-1234-5016', 'KAKAO', '문하영', '비올라소녀', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ahn.dongwon@gmail.com', '010-1234-5017', 'GOOGLE', '안동원', '펑크로커', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('seo.jisu@kakao.com', '010-1234-5018', 'KAKAO', '서지수', '컨트리송', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('nam.yubin@gmail.com', '010-1234-5019', 'GOOGLE', '남유빈', '퍼커션킹', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('hong.sanghyun@kakao.com', '010-1234-5020', 'KAKAO', '홍상현', '일렉기타짱', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 공연장 운영자들 (21-25)
('venue.bluenote@gmail.com', '010-2000-1001', 'GOOGLE', '블루노트서울', '블루노트Seoul', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('venue.jazz.alley@kakao.com', '010-2000-1002', 'KAKAO', '재즈앨리', '재즈앨리관리자', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('venue.rock.house@gmail.com', '010-2000-1003', 'GOOGLE', '락하우스', '락하우스코리아', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('venue.classic.hall@kakao.com', '010-2000-1004', 'KAKAO', '클래식홀', '클래식홀매니저', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('venue.indie.stage@gmail.com', '010-2000-1005', 'GOOGLE', '인디스테이지', '인디스테이지공식', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 일반 유저들 (26-28)
('user.fan01@gmail.com', '010-3000-2001', 'GOOGLE', '김팬덤', '음악덕후', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user.fan02@kakao.com', '010-3000-2002', 'KAKAO', '이관객', '공연중독자', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user.fan03@gmail.com', '010-3000-2003', 'GOOGLE', '박관람', '페스티벌고고', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- 관리자 (29-30)
('admin.system@artner.kr', '010-9000-0001', 'GOOGLE', '시스템관리자', 'ArtnerAdmin', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('admin.support@artner.kr', '010-9000-0002', 'KAKAO', '고객지원팀', 'ArtnerSupport', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
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
