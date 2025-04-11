package com.library.service;

import com.library.model.Patron;
import com.library.repository.PatronRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PatronService {
    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public Patron getPatronById(Long id) {
        return patronRepository.findById(id).orElse(null);
    }

    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Transactional
    public Patron updatePatron(Long id, Patron patronDetails) {
        Patron patron = patronRepository.findById(id).orElse(null);
        if (patron != null) {
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