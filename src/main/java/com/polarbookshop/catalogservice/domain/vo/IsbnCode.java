package com.polarbookshop.catalogservice.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@EqualsAndHashCode
@ToString
public final class IsbnCode {

    private String value;

    protected IsbnCode() {}

    public IsbnCode(String isbn) {
        this.value = this.getValidIsbn(isbn);
    }

    private String getValidIsbn(String isbn) {
        if (!isbn.matches("^([0-9]{10}|[0-9]{13})$")) {
            throw new IllegalArgumentException("The ISBN format must follow the standards ISBN-10 or ISBN-13.");
        }

        if (StringUtils.isEmpty(isbn)) {
            throw new IllegalArgumentException("The book ISBN must be defined.");
        }

        return isbn;
    }

    public String getValue() {
        return value;
    }
}
