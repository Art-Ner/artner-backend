package kr.artner.global.exception;

public enum ErrorStatus {
    INVALID_REFRESHTOKEN("INVALID_REFRESHTOKEN", "유효하지 않은 리프레시 토큰입니다"),
    INVALID_ACCESSTOKEN("INVALID_ACCESSTOKEN", "유효하지 않은 토큰입니다"),
    UNAUTHORIZED("UNAUTHORIZED", "토큰이 없습니다"),
    USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다"),
    MEMBER_NOT_REGISTERED_BY_GOOGLE("MEMBER_NOT_REGISTERED_BY_GOOGLE", "Google로 등록되지 않은 회원입니다"),
    MEMBER_DUPLICATE_BY_EMAIL("MEMBER_DUPLICATE_BY_EMAIL", "이미 존재하는 이메일입니다"),
    MEMBER_DUPLICATE_BY_USERNAME("MEMBER_DUPLICATE_BY_USERNAME", "이미 존재하는 사용자명입니다"),
    MEMBER_NOT_EXIST("MEMBER_NOT_EXIST", "존재하지 않는 회원입니다"),
    GOOGLE_OAUTH_ERROR("GOOGLE_OAUTH_ERROR", "구글 로그인 중 오류가 발생했습니다."),
    KAKAO_OAUTH_ERROR("KAKAO_OAUTH_ERROR", "카카오 로그인 중 오류가 발생했습니다."),
    ARTIST_PROFILE_NOT_FOUND("ARTIST_PROFILE_NOT_FOUND", "아티스트 프로필을 찾을 수 없습니다."),
    ARTIST_PROFILE_ALREADY_EXISTS("ARTIST_PROFILE_ALREADY_EXISTS", "이미 아티스트 프로필이 존재합니다."),
    FILMOGRAPHY_NOT_FOUND("FILMOGRAPHY_NOT_FOUND", "필모그래피를 찾을 수 없습니다."),
    CONCERT_HISTORY_NOT_FOUND("CONCERT_HISTORY_NOT_FOUND", "공연 이력을 찾을 수 없습니다."),
    VENUE_ADMIN_PROFILE_NOT_FOUND("VENUE_ADMIN_PROFILE_NOT_FOUND", "공간 사업자 프로필을 찾을 수 없습니다."),
    VENUE_ADMIN_PROFILE_ALREADY_EXISTS("VENUE_ADMIN_PROFILE_ALREADY_EXISTS", "이미 공간 사업자 프로필이 존재합니다.");

    private final String code;
    private final String message;

    ErrorStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}