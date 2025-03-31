package kr.ollsy.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import kr.ollsy.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByProviderId(String providerId);

    boolean existsByNickname(String nickname);
}
