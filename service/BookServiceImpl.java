package com.example.libbby.service;

import com.example.libbby.dto.BookDTO;
import com.example.libbby.model.Book;
import com.example.libbby.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        // Check if a book with the same title already exists in the database
        Book existingBook = bookRepository.findByTitle(bookDTO.getTitle());


        if (existingBook != null) {
            // If the book already exists, throw an exception or return a specific response
            throw new BookAlreadyExistsException("A book with the title '" + bookDTO.getTitle() + "' already exists.");
        }

        // Convert DTO to Entity
        Book book = new Book(bookDTO.getId(), bookDTO.getTitle(), bookDTO.getAuthor(),
                bookDTO.getIsbn(), bookDTO.getPublisher());

        // Save book to DB

        Book savedBook = bookRepository.save(book);

        // Convert back to DTO and return
        return new BookDTO(savedBook.getId(), savedBook.getTitle(), savedBook.getAuthor(),
                savedBook.getIsbn(), savedBook.getPublisher());
    }


    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        // Check if book exists
        Optional<Book> existingBookOpt = bookRepository.findById(id);
        if (existingBookOpt.isEmpty()) {
            throw new RuntimeException("Book not found with id: " + id); // You could throw a custom exception here
        }

        Book existingBook = existingBookOpt.get();
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setPublisher(bookDTO.getPublisher());

        // Save updated book
        Book updatedBook = bookRepository.save(existingBook);

        // Convert back to DTO and return
        return new BookDTO(updatedBook.getId(), updatedBook.getTitle(), updatedBook.getAuthor(),
                updatedBook.getIsbn(), updatedBook.getPublisher());
    }

    @Override
    public BookDTO getBookById(Long id) {
        // Find book by ID
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id)); // You could throw a custom exception here
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(),
                book.getIsbn(), book.getPublisher());
    }

    @Override
    public List<BookDTO> getAllBooks() {
        // Get all books
        List<Book> books = bookRepository.findAll();

        // Convert List<Book> to List<BookDTO>
        return books.stream().map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor(),
                book.getIsbn(), book.getPublisher())).collect(Collectors.toList());
    }

    @Override
    public void deleteBook(Long id) {
        // Check if book exists
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id); // You could throw a custom exception here
        }

        // Delete book from DB
        bookRepository.deleteById(id);
    }


    // Inner class for BookAlreadyExistsException
    public static class BookAlreadyExistsException extends RuntimeException {
        public BookAlreadyExistsException(String message) {
            super(message);
        }
    }
}
