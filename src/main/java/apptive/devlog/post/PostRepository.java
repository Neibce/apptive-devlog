package apptive.devlog.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndIsDeletedFalse(Long id);
    List<Post> findByIsDeletedFalseOrderByCreatedAtAsc();
}
