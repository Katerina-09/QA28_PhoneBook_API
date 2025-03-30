package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDTO;
import dto.AuthResponseDTO;
import dto.ErrorDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class RegistrationTestsOkhttp {
    Gson gson = new Gson();
    public  static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    @Test
    public void RegSuccess() throws IOException {
        String randomEmail = "user" + UUID.randomUUID().toString() + "@gmail.com";
        String password = "Tele2user85!";
        AuthRequestDTO auth = AuthRequestDTO.builder().username(randomEmail)
                .password(password).build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        AuthResponseDTO responseDTO = gson.fromJson(response.body().string(), AuthResponseDTO.class);
        System.out.println(responseDTO.getToken());

    }

    @Test
    public void RegWrongEmail() throws IOException {
        AuthRequestDTO auth = AuthRequestDTO.builder().username("firiall58gmail.com")
                .password("Tele2user85!")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        String responseBody = response.body().string();
        ErrorDTO errorDTO = gson.fromJson(responseBody, ErrorDTO.class);

        // AuthResponseDTO responseDTO = gson.fromJson(response.body().string(), AuthResponseDTO.class);
        Assert.assertEquals(errorDTO.getStatus(), 400);
        //Assert.assertEquals(errorDTO.getMessage(), "{username=must be a well-formed email address}");
        Map<String, String> messageMap = (Map<String, String>) errorDTO.getMessage();
        Assert.assertEquals(messageMap.get("username"), "must be a well-formed email address");


    }

    @Test
    public void RegWrongPassword() throws IOException {
        AuthRequestDTO auth = AuthRequestDTO.builder().username("firiall48@gmail.com")
                .password("Tele2").build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        String responseBody = response.body().string();
        ErrorDTO errorDTO = gson.fromJson(responseBody, ErrorDTO.class);

        // AuthResponseDTO responseDTO = gson.fromJson(response.body().string(), AuthResponseDTO.class);
        Assert.assertEquals(errorDTO.getStatus(), 400);
        Map<String, String> messageMap = (Map<String, String>) errorDTO.getMessage();
        Assert.assertEquals(messageMap.get("username"), null);

    }

    @Test
    public void RegistredUser() throws IOException {
        AuthRequestDTO auth = AuthRequestDTO.builder().username("firiall68@gmail.com")
                .password("Tele2user84!").build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 409);
        String responseBody = response.body().string();
        ErrorDTO errorDTO = gson.fromJson(responseBody, ErrorDTO.class);

        // AuthResponseDTO responseDTO = gson.fromJson(response.body().string(), AuthResponseDTO.class);
        Assert.assertEquals(errorDTO.getStatus(), 409);
        Assert.assertEquals(errorDTO.getMessage(), "User already exists");

    }


}