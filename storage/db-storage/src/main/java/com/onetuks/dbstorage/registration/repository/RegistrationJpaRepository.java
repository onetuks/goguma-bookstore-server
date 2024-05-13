package com.onetuks.dbstorage.registration.repository;

import com.onetuks.dbstorage.registration.entity.RegistrationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationJpaRepository extends JpaRepository<RegistrationEntity, Long> {

  List<RegistrationEntity> findByAuthorEntityAuthorId(Long authorId);

  Optional<RegistrationEntity> findByIsbn(String isbn);
}
