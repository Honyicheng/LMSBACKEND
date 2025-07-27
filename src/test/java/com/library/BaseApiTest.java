package com.library;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7000;             // Gateway port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}