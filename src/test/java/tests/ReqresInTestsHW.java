package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.get;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReqresInTestsHW {

    @Test
    void resourceNotFound() {

        String response = given()
                .when()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .statusCode(404)
                .extract().asString();

        assertEquals("{}", response);
    }

    @Test
    void resourceNotFoundOtherWay() {

        given()
                .when()
                .get("https://reqres.in/api/unknown/23")
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
                .post("https://reqres.in/api/register")
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
                .post("https://reqres.in/api/register")
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
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("provodnik"));
    }

    @Test
    void countUsersPerPage1() {

        given()
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then()
                .statusCode(200)
               .body("data", everyItem(hasKey("id")))
                .body("data.id", hasSize(6));
    }


    @Test
    void validateJsonSchema() {

      given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .assertThat().body(matchesJsonSchemaInClasspath("usr_sch.json"));



        //System.out.println(response);



        //  .body("data", hasItems("id", "email", "first_name", "last_name", "avatar"));
    }
}
