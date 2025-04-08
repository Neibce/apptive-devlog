package apptive.devlog.auth.dto;

import apptive.devlog.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        String password,

        @NotBlank(message = "이름은 필수 입력값입니다.")
        String name,

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        String nickname,

        @NotNull(message = "생년월일은 필수 입력값입니다.")
        LocalDate birth,

        @NotNull(message = "성별은 필수 입력값입니다.")
        User.Gender gender
){}
