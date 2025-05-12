package com.example.libbby.repository;

import com.example.libbby.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title);
    // No need to add anything unless you want custom queries
}
