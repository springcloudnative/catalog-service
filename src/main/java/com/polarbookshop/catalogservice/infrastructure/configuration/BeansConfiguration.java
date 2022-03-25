package com.polarbookshop.catalogservice.infrastructure.configuration;

import com.polarbookshop.catalogservice.application.service.BookService;
import com.polarbookshop.catalogservice.application.service.BookServiceImpl;
import com.polarbookshop.catalogservice.infrastructure.repository.BookRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Bean
    BookService bookService(BookRepository bookRepository) {
        return new BookServiceImpl(bookRepository);
    }
}
