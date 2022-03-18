package com.polarbookshop.catalogservice.application.api.rest;

import com.polarbookshop.catalogservice.application.service.BookService;
import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

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
    public BookAggregate post(@RequestBody BookAggregate book) {
        return bookService.addBookToCatalog(book);
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }

    @PutMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BookAggregate put(@PathVariable String isbn, @RequestBody BookAggregate book) {
        return bookService.editBookDetails(isbn, book);
    }
}
