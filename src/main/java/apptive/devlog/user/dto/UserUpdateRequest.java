package apptive.devlog.user.dto;

import apptive.devlog.user.User;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UserUpdateRequest(
        @NotBlank(message = "현재 비밀번호는 필수 입력값입니다.")
        String oldPassword,

        String newPassword,

        String name,

        String nickname,

        LocalDate birth,

        User.Gender gender
) {
}
