package com.polarbookshop.catalogservice.application.service;

import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;

import java.util.List;

public interface BookService {

    List<BookAggregate> viewBookList();
    BookAggregate viewBookDetails(String isbn);
    BookAggregate addBookToCatalog(BookAggregate book);
    void removeBookFromCatalog(String isbn);
    BookAggregate editBookDetails(String isbn, BookAggregate book);
}
