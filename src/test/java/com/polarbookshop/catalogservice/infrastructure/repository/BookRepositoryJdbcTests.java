package com.polarbookshop.catalogservice.infrastructure.repository;

import com.polarbookshop.catalogservice.infrastructure.configuration.DataConfig;
import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@DataJdbcTest()
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests extends BaseTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    public void findBookByIsbnWhenExisting() {
        String bookIsbn = "1234567890";
        BookEntity book = new BookEntity(null, bookIsbn, "Title", "Author",
                12.9, "Polarsophia", null, null, 0);

        BookEntity expectedBook = jdbcAggregateTemplate.insert(book);

        Optional<BookEntity> actualBook = bookRepository.findByIsbn(bookIsbn);

        Assertions.assertThat(actualBook).isPresent();
        Assertions.assertThat(actualBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void findBookByIsbnWhenNotExisting() {
        Optional<BookEntity> actualBook = bookRepository.findByIsbn("1234561238");
        Assertions.assertThat(actualBook).isEmpty();
    }

    @Test
    void findAllBooks() {
        BookEntity book1 = BookEntity.build("1234561235", "Title", "Author",
                12.90, "Polarsophia");
        BookEntity book2 = BookEntity.build("1234561236", "Another Title", "Author",
                12.90, "Polarsophia");
        jdbcAggregateTemplate.insert(book1);
        jdbcAggregateTemplate.insert(book2);

        Iterable<BookEntity> actualBooks = bookRepository.findAll();

        Assertions.assertThat(StreamSupport.stream(actualBooks.spliterator(), true)
                .filter(book -> book.getIsbn().equals(book1.getIsbn()) ||
                        book.getIsbn().equals(book2.getIsbn()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void existsByIsbnWhenExisting() {
        var bookIsbn = "1234561239";
        var bookToCreate = BookEntity.build(bookIsbn, "Title", "Author",
                12.90, "Polarsophia");
        jdbcAggregateTemplate.insert(bookToCreate);

        boolean existing = bookRepository.existsByIsbn(bookIsbn);

        Assertions.assertThat(existing).isTrue();
    }

    @Test
    void existsByIsbnWhenNotExisting() {
        boolean existing = bookRepository.existsByIsbn("1234561240");
        Assertions.assertThat(existing).isFalse();
    }

    @Test
    void deleteByIsbn() {
        var bookIsbn = "1234561241";
        var bookToCreate = BookEntity.build(bookIsbn, "Title", "Author",
                12.90, "Polarsophia");
        var persistedBook = jdbcAggregateTemplate.insert(bookToCreate);

        bookRepository.deleteByIsbn(bookIsbn);

        Assertions.assertThat(jdbcAggregateTemplate.findById(persistedBook.getId(), BookEntity.class)).isNull();
    }
}
