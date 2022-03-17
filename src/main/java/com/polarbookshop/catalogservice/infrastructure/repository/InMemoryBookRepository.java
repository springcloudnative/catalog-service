package com.polarbookshop.catalogservice.infrastructure.repository;

import com.polarbookshop.catalogservice.domain.repository.BookRepository;
import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private static final Map<String, BookEntity> books = new ConcurrentHashMap<>();

    @Override
    public Iterable<BookEntity> findAll() {
        return books.values();
    }

    @Override
    public Optional<BookEntity> findByIsbn(String isbn) {
        return existsByIsbn(isbn) ? Optional.of(books.get(isbn)) : Optional.empty();
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return books.get(isbn) != null;
    }

    @Override
    public BookEntity save(BookEntity book) {
        books.put(book.getIsbn(), book);
        return book;
    }

    @Override
    public void deleteByIsbn(String isbn) {
        books.remove(isbn);
    }
}
