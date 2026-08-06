package com.codewithben.Lofau.User.userRepo;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.domain.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username) ;
    long countByActiveTrue();

    long countByVerifiedTrue();

    long countByBannedTrue();


    long countByAccountStatus(AccountStatus accountStatus);

    /**
     * Counts users created after the specified date.
     */
    long countByCreatedAtAfter(LocalDateTime dateTime);


    long countByVerifiedFalse();
}
