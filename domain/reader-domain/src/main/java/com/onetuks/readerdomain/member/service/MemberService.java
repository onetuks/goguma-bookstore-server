package com.onetuks.readerdomain.member.service;

import com.onetuks.coredomain.file.filepath.ProfileImgFilePath;
import com.onetuks.coredomain.file.repository.FileRepository;
import com.onetuks.coredomain.member.dto.MemberAuthResult;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.model.vo.AuthInfo;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coreobj.vo.FilePathProvider;
import com.onetuks.coreobj.vo.FileWrapper;
import com.onetuks.readerdomain.member.param.MemberEditParam;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final FileRepository fileRepository;

  public MemberService(MemberRepository memberRepository, FileRepository fileRepository) {
    this.memberRepository = memberRepository;
    this.fileRepository = fileRepository;
  }

  @Transactional
  public MemberAuthResult createMemberIfNotExists(AuthInfo authInfo) {
    Optional<Member> optionalMember =
        memberRepository.read(authInfo.socialId(), authInfo.clientProvider());

    Member member =
        optionalMember.orElseGet(
            () ->
                memberRepository.create(
                    Member.builder()
                        .authInfo(authInfo)
                        .profileImgFilePath(
                            new ProfileImgFilePath(FilePathProvider.provideDefaultProfileURI()))
                        .build()));

    return new MemberAuthResult(
        member.memberId(),
        member.authInfo().name(),
        member.authInfo().socialId(),
        member.authInfo().clientProvider(),
        member.authInfo().roles(),
        optionalMember.isEmpty());
  }

  @Transactional(readOnly = true)
  public Member readMemberDetails(long memberId) {
    return memberRepository.read(memberId);
  }

  @Transactional
  public Member updateMemberProfile(
      long memberId, MemberEditParam param, FileWrapper profileImgFile) {
    Member member = memberRepository.read(memberId);

    fileRepository.deleteFile(member.profileImgFilePath().getUrl());
    fileRepository.putFile(profileImgFile);

    return memberRepository.update(
        member.changeMemberProfile(
            param.nickname(),
            param.isAlarmPermitted(),
            profileImgFile.getUri(),
            param.defaultAddress(),
            param.defaultAddressDetail()));
  }

  @Transactional
  public void deleteMember(long memberId) {
    memberRepository.delete(memberId);

    // todo : s3 파일 삭제
  }
}
