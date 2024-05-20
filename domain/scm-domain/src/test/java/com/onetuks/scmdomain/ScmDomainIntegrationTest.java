package com.onetuks.scmdomain;

import com.onetuks.coredomain.author.repository.AuthorScmRepository;
import com.onetuks.coredomain.book.repository.BookScmRepository;
import com.onetuks.coredomain.global.file.repository.FileRepository;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coredomain.registration.repository.RegistrationScmRepository;
import com.onetuks.scmdomain.ScmDomainIntegrationTest.ScmDomainConfig;
import com.onetuks.scmdomain.author.service.AuthorScmService;
import com.onetuks.scmdomain.book.service.BookScmService;
import com.onetuks.scmdomain.registration.service.RegistrationScmService;
import com.onetuks.scmdomain.restock.service.RestockScmService;
import com.onetuks.scmdomain.util.TestFileCleaner;
import com.onetuks.scmdomain.verification.EnrollmentInfoVerifier;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = ScmDomainConfig.class)
public class ScmDomainIntegrationTest {

  @Configuration
  @ComponentScan(basePackages = {"com.onetuks.coredomain", "com.onetuks.scmdomain"})
  public static class ScmDomainConfig {}

  @Autowired private TestFileCleaner testFileCleaner;

  @AfterEach
  void tearDown() {
    testFileCleaner.deleteAllTestStatic();
  }

  @Autowired public AuthorScmService authorScmService;
  @Autowired public BookScmService bookScmService;
  @Autowired public RegistrationScmService registrationScmService;
  @Autowired public EnrollmentInfoVerifier enrollmentInfoVerifier;
  @Autowired public RestockScmService restockScmService;

  @MockBean public MemberRepository memberRepository;
  @MockBean public AuthorScmRepository authorScmRepository;
  @MockBean public BookScmRepository bookScmRepository;
  @MockBean public RegistrationScmRepository registrationScmRepository;
  @MockBean public FileRepository fileRepository;
}
