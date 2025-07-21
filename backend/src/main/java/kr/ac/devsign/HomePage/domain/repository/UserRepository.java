package kr.ac.devsign.HomePage.domain.repository;

import kr.ac.devsign.HomePage.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByUserId(String userid);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
    boolean existsByPhoneHash(String phoneHash);

    boolean existsByDiscordId(String discordId);

    List<User> findByRole(User.Role role);

    List<User> findByStatusAndWithdrawnAtBefore(User.UserStatus status, LocalDateTime withdrawnAt);
}
