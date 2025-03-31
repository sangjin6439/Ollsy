package kr.ollsy.Auth.Jwt;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    RefreshToken findByUserId(Long userId);

    @Transactional
    @Modifying
    void deleteByUserId(Long userId);
}