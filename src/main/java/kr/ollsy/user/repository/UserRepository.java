package kr.ollsy.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

import javax.swing.text.html.Option;

import kr.ollsy.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByProviderId(String providerId);
    Optional<User> findByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}
