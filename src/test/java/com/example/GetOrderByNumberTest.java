package com.example;

import com.example.order.OrderTrackAndId;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;


public class GetOrderByNumberTest {

    private final String JSON = "application/json";
    OrderTrackAndId orderTrackAndId = new OrderTrackAndId();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("get track number")
    public int getTrack() {
        return orderTrackAndId.getTrackNumber();
    }

    @Description("do Get request to /api/v1/orders/track and response assert statusTrack 200")
    @DisplayName("order by number - Positive test")
    @Test
    public void orderByNumberPositiveTest() {
        Response response = given()
                .header("Content-Type", JSON)
                .queryParam("t", getTrack())
                .get(baseURI + "/api/v1/orders/track");
        response.then().assertThat().statusCode(200);
        System.out.println(response.getBody().asString());

    }

    @Description("attempt to receive an order with a changed tracking number " +
            "\"code\":404,\"message\":\"Заказ не найден\"")
    @DisplayName("order by number - Error test")
    @Test
    public void orderByNumberErrorTest() {
        Response response = given()
                .header("Content-Type", JSON)
                .queryParam("t", (getTrack() - 5000))
                .get(baseURI + "/api/v1/orders/track");
        response.then().assertThat().statusCode(404);
        System.out.println(response.getBody().asString());

    }

    @DisplayName("order Error test")
    @Description("attempt to receive an order without a tracking number:" +
            "\"code\":400,\"message\":\"Недостаточно данных для поиска\"")
    @Test
    public void orderErrorTest() {
        Response response = given()
                .header("Content-Type", JSON)
                .queryParam("t", "")
                .get(baseURI + "/api/v1/orders/track");
        response.then().assertThat().statusCode(400);
        System.out.println(response.getBody().asString());

    }
}
