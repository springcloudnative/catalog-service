package com.polarbookshop.catalogservice.application.api.rest;

import com.polarbookshop.catalogservice.application.service.BookService;
import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Iterable<BookAggregate> get() {
        return bookService.viewBookList();
    }

    @GetMapping("{isbn}")
    public BookAggregate getByIsbn(@PathVariable String isbn) {
        return bookService.viewBookDetails(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookAggregate post(@Valid @RequestBody BookAggregate book) {
        return bookService.addBookToCatalog(book);
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }

    @PutMapping("{isbn}")
    public BookAggregate put(@PathVariable String isbn, @Valid @RequestBody BookAggregate book) {
        return bookService.editBookDetails(isbn, book);
    }
}
