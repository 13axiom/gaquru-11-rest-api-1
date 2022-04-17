package tests.com.tricentis.demowebshop;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class DemowebshopTestsHW extends TestBaseDemowebshop {

    @Test
    void successfulSuscribeForNews() {

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; " +
                        "Nop.customer=a564969f-9309-44fc-8f4d-fa542345d05b")
                .formParam("email", "test@mailinator.com")
                .when()
                .post("/subscribenewsletter")
                .then()
                .log().all()
                .statusCode(200)
                .body("Success", is(true))
                .body("Result", is("Thank you for signing up! A verification email has been sent. We appreciate your interest."));
    }


    /*
     curl --location --request POST 'http://demowebshop.tricentis.com/subscribenewsletter' \
--header 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
--header 'Cookie: ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687; Nop.customer=a564969f-9309-44fc-8f4d-fa542345d05b' \
--data-raw 'email=dsa%40dsa.cds'
*/
}
