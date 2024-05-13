package com.onetuks.dbstorage.author.repository;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.repository.AuthorRepository;
import com.onetuks.coredomain.author.repository.AuthorScmRepository;
import com.onetuks.dbstorage.author.converter.AuthorConverter;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorEntityRepository implements AuthorRepository, AuthorScmRepository {

  private final AuthorJpaRepository authorJpaRepository;
  private final AuthorConverter converter;

  public AuthorEntityRepository(
      AuthorJpaRepository authorJpaRepository,
      AuthorConverter converter) {
    this.authorJpaRepository = authorJpaRepository;
    this.converter = converter;
  }

  @Override
  public Author create(Author author) {
    return converter.toDomain(
        authorJpaRepository.save(
            converter.toEntity(author)));
  }

  @Override
  public Author read(long authorId) {
    return converter.toDomain(
        authorJpaRepository.findById(authorId)
            .orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public List<Author> readAll() {
    return authorJpaRepository.findAll()
        .stream()
        .map(converter::toDomain)
        .toList();
  }

  @Override
  public List<Author> readAllEnrollmentPassed() {
    return authorJpaRepository.findByIsEnrollmentPassedTrue()
        .stream()
        .map(converter::toDomain)
        .toList();
  }

  @Override
  public Author update(Author author) {
    return converter.toDomain(
        authorJpaRepository.save(
            converter.toEntity(author)));
  }

  @Override
  public void delete(long authorId) {
    authorJpaRepository.deleteById(authorId);
  }
}
