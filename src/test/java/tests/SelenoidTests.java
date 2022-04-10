package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {
    // make request to https://selenoid.autotests.cloud/status
    // total is 20

    @Test
    void checkTotal() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotalWithoutGiven() {
       get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }


    @Test
    void checkBrowserVersion() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("browsers.chrome", hasKey("99.0"));
    }

    @Test
    void checkTotalBadPractice() {
        String response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response().asString();

        //здесь сделали, чтобы потом заполнить expectedResponse
        System.out.println("Response: " + response);

        String expectedResponse = "{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0," +
                "\"browsers\":" +
                "{\"chrome\":{\"100.0\":{},\"99.0\":{}}," +
                "\"firefox\":{\"97.0\":{},\"98.0\":{}}," +
                "\"opera\":{\"84.0\":{},\"85.0\":{}}}}\n";
        assertEquals(expectedResponse, response);

        //bad птмч не читаемо, неудобно, нужно привести к какому-то подходу
    }

    @Test
    void checkTotalGoodPractice() {
        //good в плане луче, чем предыдущий в-т, но удобнее конечно более верхний в-т, где все сделано в три строки checkTotalWithoutGiven
        int response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract()
                .path("total"); //берем только нужное

        System.out.println("Response: " + response);


        int expectedResponse = 20; //взяли только нужное для проверки, стало легковеснее и читаемее
        assertEquals(expectedResponse, response);
    }

    @Test
    void extractResponse() {
        String response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response().asString();

        System.out.println("Response: " + response);
    }

    @Test
    @DisplayName("примеры работы с response")
    void responseExamples() {
        Response response = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response();

        System.out.println("Response: " + response); //отдаст ерунду
        System.out.println("Response .toString(): " + response.toString()); //отдаст ерунду
        System.out.println("Response .asString(): " + response.asString()); //отдаст весь ответ
        System.out.println("Response .path(\"total\"): " + response.path("total")); //отдаст запрашиваемые данные из ответа
        System.out.println("Response .path(\"browsers.chrome\"): " + response.path("browsers.chrome")); //отдаст запрашиваемые данные из ответа
    }
    @Test
    void checkStatus401() {
        get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(401);
    }

    @Test
    void checkStatus200() {
        get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200);
    }

    @Test
    void checkStatus200WithAuth() {
        given()
                .auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200);
    }

    @Test
    void checkStatus200ExtractResponse() {
        Response response = given()
                .auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200)
                .extract().response();
        System.out.println(response.asString());
    }

}
