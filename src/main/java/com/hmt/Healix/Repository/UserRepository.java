package com.hmt.Healix.Repository;

import com.hmt.Healix.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Users> findByEmail(String email);
}
