package com.library.service;

import com.library.dto.PaginatedResponse;
import com.library.exception.EntityAlreadyExistsException;
import com.library.model.Patron;
import com.library.repository.PatronRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatronService {
    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public PaginatedResponse<Patron> getAllPatrons(Pageable pageable) {
        return new PaginatedResponse<Patron>(patronRepository.findAll(pageable));
    }

    @Cacheable(value = "patrons", key = "#id")
    public Patron getPatronById(Long id) {
        return patronRepository.findById(id).orElse(null);
    }

    public Patron addPatron(Patron patron) {
        // Check for existing email first
        if (patronRepository.existsByEmail(patron.getEmail())) {
            throw new EntityAlreadyExistsException("patron", "email", patron.getEmail());
        }
        return patronRepository.save(patron);
    }

    @CacheEvict(value = "patrons", key = "#p0")
    @Transactional
    public Patron updatePatron(Long id, Patron patronDetails) {
        Patron patron = patronRepository.findById(id).orElse(null);
        if (patron != null) {
            // Check email uniqueness if changed
            if (!patron.getEmail().equals(patronDetails.getEmail())) {
                if (patronRepository.existsByEmail(patronDetails.getEmail())) {
                    throw new EntityAlreadyExistsException("patron", "email", patron.getEmail());
                }
            }
            // Update fields
            patron.setName(patronDetails.getName());
            patron.setContactInfo(patronDetails.getContactInfo());
            patron.setEmail(patronDetails.getEmail());
            return patronRepository.save(patron);
        }
        return null;
    }

    public void deletePatron(Long id) {
        patronRepository.deleteById(id);
    }
}