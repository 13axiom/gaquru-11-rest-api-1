package tests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


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


    @Test //todo
    @Disabled
    void dataObjectContainsReqKeys() {
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("data", hasItems("id", "email", "first_name", "last_name", "avatar"));
    }
}
