package com.onetuks.modulereader.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulecommon.service.S3Service;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import com.onetuks.modulereader.ReaderIntegrationTest;
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
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class MemberServiceTest extends ReaderIntegrationTest {

  @Autowired private MemberService memberService;
  @Autowired private S3Service s3Service;

  @Autowired private MemberJpaRepository memberJpaRepository;

  private Member savedMember;

  @BeforeEach
  void setUp() {
    savedMember = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
  }

  @Test
  @DisplayName("회원가입 정보를 업데이트한다.")
  void updateMemberInfo_Test() {
    // Given
    MemberEntryInfoParam param = new MemberEntryInfoParam("빠니보틀니", true);

    // When
    MemberEntryInfoResult result = memberService.updateMemberInfo(savedMember.getMemberId(), param);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(savedMember.getMemberId()),
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.alarmPermission()).isEqualTo(param.alarmPermission()));
  }

  @Test
  @DisplayName("중복된 닉네임이 있는 경우 예외를 던진다.")
  void updateMemberInfo_DuplicatedNickname_ExceptionTest() {
    // Given
    Member otherMember = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    MemberEntryInfoParam param = new MemberEntryInfoParam(savedMember.getNickname(), true);

    // When & Then
    assertThrows(
        DataIntegrityViolationException.class,
        () -> {
          memberService.updateMemberInfo(otherMember.getMemberId(), param);
          memberJpaRepository.flush();
        });
  }

  @Test
  @DisplayName("회원 프로필을 수정하면 멤버 정보가 수정되고, 프로필 이미지가 저장된다.")
  void updateMemberProfileTest() {
    // Given
    MemberProfileEditParam param =
        new MemberProfileEditParam(
            "빠니보틀니", true, "강원도 춘천시 중앙로", "킹갓 빠니보틀 생가", CashReceiptType.PERSON, "010-0101-0101");
    FileWrapper fileWrapper =
        FileWrapperFixture.createFile(savedMember.getMemberId(), FileType.PROFILES);

    // When
    MemberProfileEditResult result =
        memberService.updateMemberProfile(savedMember.getMemberId(), param, fileWrapper);

    // Then
    File savedProfileImgFile = s3Service.getFile(fileWrapper.getUri());

    assertAll(
        () -> assertThat(savedProfileImgFile).hasSize(fileWrapper.getMultipartFile().getSize()),
        () -> assertThat(result.memberId()).isEqualTo(savedMember.getMemberId()),
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.alarmPermission()).isEqualTo(param.alarmPermission()),
        () -> assertThat(result.defaultAddress()).isEqualTo(param.defaultAddress()),
        () -> assertThat(result.defaultAddressDetail()).isEqualTo(param.defaultAddressDetail()),
        () -> assertThat(result.defaultCashReceiptType()).isEqualTo(param.defaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReceiptNumber())
                .isEqualTo(param.defaultCashReceiptNumber()));
  }

  @Test
  @DisplayName("멤버 기본 배송지를 수정한다.")
  void updateDetaultAddressTest() {
    // Given
    MemberDefaultAddressEditParam param =
        new MemberDefaultAddressEditParam("강원도 춘천시 중앙로", "킹갓 빠니보틀 생가");

    // When
    MemberDefaultAddressEditResult result =
        memberService.updateDetaultAddress(savedMember.getMemberId(), param);

    // Then
    assertAll(
        () -> assertThat(result.defaultAddress()).isEqualTo(param.defaultAddress()),
        () -> assertThat(result.defaultAddressDetail()).isEqualTo(param.defaultAddressDetail()));
  }

  @Test
  @DisplayName("기본 현금영수증 정보를 수정한다.")
  void changeDefaultCashReceiptInfoTest() {
    // Given
    MemberDefaultCashReceiptEditParam param =
        new MemberDefaultCashReceiptEditParam(CashReceiptType.PERSON, "010-0101-0101");

    // When
    MemberDefaultCashReceiptEditResult result =
        memberService.updateDefaultCashReceipt(savedMember.getMemberId(), param);

    // Then
    assertAll(
        () -> assertThat(result.defaultCashReceiptType()).isEqualTo(param.defaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReciptNumber())
                .isEqualTo(param.defaultCashReceiptNumber()));
  }

  @Test
  @DisplayName("멤버 정보를 조회한다.")
  void readMemberInfoTest() {
    // Given & When
    MemberInfoResult result = memberService.readMemberInfo(savedMember.getMemberId());

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(savedMember.getMemberId()),
        () -> assertThat(result.nickname()).isEqualTo(savedMember.getNickname()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(savedMember.getProfileImgUrl()),
        () -> assertThat(result.alarmPermission()).isEqualTo(savedMember.getAlarmPermission()),
        () -> assertThat(result.defaultAddress()).isEqualTo(savedMember.getDefaultAddress()),
        () ->
            assertThat(result.defaultAddressDetail())
                .isEqualTo(savedMember.getDefaultAddressDetail()),
        () ->
            assertThat(result.defaultCashReceiptType())
                .isEqualTo(savedMember.getDefaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReceiptNumber())
                .isEqualTo(savedMember.getDefaultCashReceiptNumber()));
  }

  @Test
  @DisplayName("존재하지 않는 멤버 조회 시 실패한다.")
  void readMemberInfo_NotExistsMember_ExceptionTest() {
    // Given
    long notExistsMemberId = 999L;

    // When & Then
    assertThrows(
        EntityNotFoundException.class, () -> memberService.readMemberInfo(notExistsMemberId));
  }
}
