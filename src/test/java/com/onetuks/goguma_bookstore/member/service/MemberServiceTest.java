package com.onetuks.goguma_bookstore.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;
import com.onetuks.goguma_bookstore.fixture.CustomFileFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberDefaultAddressEditParam;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberDefaultCashReceiptEditParam;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberEntryInfoParam;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberProfileEditParam;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberCreateResult;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberDefaultAddressEditResult;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberDefaultCashReceiptEditResult;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberEntryInfoResult;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberInfoResult;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberProfileEditResult;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;
import jakarta.persistence.EntityNotFoundException;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class MemberServiceTest extends IntegrationTest {

  @Autowired private MemberService memberService;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private S3Service s3Service;

  @Test
  @DisplayName("새로운 멤버를 생성한다.")
  void saveMemberIfNotExists_NotExistsMember_Test() {
    // Given
    UserData userData = MemberFixture.createUserData(RoleType.USER);

    // When
    MemberCreateResult result = memberService.saveMemberIfNotExists(userData);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isInstanceOf(Long.class).isNotNull(),
        () -> assertThat(result.name()).isEqualTo(userData.name()),
        () -> assertThat(result.socialId()).isEqualTo(userData.socialId()),
        () -> assertThat(result.clientProvider()).isEqualTo(userData.clientProvider()),
        () -> assertThat(result.roleType()).isEqualTo(userData.roleType()),
        () -> assertThat(result.isNewMember()).isTrue());
  }

  @Test
  @DisplayName("이미 존재하는 멤버는 따로 생성하지 않고 해당 멤버를 반환한다.")
  void saveMemberIfNotExists_ExistsMember_Test() {
    // Given
    UserData userData = MemberFixture.createUserData(RoleType.USER);
    memberService.saveMemberIfNotExists(userData);

    // When
    MemberCreateResult result = memberService.saveMemberIfNotExists(userData);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isInstanceOf(Long.class).isNotNull(),
        () -> assertThat(result.name()).isEqualTo(userData.name()),
        () -> assertThat(result.socialId()).isEqualTo(userData.socialId()),
        () -> assertThat(result.clientProvider()).isEqualTo(userData.clientProvider()),
        () -> assertThat(result.roleType()).isEqualTo(userData.roleType()),
        () -> assertThat(result.isNewMember()).isFalse());
  }

  @Test
  @DisplayName("회원가입 정보를 업데이트한다.")
  void updateMemberInfo_Test() {
    // Given
    UserData userData = MemberFixture.createUserData(RoleType.USER);
    MemberCreateResult savedMember = memberService.saveMemberIfNotExists(userData);
    MemberEntryInfoParam param = new MemberEntryInfoParam("빠니보틀니", true);

    // When
    MemberEntryInfoResult result = memberService.updateMemberInfo(savedMember.memberId(), param);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(savedMember.memberId()),
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.alarmPermission()).isEqualTo(param.alarmPermission()));
  }

  @Test
  @DisplayName("중복된 닉네임이 있는 경우 예외를 던진다.")
  void updateMemberInfo_DuplicatedNickname_ExceptionTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    memberJpaRepository.flush();

    UserData userData = MemberFixture.createUserData(RoleType.USER);
    MemberCreateResult createResult = memberService.saveMemberIfNotExists(userData);
    MemberEntryInfoParam param = new MemberEntryInfoParam(member.getNickname(), true);

    // When & Then
    assertThrows(
        DataIntegrityViolationException.class,
        () -> {
          memberService.updateMemberInfo(createResult.memberId(), param);
          memberJpaRepository.flush();
        });
  }

  @Test
  @DisplayName("회원 프로필을 수정하면 멤버 정보가 수정되고, 프로필 이미지가 저장된다.")
  void updateMemberProfileTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    MemberProfileEditParam param =
        new MemberProfileEditParam(
            "빠니보틀니", true, "강원도 춘천시 중앙로", "킹갓 빠니보틀 생가", CashReceiptType.PERSON, "010-0101-0101");
    CustomFile customFile = CustomFileFixture.createFile(member.getMemberId(), FileType.PROFILES);

    // When
    MemberProfileEditResult result =
        memberService.updateMemberProfile(member.getMemberId(), param, customFile);

    // Then
    File savedProfileImgFile = s3Service.getFile(customFile.getUri());

    assertAll(
        () -> assertThat(savedProfileImgFile).hasSize(customFile.getMultipartFile().getSize()),
        () -> assertThat(result.memberId()).isEqualTo(member.getMemberId()),
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
  @DisplayName("회원탈퇴한다.")
  void deletMemberTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    String token =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMTc1MTk1Nzc5NTA1NTg4NDczMjYiLCJsb2dpbklkIjozLCJyb2xlIjoiVVNFUiIsImV4cCI6MTcxMzAzOTU4NiwiaXNzIjoiZ29ndW1hIiwiaWF0IjoxNzEyOTUzMTg2fQ.oIxlLpioIkXI_Qr32HMuABHXyLjZQqYAclORQ8RZ7AI";
    memberJpaRepository.flush();

    // When
    memberService.deleteMember(member.getMemberId(), token);

    // Then
    boolean result = memberJpaRepository.existsById(member.getMemberId());

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("멤버 기본 배송지를 수정한다.")
  void updateDetaultAddressTest() {
    // Given
    Member save = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    MemberDefaultAddressEditParam param =
        new MemberDefaultAddressEditParam("강원도 춘천시 중앙로", "킹갓 빠니보틀 생가");

    // When
    MemberDefaultAddressEditResult result =
        memberService.updateDetaultAddress(save.getMemberId(), param);

    // Then
    assertAll(
        () -> assertThat(result.defaultAddress()).isEqualTo(param.defaultAddress()),
        () -> assertThat(result.defaultAddressDetail()).isEqualTo(param.defaultAddressDetail()));
  }

  @Test
  @DisplayName("기본 현금영수증 정보를 수정한다.")
  void updateDefaultCashReceiptInfoTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    MemberDefaultCashReceiptEditParam param =
        new MemberDefaultCashReceiptEditParam(CashReceiptType.PERSON, "010-0101-0101");

    // When
    MemberDefaultCashReceiptEditResult result =
        memberService.updateDefaultCashReceipt(member.getMemberId(), param);

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
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    memberJpaRepository.flush();

    // When
    MemberInfoResult result = memberService.readMemberInfo(member.getMemberId());

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(member.getMemberId()),
        () -> assertThat(result.nickname()).isEqualTo(member.getNickname()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(member.getProfileImgUrl()),
        () -> assertThat(result.alarmPermission()).isEqualTo(member.getAlarmPermission()),
        () -> assertThat(result.defaultAddress()).isEqualTo(member.getDefaultAddress()),
        () -> assertThat(result.defaultAddressDetail()).isEqualTo(member.getDefaultAddressDetail()),
        () ->
            assertThat(result.defaultCashReceiptType())
                .isEqualTo(member.getDefaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReceiptNumber())
                .isEqualTo(member.getDefaultCashReceiptNumber()));
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
