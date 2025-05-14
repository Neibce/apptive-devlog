package apptive.devlog.post;

import apptive.devlog.post.dto.PostCreateRequest;
import apptive.devlog.post.dto.PostData;
import apptive.devlog.post.dto.PostListItem;
import apptive.devlog.post.dto.PostUpdateRequest;
import apptive.devlog.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;

    public void createPost(PostCreateRequest request, User user) {
        postRepository.save(
                Post.builder()
                        .title(request.title())
                        .content(request.content())
                        .author(user)
                        .build());
    }

    public PostData getPost(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        return PostData.builder()
                .title(post.getTitle()).content(post.getContent())
                .authorNickname(post.getAuthor().getNickname())
                .createdAt(post.getCreatedAt()).updatedAt(post.getUpdatedAt())
                .isPostOwner(post.getAuthor().equals(user))
                .build();
    }

    public List<PostListItem> getPosts() {
        return postRepository.findByIsDeletedFalseOrderByCreatedAtAsc().stream()
                .map(post -> PostListItem.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .authorNickname(post.getAuthor().getNickname())
                        .createdAt(post.getCreatedAt())
                        .build())
                .toList();
    }

    public void updatePost(Long id, PostUpdateRequest request, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if (!post.getAuthor().equals(user))
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");

        if (request.title() != null)
            post.setTitle(request.title());
        if (request.content() != null)
            post.setContent(request.content());
        postRepository.save(post);
    }

    public void deletePost(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if (!post.getAuthor().equals(user))
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");

        post.setIsDeleted(true);
        postRepository.save(post);
    }
}
