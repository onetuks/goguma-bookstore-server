package com.onetuks.modulepersistence.member.repository;

import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

  Optional<MemberEntity> findByAuthInfoSocialIdAndAuthInfoClientProvider(
      String socialId, ClientProvider clientProvider);
}
