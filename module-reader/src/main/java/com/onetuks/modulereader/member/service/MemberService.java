package com.onetuks.modulereader.member.service;

import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.service.S3Service;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.member.service.dto.param.MemberDefaultAddressEditParam;
import com.onetuks.modulereader.member.service.dto.param.MemberDefaultCashReceiptEditParam;
import com.onetuks.modulereader.member.service.dto.param.MemberEntryInfoParam;
import com.onetuks.modulereader.member.service.dto.param.MemberProfileEditParam;
import com.onetuks.modulereader.member.service.dto.result.MemberDefaultAddressEditResult;
import com.onetuks.modulereader.member.service.dto.result.MemberDefaultCashReceiptEditResult;
import com.onetuks.modulereader.member.service.dto.result.MemberEntryInfoResult;
import com.onetuks.modulereader.member.service.dto.result.MemberInfoResult;
import com.onetuks.modulereader.member.service.dto.result.MemberProfileEditResult;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

  private final MemberJpaRepository memberJpaRepository;
  private final S3Service s3Service;

  public MemberService(MemberJpaRepository memberJpaRepository, S3Service s3Service) {
    this.memberJpaRepository = memberJpaRepository;
    this.s3Service = s3Service;
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
