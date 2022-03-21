package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * This class represents an integration test.
 * The @SpringBootTest annotation is used to bootstrap
 * an application context automatically when running tests.
 * The configuration used can be customized if needed.
 * Otherwise, the class annotated with @SpringBootApplication
 * will become the configuration source for component scanning
 * and properties, including the usual autoconfiguration provided
 * by Spring Boot.
 *
 * When working with web applications, we can run the tests on a mock
 * environment or a running server, configuring this through the webEnvironment
 * attribute of @SpringBootTest
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;	// Utility to perform REST calls for testing

	@Test
	void contextLoads() {
	}

	@Test
	public void whenPostRequestThenBookCreated() {
		BookAggregate expectedBook = BookAggregate.builder()
									.isbn("1231231231")
									.title("Title")
									.author("Author")
									.price(9.90)
									.build();

		webTestClient
				.post().uri("/books")	// HTTP POST request to the "/books" endpoint
				.bodyValue(expectedBook)	// Adds the book in the request body
				.exchange()					// Sends the request
				.expectStatus().isCreated()
				.expectBody(BookAggregate.class).value(actualBook -> {
					Assertions.assertThat(actualBook).isNotNull();
					Assertions.assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
				});
	}

}
