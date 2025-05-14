package apptive.devlog.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDeleteRequest(
        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        String password
) { }
