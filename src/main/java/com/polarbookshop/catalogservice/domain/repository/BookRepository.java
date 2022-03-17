package com.polarbookshop.catalogservice.domain.repository;

import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;

import java.util.Optional;

public interface BookRepository {

    Iterable<BookEntity> findAll();
    Optional<BookEntity> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    BookEntity save(BookEntity book);
    void deleteByIsbn(String isbn);
}
