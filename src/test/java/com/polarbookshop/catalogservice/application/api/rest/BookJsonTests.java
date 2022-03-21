package com.polarbookshop.catalogservice.application.api.rest;

import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is used to test the JSON deserialization
 * in the web layer.
 * Using the @JsonTest annotation, we can test JSON serialization
 * and deserialization against an application context, including only
 * configuration and beans needed for the purpose. The JacksonTester utility class
 * lets us perform parsing operations using the Jackson library.
 */
@JsonTest // Identifies a test class that focuses on JSON serialization.
public class BookJsonTests {

    // Utility class to assert JSON serialization and deserialization.
    @Autowired
    private JacksonTester<BookAggregate> json;

    @Test
    public void testSerialize() throws Exception {
        BookAggregate book = BookAggregate.builder()
                .isbn("1234567890")
                .title("Title")
                .author("Author")
                .price(9.90)
                .build();

        JsonContent<BookAggregate> jsonContent = json.write(book);

        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.getIsbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.getTitle());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.getAuthor());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.getPrice());
    }

    @Test
    public void testDeserialize() throws Exception {

        JSONObject jsonObject = new JSONObject();
        String content = jsonObject
                .put("isbn", "1234567890")
                .put("title", "Title")
                .put("author", "Author")
                .put("price", 9.90).toString();

        // Verifying the parsing from JSON to Java
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new BookAggregate("1234567890", "Title", "Author", 9.90));
    }
}
