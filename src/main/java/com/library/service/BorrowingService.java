package com.library.service;

import com.library.model.Book;
import com.library.model.BorrowingRecord;
import com.library.model.Patron;
import com.library.repository.BookRepository;
import com.library.repository.BorrowingRecordRepository;
import com.library.repository.PatronRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class BorrowingService {
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    public BorrowingService(BorrowingRecordRepository borrowingRecordRepository, 
                          BookRepository bookRepository, 
                          PatronRepository patronRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        Patron patron = patronRepository.findById(patronId)
            .orElseThrow(() -> new RuntimeException("Patron not found"));

        if (borrowingRecordRepository.existsByBookAndReturnDateIsNull(book)) {
            throw new RuntimeException("Book is already borrowed");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowingDate(LocalDate.now());
        
        return borrowingRecordRepository.save(record);
    }

    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        Patron patron = patronRepository.findById(patronId)
            .orElseThrow(() -> new RuntimeException("Patron not found"));

        BorrowingRecord record = borrowingRecordRepository
            .findByBookAndPatronAndReturnDateIsNull(book, patron)
            .orElseThrow(() -> new RuntimeException("No active borrowing record found"));

        book.setAvailable(true);
        bookRepository.save(book);

        record.setReturnDate(LocalDate.now());
        return borrowingRecordRepository.save(record);
    }
}