package com.onetuks.dbstorage.member.repository;

import com.onetuks.coreobj.enums.member.ClientProvider;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

  Optional<MemberEntity> findBySocialIdAndClientProvider(
      String socialId, ClientProvider clientProvider);
}
