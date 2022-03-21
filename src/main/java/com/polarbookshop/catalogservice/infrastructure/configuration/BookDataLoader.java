package com.polarbookshop.catalogservice.infrastructure.configuration;

import com.polarbookshop.catalogservice.domain.repository.BookRepository;
import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;
import lombok.Data;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Data
@Component
@Profile("test-data")
public class BookDataLoader {

    private final BookRepository bookRepository;

    /**
     * The test data generation is triggered when an ApplicationReadyEvent
     * is sent => that is when the application startup phase is completed.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        BookEntity book1 = new BookEntity("1234567891", "Northern Lights",
                "Lyra Silvertongue", 9.90);

        BookEntity book2 = new BookEntity("1234567892", "Polar Journey",
                "Iorek Polarson", 12.90);

        bookRepository.save(book1);
        bookRepository.save(book2);
    }
}
