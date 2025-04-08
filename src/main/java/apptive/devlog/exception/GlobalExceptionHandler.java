package apptive.devlog.exception;


import apptive.devlog.common.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<CommonResponse<Void>> handleEmailAlreadyExists(EmailAlreadyExistsException exception) {
        return CommonResponse.buildResponseEntity(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ResponseEntity<CommonResponse<Void>> handleInvalidPassword(NicknameAlreadyExistsException exception) {
        return CommonResponse.buildResponseEntity(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<CommonResponse<Void>> handleInvalidPassword(InvalidPasswordException exception) {
        return CommonResponse.buildResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> handleValidationError(MethodArgumentNotValidException exception) {
        return CommonResponse.buildResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getBindingResult().getFieldErrors().stream().findFirst().orElse(null).getDefaultMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CommonResponse<Void>> handleValidationError(BadCredentialsException exception) {
        return CommonResponse.buildResponseEntity(HttpStatus.UNAUTHORIZED, "인증 정보가 잘못되었습니다.");
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleAll(Exception exception) {
        return CommonResponse.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}

