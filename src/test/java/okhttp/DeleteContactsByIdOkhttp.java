package okhttp;

import com.google.gson.Gson;
import dto.ErrorDTO;
import dto.MessageDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class DeleteContactsByIdOkhttp {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZmlyaWFsbDY4QGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNzQzOTU0ODcwLCJpYXQiOjE3NDMzNTQ4NzB9.Iu3y94IfZYmL5gXaTmHRVguxnpuhbucu_88YeYYIuQA";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    String id;

    @BeforeMethod
    public void preCondition() throws IOException{
        String name = "Name" + System.currentTimeMillis();
        String lastName = "LastName" + System.currentTimeMillis();
        String email = "Email" + System.currentTimeMillis() + "@gmail.com";
        String phone = "1234567890" + System.currentTimeMillis();
        String address = "Address" + System.currentTimeMillis();


        String json = "{"
                + "\"name\": \"" + name + "\","
                + "\"lastName\": \"" + lastName + "\","
                + "\"email\": \"" + email + "\","
                + "\"phone\": \"" + phone + "\""
                + "\"address\": \"" + address + "\""
                + "}";

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful(), "contact not created");
        String responseBody = response.body().string();
        MessageDto messageDto = gson.fromJson(responseBody, MessageDto.class);

        String message = messageDto.getMessage();
        String[] parts = message.split("ID: ");
        if (parts.length > 1) {
            id = parts[1].trim();
        } else {
            Assert.fail("failed to extract ID: " + message);
        }
        System.out.println("contact created with ID : " + id);

    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .delete()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        MessageDto messageDto = gson.fromJson(response.body().string(), MessageDto.class);
        System.out.println(messageDto.getMessage());
        Assert.assertEquals(messageDto.getMessage(),"Contact was deleted!");
    }

    @Test
    public void deleteContactByIdWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/18bf1a49-d80d-4961-9fc3-792b3b0971c8")
                .delete()
                .addHeader("Authorization", "ghshfdv")
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);
        ErrorDTO errorDto = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDto.getError(),"Unauthorized");
    }

    @Test
    public void deleteContactByIdNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/18bf1a49")
                .delete()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);
        ErrorDTO errorDto = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        //System.out.println(errorDto.getMessage());
        Assert.assertEquals(errorDto.getMessage(),"Contact with id: 18bf1a49 not found in your contacts!");
    }
}


//88550343-cada-4411-9a3b-a801abae319b
//wow1603@gmail.com
//================================
//a8cb6927-d2c8-4d02-950e-3e0bd3465e70
//wow1351@gmail.com
//================================
//a45edde1-ddab-49df-8167-7a5125c4d201
//wow1160@gmail.com
//================================
//        18bf1a49-d80d-4961-9fc3-792b3b0971c8
//tanya@maol.com
//================================
//ea91ad86-f9a7-4416-9591-bb432733bb03
//vera@vera.ru
//================================
//        6fbbc54d-8158-49de-843c-fa70e9230e0b
//olsana@com.com
//================================
