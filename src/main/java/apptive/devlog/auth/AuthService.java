package apptive.devlog.auth;

import apptive.devlog.auth.token.RefreshToken;
import apptive.devlog.auth.token.RefreshTokenRepository;
import apptive.devlog.auth.token.TokenProvider;
import apptive.devlog.exception.EmailAlreadyExistsException;
import apptive.devlog.exception.InvalidPasswordException;
import apptive.devlog.exception.NicknameAlreadyExistsException;
import apptive.devlog.user.User;
import apptive.devlog.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apptive.devlog.auth.dto.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("이미 사용중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(request.nickname())) {
            throw new NicknameAlreadyExistsException("이미 사용중인 닉네임입니다.");
        }
        if (!validatePassword(request.password())) {
            throw new InvalidPasswordException("비밀번호는 대소문자, 특수문자 포함 10자 이상이어야 합니다.");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .nickname(request.nickname())
                .birth(request.birth())
                .gender(request.gender())
                .build();
        userRepository.save(user);
    }

    public LoginResult login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));

            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new BadCredentialsException("사용자 없음"));

            String accessTokenString = tokenProvider.generateAccessToken(user.getEmail());
            String refreshTokenString = tokenProvider.generateRefreshToken();
            RefreshToken refreshToken = RefreshToken.builder()
                    .token(refreshTokenString)
                    .userEmail(request.email())
                    .expiry(LocalDateTime.now().plusDays(TokenProvider.REFRESH_TOKEN_TTL))
                    .build();
            refreshTokenRepository.deleteByUserEmail(request.email());
            refreshTokenRepository.save(refreshToken);

            return new LoginResult(accessTokenString, refreshTokenString);
        } catch (BadCredentialsException exception) {
            throw new BadCredentialsException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
    }

    public void logout(String email) {
        refreshTokenRepository.deleteByUserEmail(email);
    }

    public String refresh(String refreshToken) {
        String userEmail = refreshTokenRepository.findByToken(refreshToken).getUserEmail();
        return tokenProvider.generateAccessToken(userEmail);
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{10,}$");
        return pattern.matcher(password).matches();
    }
}
