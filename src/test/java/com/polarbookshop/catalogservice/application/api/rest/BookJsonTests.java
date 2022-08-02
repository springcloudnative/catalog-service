package com.polarbookshop.catalogservice.application.api.rest;

import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import com.polarbookshop.catalogservice.infrastructure.repository.BaseTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

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
@ActiveProfiles("integration")
public class BookJsonTests extends BaseTest {

    // Utility class to assert JSON serialization and deserialization.
    @Autowired
    private JacksonTester<BookAggregate> json;

    @Test
    public void testSerialize() throws Exception {

        Instant now = Instant.now();
        BookAggregate book = BookAggregate.builder()
                .id(394L)
                .isbn(this.isbnCode)
                .title(this.title)
                .author("Author")
                .price(9.90)
                .publisher("Polarsophia")
                .createdDate(now)
                .lastModifiedDate(now)
                .version(21)
                .build();

        JsonContent<BookAggregate> jsonContent = json.write(book);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .isEqualTo(book.getId().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn.value")
                .isEqualTo(book.getIsbn().getValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title.value")
                .isEqualTo(book.getTitle().getValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.getAuthor());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate")
                .isEqualTo(book.getCreatedDate().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate")
                .isEqualTo(book.getLastModifiedDate().toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.getPrice());
        assertThat(jsonContent).extractingJsonPathStringValue("@.publisher")
                .isEqualTo(book.getPublisher());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version")
                .isEqualTo(book.getVersion());
    }

    @Test
    public void testDeserialize() throws Exception {

        Instant instant = Instant.parse("2021-09-07T22:50:37.135029Z");
        JSONObject jsonObject = new JSONObject();
        String content = jsonObject
                .put("id", 394L)
                .put("isbn", "1234567890")
                .put("title", "Title")
                .put("author", "Author")
                .put("price", 9.90)
                .put("publisher", "Polarsophia")
                .put("createdDate", "2021-09-07T22:50:37.135029Z")
                .put("lastModifiedDate", "2021-09-07T22:50:37.135029Z")
                .put("version", 21).toString();

        // Verifying the parsing from JSON to Java
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new BookAggregate(394L, this.isbnCode,
                        this.title, "Author",
                        9.90, "Polarsophia", instant,
                        instant, 21));
    }
}
