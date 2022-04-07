package com.example;

import com.example.courier.Courier;
import com.example.courier.CourierId;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;


public class CreateCourierTest {

    String courierLogin = RandomStringUtils.randomAlphabetic(10);
    String courierPassword = RandomStringUtils.randomAlphabetic(10);
    String courierFirstName = RandomStringUtils.randomAlphabetic(10);

    public ArrayList<String> getLoginPass() {
        ArrayList<String> loginPass = new ArrayList<>();
        loginPass.add(courierLogin);
        loginPass.add(courierPassword);
        return loginPass;
    }

    private final String body = "{\"login\":\"" + getLoginPass().get(0) + "\","
            + "\"password\":\"" + getLoginPass().get(1) + "\"}";

    Courier courierBody = new Courier(
            courierLogin,
            courierPassword,
            courierFirstName
    );

    CourierId id = new CourierId(getLoginPass().get(0), getLoginPass().get(1));

    private final BaseHttp baseHttp = new BaseHttp();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void cleanUp() {
        if (id.getCourierId() != 0)
            baseHttp.doDeleteRequest(baseURI + "/api/v1/courier/" + id.getCourierId());
    }

    @Step("do POST to /api/v1/courier/")
    public Response doPost(Object body) {
        return baseHttp.doPostRequest(baseURI + "/api/v1/courier/", body);
    }

    @Step("do POST bad request to /api/v1/courier/ with empty password")
    public Response doPostBadRequestPasswordEmpty() {
        return baseHttp.doPostRequest(baseURI + "/api/v1/courier/", courierBody.setPassword(null));
    }

    @Step("do POST bad request to /api/v1/courier/ with empty login")
    public Response doPostBadRequestLoginEmpty() {
        return baseHttp.doPostRequest(baseURI + "/api/v1/courier/", courierBody.setLogin(null));
    }

    @Description("code: 201. ok: true.")
    @DisplayName("register courier: positive flow")
    @Test
    public void registerCourierPositiveFlow() {
        Response response = doPost(body);
        response.then().assertThat().statusCode(201);
        System.out.println(response.getBody().asString());
    }

    @Description("code: 400, message: Недостаточно данных для создания учетной записи")
    @DisplayName("register courier: login empty")
    @Test
    public void registerCourierLoginEmpty() {
        Response response = doPostBadRequestLoginEmpty();
        response.then().assertThat().statusCode(400);
        System.out.println(response.getBody().asString());
    }

    @Description("code: 400, message: Недостаточно данных для создания учетной записи")
    @DisplayName("register courier: password empty")
    @Test
    public void registerCourierPasswordEmpty() {
        Response response = doPostBadRequestPasswordEmpty();
        response.then().assertThat().statusCode(400);
        System.out.println(response.getBody().asString());
    }

    @Description("code: 409, message: Этот логин уже используется. Попробуйте другой.")
    @DisplayName("register courier: recurring login")
    @Test
    public void registerCourierRecurringLogin() {
        doPost(body);
        Response response = doPost(courierBody);
        response.then().assertThat().statusCode(409);
        System.out.println(response.getBody().asString());
    }

}

