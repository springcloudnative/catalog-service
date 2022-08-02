package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import com.polarbookshop.catalogservice.domain.dto.BookDTO;
import com.polarbookshop.catalogservice.domain.vo.IsbnCode;
import com.polarbookshop.catalogservice.domain.vo.Title;
import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;
import com.polarbookshop.catalogservice.infrastructure.repository.BaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CatalogServiceApplicationTests extends BaseTest {

	@Autowired
	private WebTestClient webTestClient;	// Utility to perform REST calls for testing

	private Title title = new Title("Title");

	@Test
	void contextLoads() {
	}

	/**
	 * Testing HTTP GET request to get a Book by ISBN.
	 */
	@Test
	@Order(1)
	public void whenGetRequestWithIdThenBookReturned() {

		BookDTO bookToCreate = getDTOForBookCreation();

		BookAggregate expectedBook = webTestClient
					.post()
					.uri("/books")
					.bodyValue(bookToCreate)
					.exchange()
					.expectStatus().isCreated()
					.expectBody(BookAggregate.class).value(book -> assertThat(book).isNotNull())
					.returnResult().getResponseBody();

		System.out.println("expectedBook: " + expectedBook);
		webTestClient
				.get()
				.uri("/books/" + this.isbnCode.getValue())
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(BookAggregate.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.getIsbn().getValue()).isEqualTo(expectedBook.getIsbn().getValue());
				});
	}

	/**
	 * Testing HTTP POST request to save a Book.
	 */
	@Test
	@Order(2)
	public void whenPostRequestThenBookCreated() {

		BookDTO bookToCreate = getDTOForBookCreation();

		webTestClient
				.delete().uri("/books/" + bookToCreate.getIsbn())
				.exchange()
				.expectStatus().isNoContent();

		BookAggregate expectedBook = BookAggregate.builder()
									.isbn(this.isbnCode)
									.title(this.title)
									.author("Author")
									.price(9.90)
									.publisher("Polarsophia")
									.build();

		webTestClient
				.post().uri("/books")	// HTTP POST request to the "/books" endpoint
				.bodyValue(bookToCreate)	// Adds the book in the request body
				.exchange()					// Sends the request
				.expectStatus().isCreated()
				.expectBody(BookAggregate.class).value(actualBook -> {
					Assertions.assertThat(actualBook).isNotNull();
					Assertions.assertThat(actualBook.getIsbn().getValue())
							.isEqualTo(expectedBook.getIsbn().getValue());
				});
	}

	/**
	 * Testing HTTP PUT request to update a Book.
	 */
	@Test
	@Order(3)
	public void whenPutRequestThenBookUpdated() {

		BookDTO bookToCreate = getDTOForBookCreation();

		webTestClient
				.delete().uri("/books/" + bookToCreate.getIsbn())
				.exchange()
				.expectStatus().isNoContent();

		BookAggregate createdBook = webTestClient
					.post()
					.uri("/books")
					.bodyValue(bookToCreate)
					.exchange()
					.expectStatus().isCreated()
					.expectBody(BookAggregate.class).value(book -> {
						assertThat(book).isNotNull();
					})
				.returnResult().getResponseBody();

		BookDTO bookToUpdate = BookDTO.builder()
				.id(createdBook.getId())
				.isbn(createdBook.getIsbn().getValue())
				.title(createdBook.getTitle().getValue())
				.author(createdBook.getAuthor())
				.price(7.95)
				.publisher(createdBook.getPublisher())
				.createdDate(createdBook.getCreatedDate())
				.lastModifiedDate(createdBook.getLastModifiedDate())
				.version(createdBook.getVersion())
				.build();

		webTestClient
				.put().uri("/books/" + bookToUpdate.getIsbn())
				.bodyValue(bookToUpdate)
				.exchange()
				.expectStatus().isOk()
				.expectBody(BookAggregate.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.getPrice()).isEqualTo(bookToUpdate.getPrice());
				});
	}

	/**
	 * Testing HTTP DELETE request to delete a Book.
	 */
	@Test
	@Order(4)
	public void whenDeleteRequestThenBookDeleted() {

		BookDTO bookToCreate = getDTOForBookCreation();

		webTestClient
				.delete().uri("/books/" + bookToCreate.getIsbn())
				.exchange()
				.expectStatus().isNoContent();

		webTestClient
				.post().uri("/books")
				.bodyValue(bookToCreate)
				.exchange()
				.expectStatus().isCreated();

		webTestClient
				.delete().uri("/books/" + bookToCreate.getIsbn())
				.exchange()
				.expectStatus().isNoContent();

		webTestClient
				.get()
				.uri("/books/" + bookToCreate.getIsbn())
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(String.class).value(errorMessage -> {
					assertThat(errorMessage).isEqualTo(String.format("The book with ISBN %s was not found.", bookToCreate.getIsbn()));
				});
	}
}
