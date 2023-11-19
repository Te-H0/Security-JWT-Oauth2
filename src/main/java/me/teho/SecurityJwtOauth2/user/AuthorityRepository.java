package me.teho.SecurityJwtOauth2.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByAuthorityName(String name);
}
