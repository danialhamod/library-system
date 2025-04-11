package com.library.controller;

import com.library.model.Patron;
import com.library.service.PatronService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {
    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        return ResponseEntity.ok(patronService.getAllPatrons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        Patron patron = patronService.getPatronById(id);
        return patron != null ? ResponseEntity.ok(patron) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Patron> addPatron(@RequestBody Patron patron) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patronService.addPatron(patron));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @RequestBody Patron patron) {
        Patron updatedPatron = patronService.updatePatron(id, patron);
        return updatedPatron != null ? ResponseEntity.ok(updatedPatron) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.ok(null);
    }
}