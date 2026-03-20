package org.Vlad.lab4;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LabThree {

    private static final String BASE_URL = "https://901dea99-827c-4db9-90b8-3facd1101b98.mock.pstmn.io";

    private static final String CREATE_PET = "/pet/success";
    private static final String GET_PET = "/pet/12222218/success";
    private static final String UPDATE_PET = "/pet/update/success";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    // POST (create)
    @Test
    public void verifyCreatePetMock() {

        Map<String, Object> body = Map.of(
                "id", 12222218,
                "name", "VladPet"
        );

        given()
                .body(body)
                .post(CREATE_PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("VladPet"));
    }

    // GET
    @Test
    public void verifyGetPetMock() {

        given()
                .get(GET_PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(12222218))
                .body("name", equalTo("VladPet"));
    }

    // PUT
    @Test
    public void verifyUpdatePetMock() {

        Map<String, Object> body = Map.of(
                "id", 12222218,
                "name", "VladUpdatedPet"
        );

        given()
                .body(body)
                .put(UPDATE_PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("VladUpdatedPet"));
    }

    // NEGATIVE TEST
    @Test
    public void verifyErrorResponse() {

        given()
                .get("/pet/unsuccess")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}