package com.onetuks.dbstorage.member.repository;

import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coreobj.enums.member.ClientProvider;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberEntityRepository implements MemberRepository {

  private final MemberJpaRepository memberJpaRepository;
  private final MemberConverter converter;

  public MemberEntityRepository(
      MemberJpaRepository memberJpaRepository, MemberConverter converter) {
    this.memberJpaRepository = memberJpaRepository;
    this.converter = converter;
  }

  @Override
  public Member create(Member member) {
    return converter.toDomain(memberJpaRepository.save(converter.toEntity(member)));
  }

  @Override
  public Member read(long memberId) {
    return converter.toDomain(
        memberJpaRepository
            .findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다.")));
  }

  @Override
  public Optional<Member> read(String socialId, ClientProvider clientProvider) {
    return memberJpaRepository
        .findBySocialIdAndClientProvider(socialId, clientProvider)
        .map(converter::toDomain);
  }

  @Override
  public Member update(Member member) {
    return converter.toDomain(memberJpaRepository.save(converter.toEntity(member)));
  }

  @Override
  public void delete(long memberId) {
    memberJpaRepository.deleteById(memberId);
  }
}
