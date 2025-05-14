package apptive.devlog.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record CommonResponse<T>(
        @Schema(example = "200")
        int statusCode,
        String message,
        T data
) {
    public static <T> ResponseEntity<CommonResponse<T>> buildResponseEntity(HttpStatus httpStatus, String message) {
        return buildResponseEntity(httpStatus, message, null);
    }

    public static <T> ResponseEntity<CommonResponse<T>> buildResponseEntity(HttpStatus httpStatus, String message, T data) {
        return ResponseEntity.status(httpStatus)
                .body(new CommonResponse<>(httpStatus.value(), message, data));
    }
}
