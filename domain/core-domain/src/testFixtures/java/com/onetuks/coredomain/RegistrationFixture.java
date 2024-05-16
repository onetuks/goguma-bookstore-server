package com.onetuks.coredomain;

import static com.onetuks.coredomain.CustomFilePathFixture.createCoverImgFilePath;
import static com.onetuks.coredomain.CustomFilePathFixture.createDetailImgFilePaths;
import static com.onetuks.coredomain.CustomFilePathFixture.createPreviewFilePaths;
import static com.onetuks.coredomain.CustomFilePathFixture.createSampleFilePath;
import static com.onetuks.coredomain.util.TestValueProvider.createApprovalInfo;
import static com.onetuks.coredomain.util.TestValueProvider.createBookConceptualInfo;
import static com.onetuks.coredomain.util.TestValueProvider.createBookPhysicalInfo;
import static com.onetuks.coredomain.util.TestValueProvider.createBookPriceInfo;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coreobj.file.UUIDProvider;

public class RegistrationFixture {

  public static Registration create(long registrationId, Author author, boolean isApproved) {
    String uuid = UUIDProvider.provideUUID();
    return new Registration(
        registrationId,
        author,
        createApprovalInfo(isApproved),
        createBookConceptualInfo(),
        createBookPhysicalInfo(),
        createBookPriceInfo(),
        createCoverImgFilePath(uuid),
        createDetailImgFilePaths(uuid),
        createPreviewFilePaths(uuid),
        createSampleFilePath(uuid));
  }
}
