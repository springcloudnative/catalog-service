package com.polarbookshop.catalogservice.domain.repository;

import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookRepository4 {

    Iterable<BookEntity> findAll();
    Optional<BookEntity> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    BookEntity save(BookEntity book);
    void saveAll(List<BookEntity> books);
    void deleteByIsbn(String isbn);
    void deleteAll();
}
