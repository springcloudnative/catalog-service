package com.polarbookshop.catalogservice.application.service;

import com.polarbookshop.catalogservice.application.exception.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.application.exception.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import com.polarbookshop.catalogservice.domain.repository.BookRepository;
import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Iterable<BookAggregate> viewBookList() {
        Iterable<BookEntity> bookEntities = bookRepository.findAll();
        Iterator<BookEntity> iterator = bookRepository.findAll().iterator();
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
        Optional<BookEntity> book = bookRepository.findByIsbn(isbn);

        if (book.isPresent()) {
            return book.get().toBookAggregate();
        } else {
            throw new BookNotFoundException(isbn);
        }
    }

    @Override
    public BookAggregate addBookToCatalog(BookAggregate book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BookAlreadyExistsException(book.getIsbn());
        }
        BookEntity bookEntity = new BookEntity();
        BeanUtils.copyProperties(book, bookEntity);

        return bookRepository.save(bookEntity).toBookAggregate();
    }

    @Override
    public void removeBookFromCatalog(String isbn) {
        if (!bookRepository.existsByIsbn(isbn)) {
            throw new BookNotFoundException(isbn);
        }

        bookRepository.deleteByIsbn(isbn);
    }

    @Override
    public BookAggregate editBookDetails(String isbn, BookAggregate book) {
        Optional<BookEntity> existingBook = bookRepository.findByIsbn(isbn);

        if (existingBook.isEmpty()) {
            return addBookToCatalog(book);
        }

        BookEntity bookToUpdate = new BookEntity();
        BeanUtils.copyProperties(book, bookToUpdate);

        return bookRepository.save(bookToUpdate).toBookAggregate();
    }
}
