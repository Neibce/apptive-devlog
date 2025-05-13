package apptive.devlog.post.comment;

import apptive.devlog.post.Post;
import apptive.devlog.post.PostRepository;
import apptive.devlog.post.comment.dto.CommentCreateRequest;
import apptive.devlog.post.comment.dto.CommentData;
import apptive.devlog.post.comment.dto.CommentUpdateRequest;
import apptive.devlog.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void createComment(Long postId, CommentCreateRequest request, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Comment parent = (request.parentId() != null)
                ? commentRepository.findByUuidAndIsDeleted(request.parentId(), false)
                .orElseThrow(() -> new IllegalArgumentException("상위 댓글이 없습니다."))
                : null;

        Comment comment = Comment.builder()
                .content(request.content())
                .post(post)
                .parent(parent)
                .author(user)
                .build();

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentData> getComments(Long postId, User user) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return commentRepository.findByPostOrderByCreatedAtAsc(post).stream()
                .filter(comment -> comment.getParent() == null && (!comment.isDeleted() || !comment.getReplies().isEmpty()))
                .map(comment -> toDto(comment, user))
                .toList();
    }

    private CommentData toDto(Comment comment, User user) {
        CommentData.CommentDataBuilder commentDataBuilder = CommentData.builder()
                .uuid(comment.getUuid())
                .content(comment.isDeleted() ? "(삭제된 댓글)" : comment.getContent())
                .isDeleted(comment.isDeleted())
                .isCommentOwner(comment.getAuthor().equals(user))
                .replies(comment.getReplies().stream()
                        .filter(reply -> !reply.isDeleted() || !reply.getReplies().isEmpty())
                        .map(reply -> toDto(reply, user))
                        .toList());

        if (!comment.isDeleted()) {
            commentDataBuilder = commentDataBuilder.authorNickname(comment.getAuthor().getNickname())
                    .createdAt(comment.getCreatedAt());
        }

        return commentDataBuilder.build();
    }

    public void updateComment(UUID commentId,
                              CommentUpdateRequest req,
                              User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다."));

        if (!comment.getAuthor().equals(user))
            throw new IllegalArgumentException("권한이 없습니다.");

        comment.setContent(req.content());
    }

    public void deleteComment(UUID commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getAuthor().equals(user))
            throw new IllegalArgumentException("권한이 없습니다.");

        comment.setDeleted(true);
    }

}
