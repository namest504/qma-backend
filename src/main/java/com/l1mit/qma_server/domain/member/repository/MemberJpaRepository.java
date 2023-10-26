package com.l1mit.qma_server.domain.member.repository;

import com.l1mit.qma_server.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

}
