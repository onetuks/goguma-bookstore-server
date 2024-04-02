package com.onetuks.goguma_bookstore.auth.repository;

import com.onetuks.happyparkingserver.auth.model.Member;
import com.onetuks.happyparkingserver.auth.model.vo.ClientProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findBySocialIdAndClientProvider(String socialId, ClientProvider clientProvider);
}
