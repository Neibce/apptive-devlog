package apptive.devlog.auth.dto;

public record LoginResult(
        String accessToken,
        String refreshToken
) {
}
