package com.example;

import com.example.courier.CredentialsCourier;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;


import static io.restassured.RestAssured.baseURI;


public class DeleteCourierTest {
    private final BaseHttp baseHttp = new BaseHttp();
    private final CredentialsCourier credentials = new CredentialsCourier();

    String login = "\"login\":\"" + credentials.getCredentials().get(0);
    String password = "\",\"password\":\"" + credentials.getCredentials().get(1);
    String body = "{" + login + password + "\"}";
    public int statusCode;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("get courier ID: creating a new courier to get his id")
    public String getCourierId() {
        final String id;
        Response response = baseHttp.doPostRequest(baseURI + "/api/v1/courier/login", body);
        response.then().assertThat().statusCode(200);
        id = response.getBody().jsonPath().get("id").toString();
        return id;
    }


    @Step("do delete courier and get response: get a response to request DELETE depending on the statusCode")
    public Response doDeleteCourierAndGetResponse() {
        Response response = null;
        if (statusCode == 400) {
            response = baseHttp.doDeleteRequestWithBody(baseURI + "/api/v1/courier/");
        } else if (statusCode == 404) {
            response = baseHttp.doDeleteRequest(baseURI + "/api/v1/courier/" + "123");
        } else if (statusCode == 200) {
            response = baseHttp.doDeleteRequest(baseURI + "/api/v1/courier/" + getCourierId());
        }
        return response;
    }

    @Description("do DELETE request to /api/v1/courier/ with expected statusCode 200:" +
            "{\"ok\":true}")
    @DisplayName("delete courier - positive test")
    @Test
    public void deleteCourierPositiveTest() {
        statusCode = 200;
        Response response = doDeleteCourierAndGetResponse();
        response.then().assertThat().statusCode(statusCode);
        System.out.println(response.getBody().asString());
    }

    @Description("do DELETE request to /api/v1/courier/ with expected statusCode 400")
    @DisplayName("delete courier - bad request test")
    @Test
    public void deleteCourierBadRequestTest() {
        statusCode = 400;
        Response response = doDeleteCourierAndGetResponse();
        response.then().assertThat().statusCode(statusCode);
        System.out.println(response.getBody().asString());
    }

    @Description("do DELETE request to /api/v1/courier/ with expected statusCode 404:" +
            "{\"code\":404,\"message\":\"Курьера с таким id нет.\"}")
    @DisplayName("delete courier - not found test")
    @Test
    public void deleteCourierNotFoundTest() {
        statusCode = 404;
        Response response = doDeleteCourierAndGetResponse();
        response.then().assertThat().statusCode(statusCode);
        System.out.println(response.getBody().asString());
    }

}

