package tests.in.reqres;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReqresInTestsMoreApiExamplesFromOtherRepos extends TestBaseReqres {

    @Test
    void updatePutTest() {
        Instant timestamp = Instant.now();
        timestamp = timestamp.minusSeconds(5); //вычитает 5 секунд из исходного значения

        String updateData = "{\"name\":\"morpheus\",\"job\":\"zion resident\"}";

        Response response = given()
                .body(updateData)
                .contentType(JSON)
                .when()
                .put("/api/users/2")
                .then()
                .extract()
                .response();

        Instant reqInstant = Instant.parse(response.path("updatedAt"));
        System.out.println(timestamp.toString());
        System.out.println(reqInstant.toString());
        System.out.println(timestamp.isBefore(reqInstant));

        assertTrue(timestamp.isBefore(reqInstant));
    }

    @Test
    void registerSuccessfulTest() {
        String registerData = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}";

        given()
                .body(registerData)
                .contentType(JSON)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id", greaterThan(0))
                .body("token.length()", greaterThan(0)); //я в своем коде в соседнем файле использовал подход - "значение не ноль или не пустое",
        // но иногда нам нужно явно указать что оно например положительное
    }

    @Test
    void listResourceUnknownTest() {

        Response response = get("/api/unknown")
                .then()
                .extract()
                .response();

        int resPage = response.path("per_page");

        ArrayList<Integer> idList = new ArrayList<>(response.path("data.id"));

        assertEquals(resPage, idList.size());
    }

    @Test
    void singleUserTest() {
        get("/api/users/2")
                .then()
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"));
    }

    @Test
    void listResourceUnknownTestByObj() {

        Response response = get("/api/unknown")
                .then()
                .extract()
                .response();

        int resPage = response.path("per_page");

        ArrayList<Integer> objectList = new ArrayList<>(response.path("data"));
        assertEquals(resPage, objectList.size());
    }
}
