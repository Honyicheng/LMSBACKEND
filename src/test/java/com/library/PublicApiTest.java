package com.library;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

public class PublicApiTest extends BaseApiTest {

    @Test
    void getPublicBooks_returns200AndList() {
        given().
                when().
                get("/api/public/books").
                then().
                statusCode(200).
                body("$", not(hasSize(0)));
    }
}


