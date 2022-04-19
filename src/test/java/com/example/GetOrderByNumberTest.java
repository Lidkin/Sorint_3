package com.example;

import com.example.order.OrderClient;
import com.example.order.OrderTrackAndId;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

public class GetOrderByNumberTest {

    final OrderClient receipt = new OrderClient();
    final OrderTrackAndId orderTrackAndId = new OrderTrackAndId();

    @Description("do Get request to /api/v1/orders/track and response assert statusTrack 200")
    @DisplayName("order by number Positive test")
    @Test
    public void orderByNumberPositiveTest() {
        Response response = receipt.doGetByTrack(orderTrackAndId.getTrackNumber());
        response.then().assertThat().statusCode(200);
        Assert.assertEquals("HTTP/1.1 200 OK", response.statusLine());

    }

    @Description("attempt to receive an order with a non-existent tracking number")
    @DisplayName("order by number Error test")
    @Test
    public void orderByNumberErrorTest() {
        Response response = receipt.doGetByTrack(1);
        response.then().assertThat().statusCode(404);
        Assert.assertEquals("HTTP/1.1 404 Not Found", response.statusLine());
        Assert.assertEquals("Заказ не найден", response.getBody().path("message"));

    }

    @DisplayName("order Error test")
    @Description("attempt to receive an order without a tracking number")
    @Test
    public void orderErrorTest() {
        Response response = receipt.doGetByTrack("");
        response.then().assertThat().statusCode(400);
        Assert.assertEquals("HTTP/1.1 400 Bad Request", response.statusLine());
        Assert.assertEquals("Недостаточно данных для поиска", response.getBody().path("message"));

    }
}
