package com.library.repository;

import com.library.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {
    boolean existsByEmail(String email);
}