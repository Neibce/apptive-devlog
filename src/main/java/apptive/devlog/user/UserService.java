package apptive.devlog.user;

import apptive.devlog.auth.token.RefreshTokenRepository;
import apptive.devlog.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void updateUser(User user, UserUpdateRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), request.oldPassword()));

        if (request.birth() != null)
            user.setBirth(request.birth());
        if (request.gender() != null)
            user.setGender(request.gender());
        if (request.name() != null)
            user.setName(request.name());
        if (request.newPassword() != null)
            user.setPassword(passwordEncoder.encode(request.oldPassword()));
        if (request.nickname() != null)
            user.setNickname(request.nickname());
        userRepository.save(user);
    }

    public void deactivateUser(User user, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), password));

        user.setActive(false);
        userRepository.save(user);

        refreshTokenRepository.deleteByUserEmail(user.getEmail());
    }
}
