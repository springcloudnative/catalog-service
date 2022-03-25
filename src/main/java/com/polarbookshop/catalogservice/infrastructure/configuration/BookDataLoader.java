package com.polarbookshop.catalogservice.infrastructure.configuration;

import com.polarbookshop.catalogservice.domain.repository.BooksRepository;
import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;
import lombok.Data;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@Profile("test-data")
public class BookDataLoader {

    private final BooksRepository booksRepository;

    /**
     * The test data generation is triggered when an ApplicationReadyEvent
     * is sent => that is when the application startup phase is completed.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        // delete all existing books, if any, to start from an empty database
        this.booksRepository.deleteAll();

        BookEntity book1 = BookEntity.build("1234567891", "Northern Lights",
                "Lyra Silvertongue", 9.90, "Polarsophia");

        BookEntity book2 = BookEntity.build("1234567892", "Polar Journey",
                "Iorek Polarson", 12.90, "Polarsophia");

        // save multiple objects at once
        booksRepository.saveAll(List.of(book1, book2));
    }
}
