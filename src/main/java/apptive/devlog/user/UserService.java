package apptive.devlog.user;

import apptive.devlog.user.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void updateUser(String email, UpdateUserRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.oldPassword()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일 오류"));

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

    public void deleteUser(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일 오류"));
        userRepository.delete(user);
    }
}
