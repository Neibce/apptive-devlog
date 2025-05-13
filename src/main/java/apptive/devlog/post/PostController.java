package apptive.devlog.post;

import apptive.devlog.common.CommonResponse;
import apptive.devlog.post.dto.PostCreateRequest;
import apptive.devlog.post.dto.PostData;
import apptive.devlog.post.dto.PostListItem;
import apptive.devlog.post.dto.PostUpdateRequest;
import apptive.devlog.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    @Operation(summary = "게시글 업로드", description = "게시글 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업로드 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> createPost(
            @RequestBody @Valid PostCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.createPost(request, userDetails.getUser());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "게시글이 업로드 되었습니다.");
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시글 가져오기", description = "게시글 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> getPost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostData postData = postService.getPost(id, userDetails.getUser());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "성공", postData);
    }

    @GetMapping
    @Operation(summary = "게시글 목록 가져오기", description = "게시글 목록 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> getPosts() {
        List<PostListItem> posts = postService.getPosts();
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "성공", posts);
    }


    @PatchMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid PostUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.updatePost(id, request, userDetails.getUser());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "게시글이 수정되었습니다.");
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.deletePost(postId, userDetails.getUser());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "게시글이 삭제되었습니다.");
    }
}
