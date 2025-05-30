package apptive.devlog.user;


import apptive.devlog.common.CommonResponse;
import apptive.devlog.security.CustomUserDetails;
import apptive.devlog.user.dto.UserDeleteRequest;
import apptive.devlog.user.dto.UserUpdateRequest;
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

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/me")
    @Operation(summary = "회원정보 수정", description = "현재 로그인된 사용자의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 수정 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> updateUser(
            @RequestBody @Valid UserUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.updateUser(userDetails.getUser(), request);
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "회원정보가 수정되었습니다.");
    }

    @DeleteMapping("/me")
    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 사용자를 탈퇴시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> deleteUser(
            @RequestBody @Valid UserDeleteRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deactivateUser(userDetails.getUser(), request.password());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "정상적으로 탈퇴되었습니다.");
    }
}
