package com.onetuks.dbstorage.registration.repository;

import com.onetuks.dbstorage.registration.entity.RegistrationEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationJpaRepository extends JpaRepository<RegistrationEntity, Long> {

  Page<RegistrationEntity> findByAuthorEntityAuthorId(Long authorId, Pageable pageable);

  Optional<RegistrationEntity> findByBookConceptualInfoIsbn(String isbn);
}
