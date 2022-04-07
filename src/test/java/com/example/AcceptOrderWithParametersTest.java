package com.example;

import com.example.courier.CourierId;
import com.example.courier.CredentialsCourier;
import com.example.order.OrderTrackAndId;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.baseURI;


@RunWith(Parameterized.class)
public class AcceptOrderWithParametersTest {

    private static final CredentialsCourier credentials = new CredentialsCourier();
    private static final BaseHttp baseHttp = new BaseHttp();

    static OrderTrackAndId orderTrackAndId = new OrderTrackAndId();
    static String login = credentials.getCredentials().get(0), password = credentials.getCredentials().get(1);
    static CourierId id = new CourierId(login, password);
    static int courierId = id.getCourierId(), orderId = orderTrackAndId.getOrderId();


    @Parameterized.Parameter
    public int setOrderId;

    @Parameterized.Parameter(1)
    public int setCourierId;

    @Parameterized.Parameter(2)
    public int statusCode;

    @Parameterized.Parameters(name = "orderId = {0} | courierId = {1} | statusCode = {2}")
    public static Collection<Integer[]> dataParameters() {
        return Arrays.asList(new Integer[][]{
                {orderId, courierId, 200},
                {0, courierId, 400},
                {orderId, 0, 400},
                {11111111, courierId, 404},
                {orderId, 1, 404}
        });
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @AfterClass
    public static void cleanUp() {
        baseHttp.doDeleteRequest(baseURI + "/api/v1/courier/" + courierId);
    }

    @Step("send request PUT to /api/v1/orders/accept/ with courierId and orderId")
    public Response doPutWithParam() {
        return baseHttp.doPutRequestWithParam(baseURI + "/api/v1/orders/accept/" + setOrderId, setCourierId);
    }

    @Step("depending on the empty parameter (courierId or orderId), do PUT request to /api/v1/orders/accept/ ")
    public Response doPut() {
        Response response;
        if (setOrderId == 0) {
            response = baseHttp.doPutRequest(baseURI + "/api/v1/orders/accept/" + setCourierId);
        } else {
            response = baseHttp.doPutRequest(baseURI + "/api/v1/orders/accept/" + setOrderId);
        }
        return response;
    }

    @Description("test different variants of the login/password pair")
    @Test
    public void acceptOrderTest() {
        Response response;
        if (statusCode == 400) {
            response = doPut();
        } else {
            response = doPutWithParam();
        }
        response.then().assertThat().statusCode(statusCode);
        System.out.println(response.getBody().asString());
    }

}
