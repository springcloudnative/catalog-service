package com.polarbookshop.catalogservice.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAggregate {
    private String isbn;
    private String title;
    private String author;
    private Double price;
}
