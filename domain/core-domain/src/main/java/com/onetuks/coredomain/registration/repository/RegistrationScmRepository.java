package com.onetuks.coredomain.registration.repository;

import com.onetuks.coredomain.registration.model.Registration;
import java.util.List;

public interface RegistrationScmRepository {

  Registration create(Registration registration);

  Registration read(long registrationId);

  Registration readByIsbn(String isbn);

  List<Registration> readAll();

  List<Registration> readAll(long authorId);

  Registration update(Registration registration);

  void delete(long registrationId);
}
