package com.onetuks.goguma_bookstore.auth.repository;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.vo.ClientProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

  Optional<Member> findBySocialIdAndClientProvider(String socialId, ClientProvider clientProvider);
}
