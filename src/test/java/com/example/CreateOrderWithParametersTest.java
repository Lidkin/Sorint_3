package com.example;

import com.example.order.OrderClient;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderWithParametersTest {

    final OrderClient posting = new OrderClient();

    @Parameterized.Parameter
    public String color;

    @Parameterized.Parameter(1)
    public int statusCode;

    @Parameterized.Parameters(name = "color = {0} | expected statusCode = {1}")
    public static Collection<Object[]> dataColor() {
        return Arrays.asList(new Object[][]{
                {"BLACK", 201},
                {"GRAY", 201},
                {"", 201},
                {"BLACK, GRAY", 201}
        });
    }

    @Description("do POST to /api/v1/orders/ and get statusCode 201: order created")
    @Test()
    public void checkCreateOrderWithColorTest() {
        Response response = posting.doPost(color);
        response.then().assertThat().statusCode(statusCode)
                .and()
                .body("track", notNullValue());
        System.out.println((Number) response.getBody().jsonPath().get("track"));
        System.out.println(color);
    }

}
