package com.onetuks.readerdomain.service;

import static com.onetuks.coredomain.util.TestValueProvider.createAddress;
import static com.onetuks.coredomain.util.TestValueProvider.createAddressDetail;
import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static com.onetuks.coredomain.util.TestValueProvider.createNickname;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.member.dto.MemberAuthResult;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.model.vo.AuthInfo;
import com.onetuks.coredomain.util.TestValueProvider;
import com.onetuks.coreobj.FileWrapperFixture;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.UUIDProvider;
import com.onetuks.readerdomain.ReaderDomainIntegrationTest;
import com.onetuks.readerdomain.member.param.MemberPatchParam;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberServiceTest extends ReaderDomainIntegrationTest {

  @Test
  @DisplayName("새로운 멤버를 생성한다.")
  void saveMemberIfNotExists_NotExistsMember_Test() {
    // Given
    AuthInfo authInfo = TestValueProvider.createAuthInfo(RoleType.USER);
    Member member = MemberFixture.create(createId(), RoleType.USER);

    given(memberRepository.read(authInfo.socialId(), authInfo.clientProvider()))
        .willReturn(Optional.empty());
    given(memberRepository.create(any())).willReturn(member);

    // When
    MemberAuthResult result = memberService.createMemberIfNotExists(authInfo);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isInstanceOf(Long.class).isNotNull(),
        () -> assertThat(result.name()).isEqualTo(member.authInfo().name()),
        () -> assertThat(result.socialId()).isEqualTo(member.authInfo().socialId()),
        () -> assertThat(result.clientProvider()).isEqualTo(member.authInfo().clientProvider()),
        () -> assertThat(result.roleTypes()).isEqualTo(member.authInfo().roles()),
        () -> assertThat(result.isNewMember()).isTrue());
  }

  @Test
  @DisplayName("이미 존재하는 멤버는 따로 생성하지 않고 해당 멤버를 반환한다.")
  void saveMemberIfNotExists_ExistsMember_Test() {
    // Given
    AuthInfo authInfo = TestValueProvider.createAuthInfo(RoleType.USER);
    Member member = MemberFixture.create(createId(), RoleType.USER);

    given(memberRepository.read(authInfo.socialId(), authInfo.clientProvider()))
        .willReturn(Optional.of(member));

    // When
    MemberAuthResult result = memberService.createMemberIfNotExists(authInfo);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isInstanceOf(Long.class).isNotNull(),
        () -> assertThat(result.name()).isEqualTo(member.authInfo().name()),
        () -> assertThat(result.socialId()).isEqualTo(member.authInfo().socialId()),
        () -> assertThat(result.clientProvider()).isEqualTo(member.authInfo().clientProvider()),
        () -> assertThat(result.roleTypes()).isEqualTo(member.authInfo().roles()),
        () -> assertThat(result.isNewMember()).isFalse());
  }

  @Test
  @DisplayName("멤버 정보를 조회한다.")
  void readMemberInfoTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);

    given(memberRepository.read(member.memberId())).willReturn(member);

    // When
    Member result = memberService.readMemberDetails(member.memberId());

    // Then
    assertThat(result).isEqualTo(member);
  }

  @Test
  @DisplayName("멤버 프로필 정보를 수정하며, 프로필 이미지가 저장된다.")
  void updateMemberProfileTest() {
    // Given
    MemberPatchParam param =
        new MemberPatchParam(
            createNickname().nicknameValue(), true, createAddress(), createAddressDetail());
    FileWrapper profileImgFile =
        FileWrapperFixture.createFile(FileType.PROFILES, UUIDProvider.provideUUID());

    Member before = MemberFixture.create(createId(), RoleType.USER);
    Member after =
        before.changeMemberProfile(
            param.nickname(),
            param.isAlarmPermitted(),
            profileImgFile.getUri(),
            param.defaultAddress(),
            param.defaultAddressDetail());

    given(memberRepository.read(before.memberId())).willReturn(before);
    given(memberRepository.update(any())).willReturn(after);

    // When
    Member result = memberService.updateMemberProfile(before.memberId(), param, profileImgFile);

    // Then
    assertAll(
        () -> assertThat(result).isEqualTo(after),
        () -> assertThat(result.nickname().nicknameValue()).isEqualTo(param.nickname()),
        () -> assertThat(result.isAlarmPermitted()).isTrue(),
        () -> assertThat(result.profileImgFilePath().getUri()).isEqualTo(profileImgFile.getUri()),
        () -> assertThat(result.defaultAddressInfo().address()).isEqualTo(param.defaultAddress()),
        () ->
            assertThat(result.defaultAddressInfo().addressDetail())
                .isEqualTo(param.defaultAddressDetail()));

    verify(fileRepository, times(1)).deleteFile(before.profileImgFilePath().getUrl());
    verify(fileRepository, times(1)).putFile(profileImgFile);
  }

  @Test
  @DisplayName("멤버 정보를 제거한다.")
  void deletMemberTest() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);

    // When
    memberService.deleteMember(member.memberId());

    // Then
    verify(memberRepository, times(1)).delete(member.memberId());
  }
}
