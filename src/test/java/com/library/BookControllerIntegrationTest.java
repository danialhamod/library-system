package com.library;

import com.library.constants.ErrorCodes;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookControllerIntegrationTest extends BaseTest {
    @Autowired
    private BookRepository bookRepository;
    
    @Test
    @Rollback
    void createBook_shouldReturn201() throws Exception {
        Book newBook = new Book("Clean Code", "Robert Martin", "978-3-16-148410-1", 2008);

        mockMvc.perform(post("/api/books").with(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Clean Code"))
                .andExpect(jsonPath("$.data.author").value("Robert Martin"));
    }

    @Test
    @Rollback
    void getAllBooks_shouldReturnList() throws Exception {
        bookRepository.save(new Book("Book 1", "Author 1", "978-3-16-148111-1", 2000));
        bookRepository.save(new Book("Book 2", "Author 2", "978-3-16-148222-1", 2010));

        mockMvc.perform(get("/api/books").with(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].title").value("Book 1"))
                .andExpect(jsonPath("$.data.content[1].title").value("Book 2"));
    }

    @Test
    @Rollback
    void getBookById_shouldReturnBook() throws Exception {
        Book savedBook = bookRepository.save(
            new Book("Specific Book", "Author", "978-3-16-148333-1", 2020));

        mockMvc.perform(get("/api/books/" + savedBook.getId()).with(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Specific Book"));
    }

    @Test
    @Rollback
    void updateBook_shouldUpdateSuccessfully() throws Exception {
        Book savedBook = bookRepository.save(
            new Book("Original", "Author", "978-3-16-148444-1", 2000));

        Book updateData = new Book("Updated", "Author", "978-3-16-148444-1", 2023);

        mockMvc.perform(put("/api/books/" + savedBook.getId()).with(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Updated"))
                .andExpect(jsonPath("$.data.publicationYear").value(2023));
    }

    @Test
    @Rollback
    void deleteBook_shouldReturn200() throws Exception {
        Book savedBook = bookRepository.save(
            new Book("To Delete", "Author", "978-3-16-148555-1", 2010));

        mockMvc.perform(delete("/api/books/" + savedBook.getId()).with(auth))
                .andExpect(status().isOk());
    }

    @Test
    void createBook_duplicateIsbn_shouldReturn409() throws Exception {
        Book existingBook = new Book("Existing", "Author", "978-3-16-148666-1", 2020);
        bookRepository.save(existingBook);

        Book duplicateBook = new Book("Duplicate", "Author", "978-3-16-148666-1", 2021);

        mockMvc.perform(post("/api/books").with(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateBook)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(ErrorCodes.CONFLICT_ERROR));
    }

    @Test
    void getBook_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/books/999").with(auth))
                .andExpect(status().isNotFound());
    }
}