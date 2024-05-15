package com.onetuks.readerdomain.author.service;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.repository.AuthorRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

  private final AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @Transactional(readOnly = true)
  public Author readAuthorDetails(long authorId) {
    return authorRepository.read(authorId);
  }

  @Transactional(readOnly = true)
  public List<Author> readAllAuthorDetails() {
    return authorRepository.readAllEnrollmentPassed();
  }
}
