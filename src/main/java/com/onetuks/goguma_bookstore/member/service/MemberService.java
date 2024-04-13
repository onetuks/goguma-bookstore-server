package com.onetuks.goguma_bookstore.member.service;

import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberDefaultAddressEditParam;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberEntryInfoParam;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberProfileEditParam;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberCreateResult;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberDefaultAddressEditResult;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberEntryInfoResult;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberProfileEditResult;
import com.onetuks.goguma_bookstore.member.service.event.WithdrawalEventPublisher;
import com.onetuks.goguma_bookstore.member.vo.AuthInfo;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

  private final MemberJpaRepository memberJpaRepository;
  private final S3Service s3Service;
  private final WithdrawalEventPublisher withdrawalEventPublisher;

  public MemberService(
      MemberJpaRepository memberJpaRepository,
      S3Service s3Service,
      WithdrawalEventPublisher withdrawalEventPublisher) {
    this.memberJpaRepository = memberJpaRepository;
    this.s3Service = s3Service;
    this.withdrawalEventPublisher = withdrawalEventPublisher;
  }

  @Transactional
  public MemberCreateResult saveMemberIfNotExists(UserData userData) {
    Optional<Member> optionalMember =
        memberJpaRepository.findByAuthInfoSocialIdAndAuthInfoClientProvider(
            userData.socialId(), userData.clientProvider());

    return MemberCreateResult.from(
        optionalMember.orElseGet(
            () ->
                memberJpaRepository.save(
                    Member.builder()
                        .authInfo(AuthInfo.from(userData))
                        .profileImgFile(CustomFile.of().toProfileImgFile())
                        .build())),
        optionalMember.isEmpty());
  }

  @Transactional
  public MemberEntryInfoResult updateMemberInfo(long memberId, MemberEntryInfoParam param) {
    return MemberEntryInfoResult.from(
        getMemberById(memberId)
            .updateNickname(param.nickname())
            .updateAlarmPermission(param.alarmPermission()));
  }

  @Transactional
  public MemberProfileEditResult updateMemberProfile(
      long memberId, MemberProfileEditParam param, CustomFile customFile) {
    s3Service.putFile(customFile);

    return MemberProfileEditResult.from(
        getMemberById(memberId)
            .updateNickname(param.nickname())
            .updateProfileImgFile(customFile.toProfileImgFile())
            .updateAlarmPermission(param.alarmPermission())
            .updateDefaultAddressInfo(param.defaultAddress(), param.defaultAddressDetail())
            .updateDefaultCashReceiptInfo(
                param.defaultCashReceiptType(), param.defaultCashReceiptNumber()));
  }

  @Transactional
  public void deleteMember(long memberId, String token) {
    withdrawalEventPublisher.publishWithdrawalEvent(token);

    memberJpaRepository.deleteById(memberId);
  }

  @Transactional
  public MemberDefaultAddressEditResult updateDetaultAddress(
      long memberId, MemberDefaultAddressEditParam memberDefaultAddressParam) {
    return MemberDefaultAddressEditResult.from(
        getMemberById(memberId)
            .updateDefaultAddressInfo(
                memberDefaultAddressParam.defaultAddress(),
                memberDefaultAddressParam.defaultAddressDetail()));
  }

  private Member getMemberById(long memberId) {
    return memberJpaRepository
        .findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));
  }
}
