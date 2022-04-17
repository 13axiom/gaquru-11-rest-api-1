package tests.in.reqres;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBaseReqres {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in";
    }

}
