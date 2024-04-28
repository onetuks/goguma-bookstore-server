package com.onetuks.modulepersistence.member.repository;

import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.member.model.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByAuthInfoSocialIdAndAuthInfoClientProvider(
      String socialId, ClientProvider clientProvider);
}
