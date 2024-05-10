package com.onetuks.modulepersistence.registration.repository;

import com.onetuks.modulepersistence.registration.entity.RegistrationEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationJpaRepository extends JpaRepository<RegistrationEntity, Long> {

  Page<RegistrationEntity> findByAuthorEntityAuthorId(Long authorId, Pageable pageable);

  Optional<RegistrationEntity> findByBookConceptualInfoIsbn(String isbn);
}
