package com.example;

import com.example.courier.Courier;
import com.example.courier.CourierClient;
import com.example.courier.CourierId;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

public class CreateCourierTest {

    private final String courierLogin = RandomStringUtils.randomAlphabetic(10);
    private final String courierPassword = RandomStringUtils.randomAlphabetic(10);
    private final String courierFirstName = RandomStringUtils.randomAlphabetic(10);
    final CourierId id = new CourierId(getLoginPass().get(0), getLoginPass().get(1));
    final CourierClient posting = new CourierClient();
    final CourierClient courierClient = new CourierClient();

    @NotNull
    private ArrayList<String> getLoginPass() {
        ArrayList<String> loginPass = new ArrayList<>();
        loginPass.add(courierLogin);
        loginPass.add(courierPassword);
        return loginPass;
    }

    private final String body = "{\"login\":\"" + getLoginPass().get(0) + "\","
            + "\"password\":\"" + getLoginPass().get(1) + "\"}";

    Courier courierBody = new Courier(
            courierLogin,
            courierPassword,
            courierFirstName
    );

    @After
    public void cleanUp() {
        if (id.getCourierId() != 0)
            courierClient.doDelete(id.getCourierId());
    }

    @Description("code: 201. ok: true.")
    @DisplayName("register courier: positive flow")
    @Test
    public void registerCourierPositiveFlow() {
        Response response = posting.doPost(body);
        response.then().assertThat().statusCode(201);
        assertEquals("true", response.getBody().path("ok").toString());
    }

    @Description("code: 400, message: Недостаточно данных для создания учетной записи")
    @DisplayName("register courier: login empty")
    @Test
    public void registerCourierLoginEmpty() {
        Response response = posting.doPost(courierBody.setLogin(null));
        response.then().assertThat().statusCode(400);
        assertEquals("Недостаточно данных для создания учетной записи", response.getBody().path("message").toString());
    }

    @Description("code: 400, message: Недостаточно данных для создания учетной записи")
    @DisplayName("register courier: password empty")
    @Test
    public void registerCourierPasswordEmpty() {
        Response response = posting.doPost(courierBody.setPassword(null));
        response.then().assertThat().statusCode(400);
        assertEquals("Недостаточно данных для создания учетной записи", response.getBody().path("message").toString());
    }

    @Description("code: 409, message: Этот логин уже используется. Попробуйте другой.")
    @DisplayName("register courier: recurring login")
    @Test
    public void registerCourierRecurringLogin() {
        posting.doPost(body);
        Response response = posting.doPost(courierBody);
        response.then().assertThat().statusCode(409);
        assertEquals("Этот логин уже используется. Попробуйте другой.", response.getBody().path("message").toString());
    }

}

