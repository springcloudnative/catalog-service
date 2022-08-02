package com.polarbookshop.catalogservice.application.service;

import com.polarbookshop.catalogservice.application.exception.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.application.exception.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;
import com.polarbookshop.catalogservice.infrastructure.repository.BookRepository;
import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@Service
@Primary
public class BookServiceImpl implements BookService {

    private final BookRepository bookJdbcRepository;

    @Override
    public List<BookAggregate> viewBookList() {
        Iterable<BookEntity> bookEntities = bookJdbcRepository.findAll();
        Iterator<BookEntity> iterator = bookJdbcRepository.findAll().iterator();
        Iterable<BookEntity> iterable = () -> iterator;

        List<BookEntity> bookEntitiesList = StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList());

        return bookEntitiesList.stream()
                .map(bookEntity -> bookEntity.toBookAggregate())
                .collect(Collectors.toList());
    }

    @Override
    public BookAggregate viewBookDetails(String isbn) {
        Optional<BookEntity> book = bookJdbcRepository.findByIsbn(isbn);

        if (book.isPresent()) {
            return book.get().toBookAggregate();
        } else {
            throw new BookNotFoundException(isbn);
        }
    }

    @Override
    public BookAggregate addBookToCatalog(BookAggregate book) {
        if (bookJdbcRepository.existsByIsbn(book.getIsbn().getValue())) {
            throw new BookAlreadyExistsException(book.getIsbn().getValue());
        }

        BookEntity bookEntity = BookEntity.build(
                book.getIsbn().getValue(),
                book.getTitle().getValue(),
                book.getAuthor(),
                book.getPrice(),
                book.getPublisher()
        );

//        BeanUtils.copyProperties(book, bookEntity);

        return bookJdbcRepository.save(bookEntity).toBookAggregate();
    }

    @Override
    public void removeBookFromCatalog(String isbn) {
        if (!bookJdbcRepository.existsByIsbn(isbn)) {
            throw new BookNotFoundException(isbn);
        }

        bookJdbcRepository.deleteByIsbn(isbn);
    }

    @Override
    public BookAggregate editBookDetails(String isbn, BookAggregate book) {
        Optional<BookEntity> existingBook = bookJdbcRepository.findByIsbn(isbn);

        if (existingBook.isEmpty()) {
            return addBookToCatalog(book);
        }

        BookEntity bookToUpdate = new BookEntity(
                existingBook.get().getId(),
                existingBook.get().getIsbn(),
                book.getTitle().getValue(),
                book.getAuthor(),
                book.getPrice(),
                book.getPublisher(),
                existingBook.get().getCreatedDate(),
                existingBook.orElseThrow().getLastModifiedDate(),
                existingBook.get().getVersion()
        );

        return bookJdbcRepository.save(bookToUpdate).toBookAggregate();
    }
}
