package com.hmt.Healix.Repository;

import com.hmt.Healix.Entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Optional<Patient> findByUserId(long userId);
}
