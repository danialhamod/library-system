package com.library.repository;

import com.library.model.Book;
import com.library.model.BorrowingRecord;
import com.library.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    Optional<BorrowingRecord> findByBookAndPatronAndReturnDateIsNull(Book book, Patron patron);
    boolean existsByBookAndReturnDateIsNull(Book book);
}