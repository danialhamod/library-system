package com.library.controller;

import com.library.model.BorrowingRecord;
import com.library.service.BorrowingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BorrowingController {
    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> borrowBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId) {
        try {
            return ResponseEntity.ok(borrowingService.borrowBook(bookId, patronId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> returnBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId) {
        try {
            return ResponseEntity.ok(borrowingService.returnBook(bookId, patronId));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(null);
        }
    }
}