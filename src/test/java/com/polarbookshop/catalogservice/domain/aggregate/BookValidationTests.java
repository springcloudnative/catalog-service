package com.polarbookshop.catalogservice.domain.aggregate;

import com.polarbookshop.catalogservice.domain.vo.IsbnCode;
import com.polarbookshop.catalogservice.infrastructure.repository.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BookValidationTests extends BaseTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenAllFieldsCorrectThenValidationSucceeds() {

        assertDoesNotThrow(() -> {
            BookAggregate book = BookAggregate.builder()
                    .isbn(this.isbnCode)
                    .title(this.title)
                    .author("Author")
                    .price(9.9)
                    .build();
        });
    }

    @Test
    public void whenIsbnDefinedButIncorrectThenValidationFails() {

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            BookAggregate book = BookAggregate.builder()
                    .isbn(new IsbnCode("a234567890"))
                    .title(this.title)
                    .author("Author")
                    .price(9.9)
                    .build();
        });

        String expectedMessage = "The ISBN format must follow the standards ISBN-10 or ISBN-13.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}