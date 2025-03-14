package dding.auth.repository;

import dding.auth.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository  extends JpaRepository<Auth, String> {
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<Auth> findByLoginId(String loginId);
}
