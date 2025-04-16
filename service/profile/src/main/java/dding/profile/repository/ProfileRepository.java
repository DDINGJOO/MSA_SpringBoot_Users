package dding.profile.repository;

import dding.profile.entity.Profile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface ProfileRepository extends JpaRepository<Profile,String>
{
    boolean existsByNickname(String nickname);


    @Query("""
        SELECT p FROM Profile p 
        WHERE (:nickname IS NULL OR p.nickname LIKE %:nickname%)
          AND (:preferred1 IS NULL OR p.preferred1 = :preferred1)
          AND (:preferred2 IS NULL OR p.preferred2 = :preferred2)
    """)
    Page<Profile> findByConditions(
            @Param("nickname") String nickname,
            @Param("preferred1") String preferred1,
            @Param("preferred2") String preferred2,
            Pageable pageable
    );
}
