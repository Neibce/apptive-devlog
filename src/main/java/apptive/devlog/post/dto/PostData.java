package apptive.devlog.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostData(
        String title,
        String content,
        String authorNickname,
        Boolean isPostOwner,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
