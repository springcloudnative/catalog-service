package com.polarbookshop.catalogservice.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.polarbookshop.catalogservice.domain.dto.BookDTO;
import com.polarbookshop.catalogservice.domain.vo.IsbnCode;
import com.polarbookshop.catalogservice.domain.vo.Title;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAggregate {
    private Long id;

    @JsonProperty("isbn")
    private IsbnCode isbn;

    private Title title;

    private String author;

    private Double price;

    private String publisher;

    private Instant createdDate;

    private Instant lastModifiedDate;

    private int version;

    public static BookAggregate createFrom(BookDTO bookDTO) {
        return BookAggregate.builder()
                .id(bookDTO.getId())
                .isbn(new IsbnCode(bookDTO.getIsbn()))
                .title(new Title(bookDTO.getTitle()))
                .author(bookDTO.getAuthor())
                .price(bookDTO.getPrice())
                .publisher(bookDTO.getPublisher())
                .createdDate(bookDTO.getCreatedDate())
                .lastModifiedDate(bookDTO.getLastModifiedDate())
                .version(bookDTO.getVersion())
                .build();
    }
}
