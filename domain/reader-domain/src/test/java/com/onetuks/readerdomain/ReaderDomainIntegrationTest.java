package com.onetuks.readerdomain;

import com.onetuks.coredomain.author.repository.AuthorRepository;
import com.onetuks.coredomain.book.repository.BookRepository;
import com.onetuks.coredomain.comment.repository.CommentRepository;
import com.onetuks.coredomain.favorite.repository.FavoriteRepository;
import com.onetuks.coredomain.global.file.repository.FileRepository;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coredomain.restock.repository.RestockRepository;
import com.onetuks.coredomain.subscribe.repository.SubscribeRepository;
import com.onetuks.readerdomain.ReaderDomainIntegrationTest.ReaderDomainConfig;
import com.onetuks.readerdomain.author.service.AuthorService;
import com.onetuks.readerdomain.book.service.BookService;
import com.onetuks.readerdomain.comment.service.CommentService;
import com.onetuks.readerdomain.favorite.service.FavoriteService;
import com.onetuks.readerdomain.member.service.MemberService;
import com.onetuks.readerdomain.restock.service.RestockService;
import com.onetuks.readerdomain.subscribe.service.SubscribeService;
import com.onetuks.readerdomain.util.TestFileCleaner;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = ReaderDomainConfig.class)
public class ReaderDomainIntegrationTest {

  @Configuration
  @ComponentScan(basePackages = {"com.onetuks.readerdomain", "com.onetuks.coredomain"})
  public static class ReaderDomainConfig {}

  @Autowired private TestFileCleaner testFileCleaner;

  @AfterEach
  void tearDown() {
    testFileCleaner.deleteAllTestStatic();
  }

  @Autowired public MemberService memberService;
  @Autowired public AuthorService authorService;
  @Autowired public BookService bookService;
  @Autowired public SubscribeService subscribeService;
  @Autowired public FavoriteService favoriteService;
  @Autowired public RestockService restockService;
  @Autowired public CommentService commentService;

  @MockBean public MemberRepository memberRepository;
  @MockBean public AuthorRepository authorRepository;
  @MockBean public BookRepository bookRepository;
  @MockBean public SubscribeRepository subscribeRepository;
  @MockBean public FavoriteRepository favoriteRepository;
  @MockBean public RestockRepository restockRepository;
  @MockBean public CommentRepository commentRepository;

  @MockBean public FileRepository fileRepository;
}
