package com.polarbookshop.catalogservice.application.api.rest;

import com.polarbookshop.catalogservice.CatalogServiceApplication;
import com.polarbookshop.catalogservice.application.service.BookService;
import com.polarbookshop.catalogservice.domain.aggregate.BookAggregate;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * This abstract base class will be used for Spring Cloud Contract
 * to automatically generate the contracts test.
 * As part of this base class, we define the test setup and configure
 * RestAssureMockMvc for the controllers.
 */
@SpringBootTest(classes = CatalogServiceApplication.class)
@RunWith(SpringRunner.class)
public class BaseTest {

//    @Mock
//    private BookService mockedBookService;

    @Autowired
    private BookController bookController;

    @MockBean
    private BookService mockedBookService;

    @BeforeEach
    void setUp() {
/*

        Mockito.when(mockedBookService.viewBookDetails("1234561231"))
                .thenReturn(BookAggregate.builder()
                        .id(1L)
                        .isbn("1234561231")
                        .title("Java 11")
                        .author("Author")
                        .price(12.9)
                        .publisher("Polarsophia")
                        .version(1)
                        .build());*/
        Mockito.when(mockedBookService.viewBookList())
                .thenReturn(List.of(BookAggregate.builder()
                        .id(1L)
                        .isbn("1234561231")
                        .title("Java 11")
                        .author("Author")
                        .price(12.9)
                        .publisher("Polarsophia")
                        .version(1)
                        .build()));

        RestAssuredMockMvc.standaloneSetup(this.bookController);
    }
}
