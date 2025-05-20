package apptive.devlog.notification;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void notifyComment(String receiverEmail, String postTitle, String commentAuthor, String commentContent) {
        String abbreviatedPostTitle = StringUtils.abbreviate(postTitle, 15);

        String subject = "[Devlog 댓글 알림] 작성하신 글 '" + abbreviatedPostTitle + "'에 새로운 댓글이 달렸습니다";
        String text = String.format(
                "<h2>작성하신 글 '%s'에 새로운 댓글이 달렸습니다.</h2><br><p>댓글 내용: [%s] %s </p><br>",
                abbreviatedPostTitle, commentAuthor, commentContent);

        sendEmail(receiverEmail, subject, text);
    }

    public void notifyReplyToComment(String receiverEmail, String postTitle, String commentContent, String replyAuthor, String replyContent) {
        String abbreviatedPostTitle = StringUtils.abbreviate(postTitle, 15);
        String abbreviatedCommentContent = StringUtils.abbreviate(commentContent, 15);

        String subject = "[Devlog 대댓글 알림] 작성하신 댓글 '" + abbreviatedCommentContent + "'에 새로운 댓글이 달렸습니다";
        String text = String.format(
                "<h2>'%s' 게시글에 작성하신 댓글 '%s'에 새로운 대댓글이 달렸습니다.</h2><p>대댓글 내용: [%s] %s </p><br>",
                abbreviatedPostTitle, abbreviatedCommentContent,  replyAuthor, replyContent);

        sendEmail(receiverEmail, subject, text);
    }

    public void notifyNewPostToFollowers(String receiverEmail, String postTitle, String author) {
        String abbreviatedPostTitle = StringUtils.abbreviate(postTitle, 15);

        String subject = String.format("[Devlog 새 게시글 알림] %s님이 새 게시글을 작성했습니다 : '%s'", author, abbreviatedPostTitle);
        String text = String.format("<h2>팔로우 중인 %s님이 새 게시글을 작성했습니다 :</h2><p>%s</p>", author, postTitle);

        sendEmail(receiverEmail, subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            helper.setFrom(new InternetAddress(fromEmail, "Devlog", "UTF-8"));

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("메일 전송 실패", e);
        }
    }
}

