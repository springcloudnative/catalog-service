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

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    public void findBookByIsbnWhenExisting() {
        String bookIsbn = "1234561235";
        BookEntity book = new BookEntity(null, bookIsbn, "Title", "Author",
                12.9, null, null, 0);

        BookEntity expectedBook = jdbcAggregateTemplate.insert(book);

        Optional<BookEntity> actualBook = bookRepository.findByIsbn(bookIsbn);

        Assertions.assertThat(actualBook).isPresent();
        Assertions.assertThat(actualBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }
}
