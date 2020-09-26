package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.FileUtils.readStringFromFile;

public class RegressInTests {

    @BeforeEach
    void beforeEach() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    void getSingleResourseTest() {
        given()
                .when()
                .get("/unknown/2")
                .then()
                .statusCode(200)
                .log().body()
                .body("data.id", is("2"))
                .body("name", is(notNullValue()));
    }

    @Test
    void successCreateUserTest() {
        String data = readStringFromFile("src/test/resources/users.json");

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .log().body()
                .body("id", is(notNullValue()));
    }

    @Test
    void successUpdateUserTest() {
        String data = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .put("/users/2")
                .then()
                .statusCode(201)
                .log().body()
                .body("job", is("zion resident"));
    }

    @Test
    void successDeleteUserTest() {
        given()
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void unsuccessLoginUserTest() {
        String data = "{\n" +
                "    \"email\": \"peter@klaven\",\n" +
                "}";
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .log().body()
                .body("error", is("Missing password"));
    }

}