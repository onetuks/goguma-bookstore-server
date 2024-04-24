package com.onetuks.goguma_bookstore.registration.repository;

import com.onetuks.goguma_bookstore.registration.model.Registration;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationJpaRepository extends JpaRepository<Registration, Long> {

  Page<Registration> findAll(Pageable pageable);

  Page<Registration> findByAuthorAuthorId(Long authorId, Pageable pageable);

  Optional<Registration> findByBookConceptualInfoIsbn(String isbn);
}
