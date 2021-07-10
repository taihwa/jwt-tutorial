package com.taihwa.tutorial.repository;

import com.taihwa.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authrities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
