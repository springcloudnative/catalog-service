package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

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
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;	// Utility to perform REST calls for testing

	@Test
	void contextLoads() {
	}

	@Test
	public void whenGetRequestWithIdThenBookReturned() {
		String bookIsbn = "1231231230";
		BookEntity bookToCreate = BookEntity.build(bookIsbn, "Title", "Author", 9.90);

		BookEntity expectedBook = webTestClient
					.post()
					.uri("/books")
					.bodyValue(bookToCreate)
					.exchange()
					.expectStatus().isCreated()
					.expectBody(BookEntity.class).value(book -> assertThat(book).isNotNull())
					.returnResult().getResponseBody();

		webTestClient
				.get()
				.uri("/books/" + bookIsbn)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(BookEntity.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
				});
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

	@Test
	public void whenPutRequestThenBookUpdated() {
		String bookIsbn = "1231231232";
		BookAggregate bookToCreate = BookAggregate.builder()
				.id(1L)
				.isbn(bookIsbn)
				.title("Title")
				.author("Author")
				.price(9.90)
				.version(1)
				.build();

		BookAggregate createdBook = webTestClient
					.post().uri("/books")
					.bodyValue(bookToCreate)
					.exchange()
					.expectStatus().isCreated()
					.expectBody(BookAggregate.class).value(book -> {
						assertThat(book).isNotNull();
					})
				.returnResult().getResponseBody();

		BookAggregate bookToUpdate = BookAggregate.builder()
				.isbn(createdBook.getIsbn())
				.title(createdBook.getTitle())
				.author(createdBook.getAuthor())
				.price(7.95)
				.build();

		webTestClient
				.put().uri("/books/" + bookIsbn)
				.bodyValue(bookToUpdate)
				.exchange()
				.expectStatus().isOk()
				.expectBody(BookAggregate.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.getPrice()).isEqualTo(bookToUpdate.getPrice());
				});
	}

	@Test
	public void whenDeleteRequestThenBookDeleted() {
		String bookIsbn = "1231231233";
		BookAggregate bookToCreate = BookAggregate.builder()
				.isbn(bookIsbn)
				.title("Title")
				.author("Author")
				.price(9.90)
				.build();

		webTestClient
				.post().uri("/books")
				.bodyValue(bookToCreate)
				.exchange()
				.expectStatus().isCreated();

		webTestClient
				.delete().uri("/books/" + bookIsbn)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient
				.get()
				.uri("/books/" + bookIsbn)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(String.class).value(errorMessage -> {
					assertThat(errorMessage).isEqualTo("The book with ISBN " + bookIsbn + " was not found.");
				});
	}
}
