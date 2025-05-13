package apptive.devlog.post.comment.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CommentCreateRequest(
        @NotBlank(message = "내용은 필수 입력값입니다.")
        String content,
        UUID parentId
) {}
