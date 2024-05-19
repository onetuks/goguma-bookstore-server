package com.onetuks.dbstorage.registration.repository;

import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coredomain.registration.repository.RegistrationScmRepository;
import com.onetuks.dbstorage.registration.converter.RegistrationConverter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public Page<Registration> readAll(Pageable pageable) {
    return repository.findAll(pageable).map(converter::toDomain);
  }

  @Override
  public Page<Registration> readAll(long authorId, Pageable pageable) {
    return repository.findByAuthorEntityAuthorId(authorId, pageable).map(converter::toDomain);
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
