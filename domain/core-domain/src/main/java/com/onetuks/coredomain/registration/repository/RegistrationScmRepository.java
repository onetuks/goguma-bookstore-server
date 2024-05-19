package com.onetuks.coredomain.registration.repository;

import com.onetuks.coredomain.registration.model.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationScmRepository {

  Registration create(Registration registration);

  Registration read(long registrationId);

  Registration readByIsbn(String isbn);

  Page<Registration> readAll(Pageable pageable);

  Page<Registration> readAll(long authorId, Pageable pageable);

  Registration update(Registration registration);

  void delete(long registrationId);
}
