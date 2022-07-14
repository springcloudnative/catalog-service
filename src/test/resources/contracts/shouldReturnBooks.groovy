package contracts

import org.apache.http.HttpHeaders
import org.springframework.cloud.contract.spec.Contract
import org.springframework.http.MediaType

Contract.make {
    description "Should return a list of books"

    request {
        method GET()
        url '/books/'
    }

    response {
        status OK()
        headers {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        }
        body ([ [id: 1L, isbn: "1234561231", title: "Java 11", author: "Author", price: 12.9, publisher: "Polarsophia", version: 1] ])
    }
}

