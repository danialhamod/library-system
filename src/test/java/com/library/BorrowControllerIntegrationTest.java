package com.library;

import com.library.constants.ErrorCodes;
import com.library.model.Book;
import com.library.model.Patron;
import com.library.repository.BookRepository;
import com.library.repository.BorrowingRecordRepository;
import com.library.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BorrowControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    private Book testBook;
    private Patron testPatron;

    @BeforeEach
    void setUp() {
        borrowingRecordRepository.deleteAll();
        bookRepository.deleteAll();
        patronRepository.deleteAll();
        
        testBook = bookRepository.save(
            new Book("Borrowable Book", "Author", "978-3-16-148777-1", 2020));
        testPatron = patronRepository.save(
            new Patron("Test Patron", "test@example.com", "8888888888"));
    }

    @Test
    @Rollback
    void borrowBook_shouldReturn200() throws Exception {
        mockMvc.perform(post("/api/borrow/" + testBook.getId() + "/patron/" + testPatron.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.book.id").value(testBook.getId()))
                .andExpect(jsonPath("$.data.patron.id").value(testPatron.getId()))
                .andExpect(jsonPath("$.data.returnDate").doesNotExist());
    }

    @Test
    @Rollback
    void returnBook_shouldReturn200() throws Exception {
        // First borrow the book
        mockMvc.perform(post("/api/borrow/" + testBook.getId() + "/patron/" + testPatron.getId()));

        // Then return it
        mockMvc.perform(put("/api/return/" + testBook.getId() + "/patron/" + testPatron.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.returnDate").exists());
    }

    @Test
    void borrowAlreadyBorrowedBook_shouldReturn400() throws Exception {
        // First successful borrow
        mockMvc.perform(post("/api/borrow/" + testBook.getId() + "/patron/" + testPatron.getId()));

        // Attempt to borrow again
        mockMvc.perform(post("/api/borrow/" + testBook.getId() + "/patron/" + testPatron.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book is already borrowed"))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.PROCCESSING_ERROR));
    }

    @Test
    void returnNotBorrowedBook_shouldReturn400() throws Exception {
        mockMvc.perform(put("/api/return/" + testBook.getId() + "/patron/" + testPatron.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("No active borrowing record found"))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.PROCCESSING_ERROR));
    }

    @Test
    void borrowNonExistingBook_shouldReturn404() throws Exception {
        mockMvc.perform(post("/api/borrow/999/patron/" + testPatron.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Book not found"))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.PROCCESSING_ERROR));
    }

    @Test
    void borrowWithNonExistingPatron_shouldReturn404() throws Exception {
        mockMvc.perform(post("/api/borrow/" + testBook.getId() + "/patron/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Patron not found"))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.PROCCESSING_ERROR));
    }
}