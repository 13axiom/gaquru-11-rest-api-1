package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresInTestsHW extends TestBase {

    @Test
    void resourceNotFound() {

        String response = given()
                .when()
                .get("/api/unknown/23")
                .then()
                .statusCode(404)
                .extract().asString();

        assertEquals("{}", response);
    }

    @Test
    void resourceNotFoundOtherWay() {
        get("/api/unknown/23")
                .then()
                .statusCode(404)
                .body("data", is(emptyOrNullString()));
    }

    @Test
    void successfulRegister() {

        String registerData = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .body(registerData)
                .contentType(JSON)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("token", is(notNullValue()));
    }

    @Test
    void failedRegisterWithoutPass() {

        String registerDataWithoutPass = "{\"email\": \"peter@klaven\"}";

        given()
                .body(registerDataWithoutPass)
                .contentType(JSON)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void updateUserJob() {
        String updatedInfo = "{ \"name\": \"morpheus\", \"job\": \"provodnik\" }";
        given()
                .body(updatedInfo)
                .contentType(JSON)
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("provodnik"));
    }

    @Test
    void countUsersPerPage1() {

        get("/api/users?page=1")
                .then()
                .statusCode(200)
                .body("data", everyItem(hasKey("id")))
                .body("data.id", hasSize(6));
    }


    @Test
    void validateJsonSchema() {

        get("/api/users?page=2")
                .then()
                .assertThat().body(matchesJsonSchemaInClasspath("usr_sch.json"));
    }

    @Test
    void checkThatUserListHasId7() {
        get("/api/users?page=2")
                .then()
                .body("data.id", hasItems(7));
    }

    @Test
    void checkThatUserListHasId7And9() {
        get("/api/users?page=2")
                .then()
                .body("data.id", hasItems(7, 9));
    }

    @Test
    void checkThatSecondPageOfUserListHasntId6() {
        get("/api/users?page=2")
                .then()
                .body("data.id", not(hasItems(6)));
    }

    @Test
    void checkThatUserListHasUserWitnName() {
        get("/api/users?page=2")
                .then()
                .body("data.first_name", hasItems("Rachel"));
    }
}
