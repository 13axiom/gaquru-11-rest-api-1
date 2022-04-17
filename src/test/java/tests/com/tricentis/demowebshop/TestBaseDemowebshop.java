package tests.com.tricentis.demowebshop;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBaseDemowebshop {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
    }

}
