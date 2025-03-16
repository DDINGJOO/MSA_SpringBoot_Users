package dding.profile.repository;

import dding.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,String>
{
    boolean existsByNickname(String nickname);
}
