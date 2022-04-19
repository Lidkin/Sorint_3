package com.example.order;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTrackAndId {
    int number;

    public Response getRequestOrders(){
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get("https://qa-scooter.praktikum-services.ru/api/v1/orders");
        return response;
    }

    public int getTrackNumber() {
        String t = RandomStringUtils.randomNumeric(2);
        number = Integer.parseInt(t);
        if (number >= 30) {
            number = 29;
        }
        getRequestOrders().then().assertThat().body("orders.track", notNullValue());
        String getTracks = getRequestOrders().getBody().jsonPath().get("orders.track").toString();
        String[] tracks = getTracks.substring(1, (getTracks.length() - 1)).replaceAll(",", "").split(" ");
        return Integer.parseInt(tracks[number]);
    }

    public int getOrderId(){
        String t = RandomStringUtils.randomNumeric(2);
        number = Integer.parseInt(t);
        if (number >= 30) {
            number = 29;
        }
        getRequestOrders().then().assertThat().body("orders.id", notNullValue());
        String getId = getRequestOrders().getBody().jsonPath().get("orders.id").toString();
        String[] tracks = getId.substring(1, (getId.length() - 1)).replaceAll(",", "").split(" ");
        return Integer.parseInt(tracks[number]);
    }

}
