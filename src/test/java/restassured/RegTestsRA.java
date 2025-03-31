package restassured;

import dto.AuthRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RegTestsRA {
    String endpoint =  "user/registration/usernamepassword";


    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";
    }

    @Test
    public void regSuccess(){
        int i = (int) ((System.currentTimeMillis()/1000)%3600);
        AuthRequestDTO auth = AuthRequestDTO.builder().username("ton" + i + "@gmail.com")
                .password("Tele2user84!").build();

        String token =

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("token");

        System.out.println(token);

    }
    @Test
    public void regWrongEmail(){
        AuthRequestDTO auth = AuthRequestDTO.builder().username("tongmail.com")
                .password("Tele2user84!").build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.username", containsString("must be a well-formed email address"))
        ;

    }
    @Test
    public void regWrongPass(){
        AuthRequestDTO auth = AuthRequestDTO.builder().username("ton@gmail.com")
                .password("Tele").build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.password", containsString("At least 8 characters; Must contain at least 1 uppercase letter"))
        ;

    }

    @Test
    public void regDuplicate(){
        AuthRequestDTO auth = AuthRequestDTO.builder().username("firiall68@gmail.com")
                .password("Tele2user84!").build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(409)
                .assertThat().body("message", containsString("User already exists"))

        ;

    }




}
