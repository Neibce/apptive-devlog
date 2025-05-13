package apptive.devlog.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostListItem(
        Long id,
        String title,
        String authorNickname,
        LocalDateTime createdAt
) {
}
