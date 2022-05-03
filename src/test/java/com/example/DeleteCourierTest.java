package com.example;

import com.example.courier.CourierClient;
import com.example.courier.CredentialsCourier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;

public class DeleteCourierTest {

    private final CredentialsCourier credentials = new CredentialsCourier();
    final CourierClient deleting = new CourierClient();
    private final String login = "\"login\":\"" + credentials.getCredentials().get(0);
    private final String password = "\",\"password\":\"" + credentials.getCredentials().get(1);
    private final String body = "{" + login + password + "\"}";
    private int statusCode;

    @Description("do DELETE request to /api/v1/courier/ with expected statusCode 200")
    @DisplayName("positive test")
    @Test
    public void deleteCourierPositiveTest() {
        statusCode = 200;
        Assert.assertEquals("true",deleting.courierConditionalDelete(statusCode, body));
    }

    @Description("do DELETE request to /api/v1/courier/ with expected statusCode 400")
    @DisplayName("bad request test")
    @Test
    public void deleteCourierBadRequestTest() {
        statusCode = 400;
        Assert.assertEquals("HTTP/1.1 400 Bad Request",deleting.courierConditionalDelete(statusCode, body));
    }

    @Description("do DELETE request to /api/v1/courier/ with expected statusCode 404")
    @DisplayName("not found test")
    @Test
    public void deleteCourierNotFoundTest() {
        statusCode = 404;
        Assert.assertEquals("Курьера с таким id нет.",deleting.courierConditionalDelete(statusCode, body));
    }

}

