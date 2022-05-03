package com.example;

import com.example.order.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {

    final OrderClient receipt = new OrderClient();

    @Test
    @Description("statusCode 200 and list of orders is returned in the response body")
    @DisplayName("get order list without courier Id test")
    public void getOrderListTest(){
        Response response = receipt.doGet();
        response.then().statusCode(200);
        response.then().assertThat().body("orders",notNullValue());
        System.out.println(response.getBody().asString());

    }
}
