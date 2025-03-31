package restassured;

import dto.ContactDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactByID {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZmlyaWFsbDY4QGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNzQzOTU0ODcwLCJpYXQiOjE3NDMzNTQ4NzB9.Iu3y94IfZYmL5gXaTmHRVguxnpuhbucu_88YeYYIuQA";
    //String endpoint =  "contacts";
    String id;


    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";

        int i = new Random().nextInt(1000) + 1000;
        ContactDto contactDto = ContactDto.builder()
                .name("Maya")
                .lastName("Dow")
                .email("maya" + i + "@gmail.com")
                .phone("12345565" + i)
                .address("TA")
                .description("Friend")
                .build();

        String message = given()
                .body(contactDto)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                //"Contact was added! ID: a4a33d33-b00e-4049-8570-dfd07aace9c7"
                .extract()
                .path("message");
        String[] all = message.split(": ");
        id = all[1];
    }


    @Test
    public void deleteContactByIdSuccess() {
        given()
                .header("Authorization", token)
                .when()
                .delete("contacts/" + id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", equalTo("Contact was deleted!"));


    }

    @Test
    public void deleteContactByIdWrongToken() {
        given()
                .header("Authorization", "ghsfv")
                .when()
                .delete("contacts/" + id)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error", equalTo("Unauthorized"));
    }
}
