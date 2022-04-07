package com.example;

import com.example.order.BodyOrder;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)

public class CreateOrderWithParametersTest {

    private final BaseHttp baseHttp = new BaseHttp();
    private final BodyOrder order = new BodyOrder();


    @Parameterized.Parameter
    public String setColorList;

    @Parameterized.Parameter(1)
    public int statusCode;

    @Parameterized.Parameters(name = "colorList = {0} | expected statusCode = {1}")
    public static Collection<Object[]> dataColor() {
        return Arrays.asList(new Object[][]{
                {"BLACK", 201},
                {"GRAY", 201},
                {"", 201},
                {"BLACK, GRAY", 201}
        });
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @Step("set colors in order body, get POST request to /api/v1/orders/ and getting response")
    public Response doPost() {
        return baseHttp.doPostRequest(baseURI + "/api/v1/orders/", order.getOrderBody().setColor(new String[]{setColorList}));
    }

    @Description("do POST to /api/v1/orders/ and get statusCode 201: order created")
    @Test()
    public void checkCreateOrderWithColorTest() {
        Response response = doPost();
        response.then().assertThat().statusCode(statusCode)
                .and()
                .body("track", notNullValue());
        System.out.println((Number) response.getBody().jsonPath().get("track"));
        System.out.println(setColorList);
    }

}
