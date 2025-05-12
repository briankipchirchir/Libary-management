package com.example.libbby.service;

import com.example.libbby.dto.BookDTO;
import java.util.List;

public interface BookService {
    BookDTO createBook(BookDTO bookDTO);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    BookDTO getBookById(Long id);
    List<BookDTO> getAllBooks();
    void deleteBook(Long id);
}
