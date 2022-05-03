package com.example;

import com.example.courier.CourierClient;
import com.example.courier.CourierId;
import com.example.courier.CredentialsCourier;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CourierLoginWithParametersTest {

    private static final CredentialsCourier credentials = new CredentialsCourier();
    CourierId id = new CourierId(login, password);
    CourierClient posting = new CourierClient();
    CourierClient courierClient = new CourierClient();
    static String login = credentials.getCredentials().get(0);
    static String wrongLogin = login + RandomStringUtils.randomAlphabetic(1);
    static String password = credentials.getCredentials().get(1);
    static String wrongPassword = password + RandomStringUtils.randomAlphabetic(1);

    @Parameterized.Parameter
    public String courierLogin;

    @Parameterized.Parameter(1)
    public String courierPassword;

    @Parameterized.Parameter(2)
    public int statusCode;

    @Parameterized.Parameters(name = "login = {0}, password = {1} | {2}")
    public static Object[][] getData() {
        return new Object[][]{
                {login, password, 200},
                {login, null, 400},
                {null, password, 400},
                {login, wrongPassword, 404},
                {wrongLogin, password, 404},
        };
    }

    @After
    public void cleanUp() {
        if (id.getCourierId() != 0)
           courierClient.doDelete(id.getCourierId());
    }

    @Description("check is performed with a different set of login, password and expected statusCode")
    @Test
    public void credentialsTest() {
        Response response = posting.doPostByLogin(statusCode, courierPassword, courierLogin);
        response.then().assertThat().statusCode(statusCode);
    }
}
