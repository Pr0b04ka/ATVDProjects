package org.Vlad.lab3;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PetTests {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    private static final String PET = "/pet";
    private static final String PET_BY_ID = PET + "/{petId}";

    private int petId;
    private String petName;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyCreatePet() {
        petId = 12222218;
        petName = "VladPet";

        Map<String, Object> body = Map.of(
                "id", petId,
                "name", petName,
                "status", "available",
                "photoUrls", List.of("string"),
                "category", Map.of("id", 1, "name", "dogs")
        );

        given()
                .body(body)
                .post(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(petName));
    }

    @Test(dependsOnMethods = "verifyCreatePet")
    public void verifyGetPet() {
        given()
                .pathParam("petId", petId)
                .get(PET_BY_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(petId))
                .body("name", equalTo(petName));
    }

    @Test(dependsOnMethods = "verifyGetPet")
    public void verifyUpdatePet() {
        String updatedName = "VladUpdatedPet";

        Map<String, Object> body = Map.of(
                "id", petId,
                "name", updatedName,
                "status", "sold",
                "photoUrls", List.of("string"),
                "category", Map.of("id", 1, "name", "dogs")
        );

        given()
                .body(body)
                .put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(updatedName));
    }
}