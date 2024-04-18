package com.onetuks.goguma_bookstore.global.vo.file;

import com.onetuks.goguma_bookstore.global.vo.file.provider.FilePathProvider;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CustomFile {

  private static final String BUCKET_URL = "https://test-bucket-url.com";

  private final String uri;
  private final MultipartFile multipartFile;

  protected CustomFile() {
    this.uri = FilePathProvider.provideDefaultProfileURI();
    this.multipartFile = null;
  }

  protected CustomFile(String uri, MultipartFile multipartFile) {
    this.uri = uri;
    this.multipartFile = multipartFile;
  }

  protected CustomFile(long id, FileType fileType, MultipartFile multipartFile) {
    this.uri = FilePathProvider.provideFileURI(fileType, id);
    this.multipartFile = multipartFile;
  }

  public static CustomFile of() {
    return new CustomFile();
  }

  public static CustomFile of(long id, FileType fileType, MultipartFile multipartFile) {
    return new CustomFile(id, fileType, multipartFile);
  }

  public boolean isNullFile() {
    return multipartFile == null;
  }

  protected String getBucketUrl() {
    return BUCKET_URL;
  }

  public ProfileImgFile toProfileImgFile() {
    return new ProfileImgFile(uri, multipartFile);
  }

  public EscrowServiceFile toEscrowServiceFile() {
    return new EscrowServiceFile(uri, multipartFile);
  }

  public MailOrderSalesFile toMailOrderSalesFile() {
    return new MailOrderSalesFile(uri, multipartFile);
  }

  public CoverImgFile toCoverImgFile() {
    return new CoverImgFile(uri, multipartFile);
  }

  public SampleFile toSampleFile() {
    return new SampleFile(uri, multipartFile);
  }
}
