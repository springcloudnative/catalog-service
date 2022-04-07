package com.polarbookshop.catalogservice.infrastructure.configuration;

import com.polarbookshop.catalogservice.application.service.BookService;
import com.polarbookshop.catalogservice.application.service.BookServiceImpl;
import com.polarbookshop.catalogservice.infrastructure.repository.BookRepository;
import com.polarbookshop.catalogservice.infrastructure.repository.MySqlDbCatalogRepository;
import com.polarbookshop.catalogservice.infrastructure.repository.SpringDataPostgresCatalogRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Bean(value = "bookService")
    BookService bookService(BookRepository bookRepository) {
        return new BookServiceImpl(bookRepository);
    }

    @Bean
    MySqlDbCatalogRepository bookJdbcRepository(SpringDataPostgresCatalogRepository bookRepository) {
        return new MySqlDbCatalogRepository(bookRepository);
    }
}
