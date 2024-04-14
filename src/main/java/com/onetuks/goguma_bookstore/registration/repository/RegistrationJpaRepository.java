package com.onetuks.goguma_bookstore.registration.repository;

import com.onetuks.goguma_bookstore.registration.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationJpaRepository extends JpaRepository<Registration, Long> {}
