package com.example;

import com.example.courier.CourierClient;
import com.example.courier.CourierId;
import com.example.courier.CredentialsCourier;
import com.example.order.OrderClient;
import com.example.order.OrderTrackAndId;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AcceptOrderWithParametersTest {

    private static final CredentialsCourier credentials = new CredentialsCourier();
    private static final CourierClient courierClient = new CourierClient();
    static String login = credentials.getCredentials().get(0), password = credentials.getCredentials().get(1);
    static int courierId = new CourierId(login, password).getCourierId();
    static int orderId = new OrderTrackAndId().getOrderId();
    OrderClient receipt = new OrderClient();

    @Parameterized.Parameter
    public int order;

    @Parameterized.Parameter(1)
    public int courier;

    @Parameterized.Parameter(2)
    public int statusCode;

    @Parameterized.Parameter(3)
    public String status;

    @Parameterized.Parameter(4)
    public String path;

    @Parameterized.Parameters(name = "orderId = {0}, courierId = {1} | {2}: {3}")
    public static Collection<Object[]> dataParameters() {
        return Arrays.asList(new Object[][]{
                {orderId, courierId, 200, "true", "ok"},
                {0, courierId, 400, "Недостаточно данных для поиска", "message"},
                {orderId, 0, 400, "Недостаточно данных для поиска", "message"},
                {11111111, courierId, 404, "Заказа с таким id не существует", "message"},
                {orderId, 1, 404, "Курьера с таким id не существует", "message"}
        });
    }

    @AfterClass
    public static void cleanUp() {
        courierClient.doDelete(courierId);
    }

    @Description("test with different variants of the login/password pair")
    @Test
    public void acceptOrderTest() {
        Response response;
        if (statusCode == 400) {
            response = receipt.doPut(order,courier);
        } else {
            response = receipt.doPutWithParam(order,courier);
        }
        response.then().assertThat().statusCode(statusCode);
        Assert.assertEquals(status, response.getBody().path(path).toString());
    }

}
