package com.polarbookshop.catalogservice.infrastructure.repository;

import com.polarbookshop.catalogservice.domain.dto.BookDTO;
import com.polarbookshop.catalogservice.domain.vo.IsbnCode;
import com.polarbookshop.catalogservice.domain.vo.Title;

public class BaseTest {

    protected IsbnCode isbnCode = new IsbnCode("1234567890");
    protected IsbnCode isbnCode1 = new IsbnCode("1234561236");
    protected Title title = new Title("Title");
    protected Title title1 = new Title("Title1");

    protected BookDTO getDTOForBookCreation() {
        return BookDTO.builder()
                .isbn(this.isbnCode.getValue())
                .title(this.title.getValue())
                .author("Author")
                .price(9.90)
                .publisher("Polarsophia")
                .build();
    }
}
