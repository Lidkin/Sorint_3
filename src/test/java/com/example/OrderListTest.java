package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {

private final BaseHttp baseHttp = new BaseHttp();

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("do GET request to /api/v1/orders/ for the entire list of orders without a courier id")
    public Response doGet(){
        return baseHttp.doGetRequest(baseURI + "/api/v1/orders/");
    }

    @Test
    @Description("statusCode 200 and list of orders is returned in the response body")
    @DisplayName("get order list without courier Id test")
    public void getOrderListWithoutCourierIdTest(){
        Response response = doGet();
        response.then().statusCode(200);
        response.then().assertThat().body("orders",notNullValue());
        System.out.println(response.getBody().asString());

    }
}
