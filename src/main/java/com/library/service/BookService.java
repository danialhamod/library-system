package com.library.service;

import com.library.exception.EntityAlreadyExistsException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Cacheable(value = "books", key = "#id")
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public Book addBook(Book book) {
        // Check for existing ISBN first
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new EntityAlreadyExistsException("book", "ISBN", book.getIsbn());
        }
        return bookRepository.save(book);
    }

    @CacheEvict(value = "books", key = "#p0")
    @Transactional
    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            // Check ISBN uniqueness if changed
            if (!book.getIsbn().equals(bookDetails.getIsbn())) {
                if (bookRepository.existsByIsbn(bookDetails.getIsbn())) {
                    throw new EntityAlreadyExistsException("book", "ISBN", bookDetails.getIsbn());
                }
            }
            // Update fields
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublicationYear(bookDetails.getPublicationYear());
            book.setIsbn(bookDetails.getIsbn());
            return bookRepository.save(book);
        }
        return null;
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}