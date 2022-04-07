package com.example;

import com.example.courier.CourierId;
import com.example.courier.CredentialsCourier;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.*;


@RunWith(Parameterized.class)
public class CourierLoginWithParametersTest {

    private static final CredentialsCourier credentials = new CredentialsCourier();
    private final BaseHttp baseHttp = new BaseHttp();
    CourierId id = new CourierId(login, password);


    @Parameterized.Parameter
    public String setLogin;

    @Parameterized.Parameter(1)
    public String setPassword;

    @Parameterized.Parameter(2)
    public int statusCode;

    @Parameterized.Parameters(name = "login = {0} | password = {1} | expected statusCode = {2}")
    public static Object[][] getData() {
        return new Object[][]{
                {login, password, 200},
                {login, null, 400},
                {null, password, 400},
                {login, wrongPassword, 404},
                {wrongLogin, password, 404},
        };
    }

    static String login = credentials.getCredentials().get(0);
    static String wrongLogin = login + RandomStringUtils.randomAlphabetic(1);
    static String password = credentials.getCredentials().get(1);
    static String wrongPassword = password + RandomStringUtils.randomAlphabetic(1);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void cleanUp() {
        if (id.getCourierId() != 0)
            baseHttp.doDeleteRequest(baseURI + "/api/v1/courier/" + id.getCourierId());
    }

    @Step("do POST to /api/v1/courier/login with any credentials (login and password)")
    public Response doPostWithAnyCredentials() {
        String body = "";
        if (statusCode == 400) {
            if (setLogin == null) {
                body = "{\"login\":\"" + "\",\"password\":\"" + setPassword + "\"}";
            } else if (setPassword == null) {
                body = "{\"login\":\"" + setLogin + "\",\"password\":\"" + "\"}";
            }
        } else {
            body = "{\"login\":\"" + setLogin + "\",\"password\":\"" + setPassword + "\"}";
        }
        return baseHttp.doPostRequest(baseURI + "/api/v1/courier/login", body);
    }

    @Description("check is performed with a different set of login, password and expected statusCode")
    @Test
    public void credentialsTest() {
        Response response = doPostWithAnyCredentials();
        response.then().assertThat().statusCode(statusCode);
        System.out.println(response.getBody().asString());
    }
}
