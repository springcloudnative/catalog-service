package com.polarbookshop.catalogservice.infrastructure.repository;

import org.springframework.stereotype.Component;

@Component("mySqlDbCatalogRepository")
public class MySqlDbCatalogRepository {
 /*       implements BookRepository {

    private final SpringDataPostgresCatalogRepository bookRepository;

    @Autowired
    public MySqlDbCatalogRepository4(SpringDataPostgresCatalogRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Iterable<BookEntity> findAll() {
        return this.bookRepository.findAll();
    }

    @Override
    public Optional<BookEntity> findByIsbn(String isbn) {
        return this.bookRepository.findByIsbn(isbn);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return this.bookRepository.existsByIsbn(isbn);
    }

    @Override
    public BookEntity save(BookEntity book) {
        return this.bookRepository.save(book);
    }

    @Override
    public void saveAll(List<BookEntity> books) {
        this.bookRepository.saveAll(books);
    }

    @Override
    public void deleteByIsbn(String isbn) {
        this.bookRepository.deleteByIsbn(isbn);
    }

    @Override
    public void deleteAll() {
        this.bookRepository.deleteAll();
    }*/
}
