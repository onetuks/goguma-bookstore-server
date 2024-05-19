package com.onetuks.dbstorage.author.repository;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.repository.AuthorRepository;
import com.onetuks.coredomain.author.repository.AuthorScmRepository;
import com.onetuks.dbstorage.author.converter.AuthorConverter;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorEntityRepository implements AuthorRepository, AuthorScmRepository {

  private final AuthorJpaRepository repository;
  private final AuthorConverter converter;

  public AuthorEntityRepository(AuthorJpaRepository repository, AuthorConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public Author create(Author author) {
    return converter.toDomain(repository.save(converter.toEntity(author)));
  }

  @Override
  public Author read(long authorId) {
    return converter.toDomain(
        repository.findById(authorId).orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public Author readByMember(long memberId) {
    return repository
        .findByMemberEntityMemberId(memberId)
        .map(converter::toDomain)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public List<Author> readAll() {
    return repository.findAll().stream().map(converter::toDomain).toList();
  }

  @Override
  public Page<Author> readAll(Pageable pageable) {
    return repository.findAll(pageable).map(converter::toDomain);
  }

  @Override
  public Page<Author> readAllEnrollmentPassed(Pageable pageable) {
    return repository.findByIsEnrollmentPassedTrue(pageable).map(converter::toDomain);
  }

  @Override
  public Author update(Author author) {
    return converter.toDomain(repository.save(converter.toEntity(author)));
  }

  @Override
  public void delete(long authorId) {
    repository.deleteById(authorId);
  }
}
