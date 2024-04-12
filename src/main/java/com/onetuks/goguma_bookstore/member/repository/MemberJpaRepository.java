package com.onetuks.goguma_bookstore.member.repository;

import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.vo.ClientProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByAuthInfoSocialIdAndAuthInfoClientProvider(
      String socialId, ClientProvider clientProvider);
}
