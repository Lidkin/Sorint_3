package com.example.courier;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierId {

    String login;
    String password;

    public CourierId(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public int getCourierId() {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body("{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login");
        String id;
        if (response.getBody().jsonPath().get(("id")) == null) {
            return 0;
        } else {
            id = response.getBody().jsonPath().get("id").toString();
        }
        return Integer.parseInt(id);
    }
}
