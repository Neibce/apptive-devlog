package apptive.devlog.post.comment.listener;


import apptive.devlog.notification.NotificationService;
import apptive.devlog.post.comment.event.CommentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class CommentNotificationListener {
    private final NotificationService notificationService;

    @Async("mailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(CommentCreatedEvent event) {

        if (event.isTopLevel()) {
            if (!event.postAuthorUuid().equals(event.commentAuthorUuid())) {
                notificationService.notifyComment(event.postAuthorEmail(), event.postTitle(),
                        event.commentAuthorNickname(), event.commentContent());
            }
            return;
        }

        if (!event.parentCommentAuthorUuid().equals(event.commentAuthorUuid())) {
            notificationService.notifyReplyToComment(event.parentCommentAuthorEmail(), event.postTitle(),
                    event.parentCommentContent(), event.commentAuthorNickname(), event.commentContent());
        }
    }
}
