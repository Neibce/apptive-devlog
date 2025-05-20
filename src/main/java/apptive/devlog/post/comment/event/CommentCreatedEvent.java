package apptive.devlog.post.comment.event;

import java.util.UUID;

public record CommentCreatedEvent(
        boolean isTopLevel,

        String postTitle,
        String postAuthorEmail,
        UUID postAuthorUuid,

        UUID parentCommentAuthorUuid,
        String parentCommentAuthorEmail,
        String parentCommentContent,

        UUID commentAuthorUuid,
        String commentAuthorNickname,
        String commentContent
) {}

