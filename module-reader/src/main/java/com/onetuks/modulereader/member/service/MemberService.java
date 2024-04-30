package com.onetuks.modulereader.member.service;

import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.service.S3Service;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.member.vo.AuthInfo;
import com.onetuks.modulepersistence.member.vo.UserData;
import com.onetuks.modulereader.member.service.dto.param.MemberDefaultAddressEditParam;
import com.onetuks.modulereader.member.service.dto.param.MemberDefaultCashReceiptEditParam;
import com.onetuks.modulereader.member.service.dto.param.MemberEntryInfoParam;
import com.onetuks.modulereader.member.service.dto.param.MemberProfileEditParam;
import com.onetuks.modulereader.member.service.dto.result.MemberCreateResult;
import com.onetuks.modulereader.member.service.dto.result.MemberDefaultAddressEditResult;
import com.onetuks.modulereader.member.service.dto.result.MemberDefaultCashReceiptEditResult;
import com.onetuks.modulereader.member.service.dto.result.MemberEntryInfoResult;
import com.onetuks.modulereader.member.service.dto.result.MemberInfoResult;
import com.onetuks.modulereader.member.service.dto.result.MemberProfileEditResult;
import com.onetuks.modulereader.member.service.event.WithdrawalEventPublisher;
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
                        .profileImgFilePath(FileWrapper.of().getUri())
                        .build())),
        optionalMember.isEmpty());
  }

  @Transactional
  public MemberEntryInfoResult updateMemberInfo(long memberId, MemberEntryInfoParam param) {
    return MemberEntryInfoResult.from(
        getMemberById(memberId)
            .changeNickname(param.nickname())
            .changeAlarmPermission(param.alarmPermission()));
  }

  @Transactional
  public MemberProfileEditResult updateMemberProfile(
      long memberId, MemberProfileEditParam param, FileWrapper profileImgFile) {
    s3Service.putFile(profileImgFile);

    return MemberProfileEditResult.from(
        getMemberById(memberId)
            .changeNickname(param.nickname())
            .changeProfileImgFile(profileImgFile.getUri())
            .changeAlarmPermission(param.alarmPermission())
            .changeDefaultAddressInfo(param.defaultAddress(), param.defaultAddressDetail())
            .changeDefaultCashReceiptInfo(
                param.defaultCashReceiptType(), param.defaultCashReceiptNumber()));
  }

  @Transactional
  public void deleteMember(long memberId, String token) {
    withdrawalEventPublisher.publishWithdrawalEvent(token);

    memberJpaRepository.deleteById(memberId);

    // todo : s3 파일 삭제
  }

  @Transactional
  public MemberDefaultAddressEditResult updateDetaultAddress(
      long memberId, MemberDefaultAddressEditParam memberDefaultAddressParam) {
    return MemberDefaultAddressEditResult.from(
        getMemberById(memberId)
            .changeDefaultAddressInfo(
                memberDefaultAddressParam.defaultAddress(),
                memberDefaultAddressParam.defaultAddressDetail()));
  }

  @Transactional
  public MemberDefaultCashReceiptEditResult updateDefaultCashReceipt(
      long memberId, MemberDefaultCashReceiptEditParam param) {
    return MemberDefaultCashReceiptEditResult.from(
        getMemberById(memberId)
            .changeDefaultCashReceiptInfo(
                param.defaultCashReceiptType(), param.defaultCashReceiptNumber()));
  }

  @Transactional(readOnly = true)
  public MemberInfoResult readMemberInfo(long memberId) {
    return MemberInfoResult.from(getMemberById(memberId));
  }

  private Member getMemberById(long memberId) {
    return memberJpaRepository
        .findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 멤버입니다."));
  }
}
