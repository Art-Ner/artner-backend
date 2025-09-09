package kr.artner.global.exception;

public enum ErrorStatus {
    INVALID_REFRESHTOKEN("INVALID_REFRESHTOKEN", "유효하지 않은 리프레시 토큰입니다"),
    INVALID_ACCESSTOKEN("INVALID_ACCESSTOKEN", "유효하지 않은 토큰입니다"),
    UNAUTHORIZED("UNAUTHORIZED", "토큰이 없습니다"),
    USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다"),
    MEMBER_NOT_REGISTERED_BY_GOOGLE("MEMBER_NOT_REGISTERED_BY_GOOGLE", "Google로 등록되지 않은 회원입니다"),
    MEMBER_DUPLICATE_BY_EMAIL("MEMBER_DUPLICATE_BY_EMAIL", "이미 존재하는 이메일입니다"),
    MEMBER_DUPLICATE_BY_USERNAME("MEMBER_DUPLICATE_BY_USERNAME", "이미 존재하는 사용자명입니다"),
    MEMBER_NOT_EXIST("MEMBER_NOT_EXIST", "존재하지 않는 회원입니다");

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