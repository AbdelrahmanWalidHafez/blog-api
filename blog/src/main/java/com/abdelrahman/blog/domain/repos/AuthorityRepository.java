package com.abdelrahman.blog.domain.repos;

import com.abdelrahman.blog.domain.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    Optional<Authority> findByName(String name);
}
