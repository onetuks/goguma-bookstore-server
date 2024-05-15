package com.onetuks.dbstorage.registration.repository;

import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coredomain.registration.repository.RegistrationScmRepository;
import com.onetuks.dbstorage.registration.converter.RegistrationConverter;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RegistrationEntityRepository implements RegistrationScmRepository {

  private final RegistrationJpaRepository repository;
  private final RegistrationConverter converter;

  public RegistrationEntityRepository(
      RegistrationJpaRepository repository, RegistrationConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public Registration create(Registration registration) {
    return converter.toDomain(repository.save(converter.toEntity(registration)));
  }

  @Override
  public Registration read(long registrationId) {
    return converter.toDomain(
        repository.findById(registrationId).orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public Registration readByIsbn(String isbn) {
    return converter.toDomain(
        repository.findByIsbn(isbn).orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public List<Registration> readAll() {
    return repository.findAll().stream().map(converter::toDomain).toList();
  }

  @Override
  public List<Registration> readAll(long authorId) {
    return repository.findByAuthorEntityAuthorId(authorId).stream()
        .map(converter::toDomain)
        .toList();
  }

  @Override
  public Registration update(Registration registration) {
    return converter.toDomain(repository.save(converter.toEntity(registration)));
  }

  @Override
  public void delete(long registrationId) {
    repository.deleteById(registrationId);
  }
}
