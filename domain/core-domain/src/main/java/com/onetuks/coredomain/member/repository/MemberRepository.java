package com.onetuks.coredomain.member.repository;

import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coreobj.enums.member.ClientProvider;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {

  Member create(Member member);

  Member read(long memberId);

  Optional<Member> read(String socialId, ClientProvider clientProvider);

  Member update(Member member);

  void delete(long memberId);
}
