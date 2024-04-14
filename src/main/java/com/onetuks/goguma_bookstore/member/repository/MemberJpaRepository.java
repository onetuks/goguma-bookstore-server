package com.onetuks.goguma_bookstore.member.repository;

import com.onetuks.goguma_bookstore.global.vo.auth.ClientProvider;
import com.onetuks.goguma_bookstore.member.model.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByAuthInfoSocialIdAndAuthInfoClientProvider(
      String socialId, ClientProvider clientProvider);
}
