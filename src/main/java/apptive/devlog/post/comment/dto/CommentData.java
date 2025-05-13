package apptive.devlog.post.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record CommentData(
        UUID uuid,
        String content,
        String authorNickname,
        Boolean isDeleted,
        Boolean isCommentOwner,
        LocalDateTime createdAt,
        List<CommentData> replies
) {}
