package com.polarbookshop.catalogservice.infrastructure.entity;

import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {

    private String isbn;
    private String title;
    private String author;
    private Double price;

    public BookAggregate toBookAggregate() {
        return BookAggregate.builder()
                .isbn(this.isbn)
                .title(this.title)
                .author(this.author)
                .price(this.price)
                .build();
    }
}
