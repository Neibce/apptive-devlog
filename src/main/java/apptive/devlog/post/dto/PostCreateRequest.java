package apptive.devlog.post.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCreateRequest(
        @NotBlank(message = "제목은 필수 입력값입니다.")
        String title,

        @NotBlank(message = "내용은 필수 입력값입니다.")
        String content
) { }
