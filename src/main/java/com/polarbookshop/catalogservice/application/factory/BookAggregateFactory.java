package com.polarbookshop.catalogservice.application.factory;

import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import com.polarbookshop.catalogservice.domain.vo.IsbnCode;
import com.polarbookshop.catalogservice.domain.vo.Title;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class BookAggregateFactory implements FactoryBean<BookAggregate> {

    private Long id;
    private IsbnCode isbn;
    private Title title;
    private String author;
    private Double price;
    private String publisher;
    private Instant createdDate;
    private Instant lastModifiedDate;
    private int version;

    @Override
    public BookAggregate getObject() throws Exception {
        return BookAggregate.builder()
                .id(id)
                .isbn(isbn)
                .title(title)
                .author(author)
                .price(price)
                .publisher(publisher)
                .createdDate(createdDate)
                .lastModifiedDate(lastModifiedDate)
                .version(version)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
