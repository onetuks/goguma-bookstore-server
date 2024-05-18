package com.onetuks.scmdomain.registration.service;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.repository.AuthorScmRepository;
import com.onetuks.coredomain.book.model.vo.BookConceptualInfo;
import com.onetuks.coredomain.book.model.vo.BookPhysicalInfo;
import com.onetuks.coredomain.book.model.vo.BookPriceInfo;
import com.onetuks.coredomain.book.repository.BookScmRepository;
import com.onetuks.coredomain.file.filepath.CoverImgFilePath;
import com.onetuks.coredomain.file.filepath.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.coredomain.file.filepath.PreviewFilePath.PreviewFilePaths;
import com.onetuks.coredomain.file.filepath.SampleFilePath;
import com.onetuks.coredomain.file.repository.FileRepository;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coredomain.registration.model.vo.ApprovalInfo;
import com.onetuks.coredomain.registration.repository.RegistrationScmRepository;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.FileWrapper.FileWrapperCollection;
import com.onetuks.scmdomain.registration.param.RegistrationCreateParam;
import com.onetuks.scmdomain.registration.param.RegistrationEditParam;
import com.onetuks.scmdomain.verification.webclient.IsbnWebClient;
import com.onetuks.scmdomain.verification.webclient.dto.result.RegistrationIsbnResult;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationScmService {

  private final RegistrationScmRepository registrationScmRepository;
  private final AuthorScmRepository authorScmRepository;
  private final BookScmRepository bookScmRepository;
  private final FileRepository fileRepository;
  private final IsbnWebClient isbnWebClient;

  public RegistrationScmService(
      RegistrationScmRepository registrationScmRepository,
      AuthorScmRepository authorScmRepository,
      BookScmRepository bookScmRepository,
      FileRepository fileRepository,
      IsbnWebClient isbnWebClient) {
    this.registrationScmRepository = registrationScmRepository;
    this.authorScmRepository = authorScmRepository;
    this.bookScmRepository = bookScmRepository;
    this.fileRepository = fileRepository;
    this.isbnWebClient = isbnWebClient;
  }

  @Transactional
  public Registration createRegistration(
      long memberId,
      RegistrationCreateParam param,
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles,
      FileWrapper sampleFile) {
    checkFileValidity(coverImgFile, detailImgFiles, previewFiles, sampleFile);

    fileRepository.putFile(coverImgFile);
    fileRepository.putFile(sampleFile);
    detailImgFiles.fileWrappers().forEach(fileRepository::putFile);
    previewFiles.fileWrappers().forEach(fileRepository::putFile);

    return registrationScmRepository.create(
        new Registration(
            null,
            authorScmRepository.readByMember(memberId),
            ApprovalInfo.init(),
            new BookConceptualInfo(
                param.title(),
                param.oneLiner(),
                param.summary(),
                param.categories(),
                param.publisher(),
                param.isbn()),
            new BookPhysicalInfo(
                param.height(), param.width(), param.coverType(), param.pageCount()),
            new BookPriceInfo(
                param.price(), param.salesRate(), param.isPromotion(), param.stockCount()),
            CoverImgFilePath.of(coverImgFile.getUri()),
            DetailImgFilePaths.of(detailImgFiles.getUris()),
            PreviewFilePaths.of(previewFiles.getUris()),
            SampleFilePath.of(sampleFile.getUri())));
  }

  @Transactional(readOnly = true)
  public Registration readRegistration(long authorId, long registrationId) {
    Registration registration = registrationScmRepository.read(registrationId);

    if (registration.author().authorId() != authorId) {
      throw new ApiAccessDeniedException("해당 신간등록에 대한 권한이 없는 작가입니다.");
    }

    return registration;
  }

  @Transactional(readOnly = true)
  public List<Registration> readAllRegistrations() {
    return registrationScmRepository.readAll();
  }

  @Transactional(readOnly = true)
  public List<Registration> readAllRegistrationsByAuthor(long memberId, long authorId) {
    Author author = authorScmRepository.read(authorId);
    if (author.member().memberId() != memberId) {
      throw new ApiAccessDeniedException("해당 신간등록에 대한 권한이 없는 작가입니다.");
    }

    return registrationScmRepository.readAll(authorId);
  }

  @Transactional
  public Registration updateRegistrationApprovalInfo(
      long registrationId, boolean isApproved, String approvalMemo) {
    Registration registration =
        registrationScmRepository.update(
            registrationScmRepository
                .read(registrationId)
                .changeApprovalInfo(isApproved, approvalMemo));

    if (isApproved) {
      bookScmRepository.create(registration);
    }

    return registration;
  }

  @Transactional
  public Registration updateRegistration(
      long registrationId,
      RegistrationEditParam param,
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles,
      FileWrapper sampleFile) {
    Registration registration = registrationScmRepository.read(registrationId);

    replaceIfValidFile(coverImgFile, detailImgFiles, previewFiles, sampleFile, registration);

    return registrationScmRepository.update(
        registration.changeRegistration(
            param.oneLiner(),
            param.summary(),
            param.categories(),
            param.price(),
            param.salesRate(),
            param.isPromotion(),
            param.stockCount(),
            CoverImgFilePath.of(coverImgFile.getUri()),
            DetailImgFilePaths.of(detailImgFiles.getUris()),
            PreviewFilePaths.of(previewFiles.getUris()),
            SampleFilePath.of(sampleFile.getUri())));
  }

  @Transactional
  public void deleteRegistration(long memberId, long registrationId) {
    Registration registration = registrationScmRepository.read(registrationId);

    if (registration.author().member().memberId() != memberId) {
      throw new ApiAccessDeniedException("해당 신간등록을 제거할 권한이 없는 멤버입니다.");
    }

    fileRepository.deleteFile(registration.coverImgFilePath().getUrl());
    fileRepository.deleteFile(registration.sampleFilePath().getUrl());
    registration.detailImgFilePaths().getUrls().forEach(fileRepository::deleteFile);
    registration.previewFilePaths().getUrls().forEach(fileRepository::deleteFile);

    registrationScmRepository.delete(registrationId);
  }

  @Transactional
  public RegistrationIsbnResult verifyIsbn(String isbn) {
    return isbnWebClient.requestData(isbn);
  }

  private void checkFileValidity(
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles,
      FileWrapper sampleFile) {
    if (coverImgFile.isNullFile()
        || sampleFile.isNullFile()
        || detailImgFiles.isEmpty()
        || previewFiles.isEmpty()) {
      throw new IllegalArgumentException("신간 등록에 필요한 파일이 존재하지 않습니다.");
    }
  }

  private void replaceIfValidFile(
      FileWrapper coverImgFile,
      FileWrapperCollection detailImgFiles,
      FileWrapperCollection previewFiles,
      FileWrapper sampleFile,
      Registration registration) {
    if (!coverImgFile.isNullFile()) {
      fileRepository.putFile(coverImgFile);
    }
    if (!sampleFile.isNullFile()) {
      fileRepository.putFile(sampleFile);
    }
    if (!detailImgFiles.isEmpty()) {
      registration.detailImgFilePaths().getUrls().forEach(fileRepository::deleteFile);
      detailImgFiles.fileWrappers().forEach(fileRepository::putFile);
    }
    if (!previewFiles.isEmpty()) {
      registration.previewFilePaths().getUrls().forEach(fileRepository::deleteFile);
      previewFiles.fileWrappers().forEach(fileRepository::putFile);
    }
  }
}
