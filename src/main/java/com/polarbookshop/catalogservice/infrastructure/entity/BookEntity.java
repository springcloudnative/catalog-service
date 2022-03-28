package com.polarbookshop.catalogservice.infrastructure.entity;

import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("books")
public class BookEntity {

    @Id
    private Long id;

    private String isbn;
    private String title;
    private String author;
    private Double price;

    String publisher;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private int version;

    public BookAggregate toBookAggregate() {
        return BookAggregate.builder()
                .isbn(this.isbn)
                .title(this.title)
                .author(this.author)
                .price(this.price)
                .publisher(this.publisher)
                .createdDate(this.createdDate)
                .lastModifiedDate(this.lastModifiedDate)
                .build();
    }

    public static BookEntity build(String isbn, String title,
                                   String author, Double price,
                                   String publisher) {

        return BookEntity.builder()
                .id(null)
                .isbn(isbn)
                .title(title)
                .author(author)
                .price(price)
                .publisher(publisher)
                .createdDate(null)
                .lastModifiedDate(null)
                .version(0)
                .build();
    }
}
