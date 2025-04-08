package apptive.devlog.auth.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    RefreshToken findByToken(String token);
    void deleteByUserEmail(String userEmail);
}
