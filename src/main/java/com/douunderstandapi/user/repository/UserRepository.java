package com.douunderstandapi.user.repository;

import com.douunderstandapi.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT distinct u.email FROM User u JOIN Subscribe s ON u.id = s.user.id WHERE u.isAllowedNotification = true ")
    List<String> findAllByIsAllowedNotificationAndExistsSubscribeWithCoveringIndex();

    boolean existsByEmail(String email);
}
