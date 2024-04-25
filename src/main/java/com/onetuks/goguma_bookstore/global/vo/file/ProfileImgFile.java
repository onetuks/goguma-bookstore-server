package com.onetuks.goguma_bookstore.global.vo.file;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ProfileImgFile extends CustomFile {

  @Column(name = "profile_img_uri", nullable = false)
  private String profileImgUri;

  public ProfileImgFile(String profileImgUri, MultipartFile multipartFile) {
    super(profileImgUri, multipartFile);
    this.profileImgUri = profileImgUri;
  }

  public String getProfileImgUrl() {
    return getBucketUrl() + getProfileImgUri();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ProfileImgFile that = (ProfileImgFile) o;
    return Objects.equals(profileImgUri, that.profileImgUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), profileImgUri);
  }
}
