package kr.artner.response;

public record ApiResponse<T>(
        Boolean success,
        String message,
        T result) {

    public static final ApiResponse<Void> OK = new ApiResponse<>(true, "성공", null);

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(true, "성공", result);
    }

    public static <T> ApiResponse<T> success(String message, T result) {
        return new ApiResponse<>(true, message, result);
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> failure(String message, T result) {
        return new ApiResponse<>(false, message, result);
    }
}
