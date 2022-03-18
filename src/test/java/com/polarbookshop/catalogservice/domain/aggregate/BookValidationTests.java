package com.polarbookshop.catalogservice.domain.aggregate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BookValidationTests {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenAllFieldsCorrectThenValidationSucceeds() {
        BookAggregate book = BookAggregate.builder()
                            .isbn("1234567890")
                            .title("Title")
                            .author("Author")
                            .price(9.9)
                            .build();

        Set<ConstraintViolation<BookAggregate>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenIsbnDefinedButIncorrectThenValidationFails() {
        BookAggregate book = BookAggregate.builder()
                .isbn("a234567890")
                .title("Title")
                .author("Author")
                .price(9.9)
                .build();

        Set<ConstraintViolation<BookAggregate>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must follow the standards ISBN-10 or ISBN-13.");
    }
}