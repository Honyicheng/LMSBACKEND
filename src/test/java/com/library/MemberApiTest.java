
// src/test/java/com/library/MemberApiTest.java
package com.library;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class MemberApiTest extends BaseApiTest {

    private String obtainToken(String username, String password) {
        String json = given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", username, "password", password))
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract().asString();

        return JsonPath.from(json).getString("accessToken");
    }

    @Test
    void viewMemberProfile_withValidToken_returns200() {
        String token = obtainToken("member1@example.com", "password123");

        given()
                .auth().oauth2(token)
                .when()
                .get("/api/member/me")
                .then()
                .statusCode(200)
                .body("email", equalTo("member1@example.com"));
    }

    @Test
    void borrowBook_underLimit_returns201() {
        String token = obtainToken("member1@example.com", "password123");

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(Map.of("bookIsbn", "978-0134685991"))
                .when()
                .post("/api/member/loans")
                .then()
                .statusCode(201)
                .body("dueDate", notNullValue());
    }
}

