package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.global.vo.auth.ClientProvider;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

  Optional<MemberEntity> findByAuthInfoSocialIdAndAuthInfoClientProvider(
      String socialId, ClientProvider clientProvider);
}
