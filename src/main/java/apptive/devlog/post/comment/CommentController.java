package apptive.devlog.post.comment;

import apptive.devlog.common.CommonResponse;
import apptive.devlog.post.comment.dto.CommentCreateRequest;
import apptive.devlog.post.comment.dto.CommentData;
import apptive.devlog.post.comment.dto.CommentUpdateRequest;
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
import java.util.UUID;

@RestController
@RequestMapping("/post/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 업로드", description = "댓글 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업로드 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        commentService.createComment(postId, request, userDetails.getUser());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "댓글이 등록되었습니다.");
    }

    @Operation(summary = "댓글 목록 조회", description = "댓글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    @GetMapping
    public ResponseEntity<?> getComments(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<CommentData> data = commentService.getComments(postId, userDetails.getUser());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "성공", data);
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable UUID commentId,
            @RequestBody @Valid CommentUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        commentService.updateComment(commentId, request, userDetails.getUser());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "댓글이 수정되었습니다.");
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable UUID commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        commentService.deleteComment(commentId, userDetails.getUser());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "댓글이 삭제되었습니다.");
    }
}
