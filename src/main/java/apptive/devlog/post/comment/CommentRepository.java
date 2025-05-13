package apptive.devlog.post.comment;

import apptive.devlog.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Optional<Comment> findByUuidAndIsDeleted(UUID uuid, boolean deleted);
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
}

