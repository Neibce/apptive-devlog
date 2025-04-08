package apptive.devlog.auth;

import apptive.devlog.auth.dto.*;
import apptive.devlog.common.CommonResponse;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "회원으로 가입합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "입가 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return CommonResponse.buildResponseEntity(HttpStatus.CREATED, "성공적으로 가입되었습니다.");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "정보 일치 시 AccessToken과 RefreshToken을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        LoginResult loginResult = authService.login(request);
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "로그인 되었습니다.", loginResult);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "RefreshToken을 Revoke합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "로그아웃 되었습니다.");
    }

    @PostMapping("/refresh")
    @Operation(summary = "AccessToken 재발급", description = "AccessToken을 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재발급 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    public ResponseEntity<?> refresh(@RequestBody @Valid RefreshRequest request) {
        String newAccessToken = authService.refresh(request.refreshToken());
        return CommonResponse.buildResponseEntity(HttpStatus.OK, "AccessToken 재발급 되었습니다.",
                Map.of("accessToken", newAccessToken));
    }
}
