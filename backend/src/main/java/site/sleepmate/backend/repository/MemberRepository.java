package site.sleepmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.sleepmate.backend.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer>{
    Optional<Member> findByMemberSeq(Long memberSeq);
}
