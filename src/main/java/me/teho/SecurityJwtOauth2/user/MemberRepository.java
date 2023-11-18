package me.teho.SecurityJwtOauth2.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberById(Long id);

    Optional<Member> findMemberByName(String name);

}
