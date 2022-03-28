package com.polarbookshop.catalogservice.application.api.rest;

import com.polarbookshop.catalogservice.application.exception.BookNotFoundException;
import com.polarbookshop.catalogservice.application.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class is used to thes the web MVC slice.
 *
 * Spring Boot offers the possibility of using contexts initialized
 * only with a subgroup of components (beans), targeting a specific application slice.
 * Spring MVC controllerscan be tested by using the @WebMvcTest annotation.
 * It loads a Spring application context in a mock web environment (no running server),
 * configures the Spring MVC infrastructure, and includes only the beans used by
 * the MVC layer.
 * It’s a good idea to limit the context to the beans used by the specific controller
 * under test. It can be done by providing the controller class as an argument to the
 * @WebMvcTest annotation.
 */
@WebMvcTest(BookController.class)
class BookControllerMvcTests {

    @Autowired
    private MockMvc mockMvc; // Utility class to test the web layer in a mock environment

    @MockBean
    private BookService bookService; // Adding a mock of BookService to the Spring application context

    @Test
    public void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "73737313940";

        // Defines the expected behavior for the BookService mock bean
        BDDMockito.given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException.class);

        mockMvc
                .perform(get("/books" + isbn))
                .andExpect(status().isNotFound());
    }

}