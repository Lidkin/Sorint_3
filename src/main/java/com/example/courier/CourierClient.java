package com.example.courier;

import com.example.BaseHttp;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class CourierClient extends BaseHttp {

    private final String baseURI = "https://qa-scooter.praktikum-services.ru";
    private final String apiCourier = "/api/v1/courier/";

    @Step("do POST to /api/v1/courier/")
    public Response doPost(Object body){
        return doPostRequest(baseURI + apiCourier, body);
    }

    @Step("do POST to /api/v1/courier/login with any credentials (login {login} and password {password}), expected status code {statusCode}")
    public Response doPostByLogin(int statusCode, String password, String login){
        String body = "";
        if (statusCode == 400) {
            if (login == null) {
                body = "{\"login\":\"" + "\",\"password\":\"" + password + "\"}";
            } else if (password == null) {
                body = "{\"login\":\"" + login + "\",\"password\":\"" + "\"}";
            }
        } else {
            body = "{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}";
        }
        return doPostRequest(baseURI + apiCourier + "login", body);
    }
    @Step("deleting data by id {id}")
    public void doDelete(int id){
        doDeleteRequest(baseURI + apiCourier + id);
    }

    @Step("creating a new courier to get his id")
    public String getCourierId(String body) {
        Response response = doPostRequest(baseURI + apiCourier + "login", body);
        response.then().assertThat().statusCode(200);
        return response.getBody().jsonPath().get("id").toString();
    }

    @Step("get a response to request DELETE depending on the statusCode {statusCode}")
    public String courierConditionalDelete(int statusCode, String body) {
        String status = "";
        if (statusCode == 400) {
            Response response = doDeleteRequestWithBody(baseURI + apiCourier);
            status = response.statusLine();
        } else if (statusCode == 404) {
            Response response = doDeleteRequest(baseURI + apiCourier + "123");
            status = response.getBody().path("message");
        } else if (statusCode == 200) {
            Response response = doDeleteRequest(baseURI + apiCourier + getCourierId(body));
            status = response.getBody().path("ok").toString();
        }
        return status;
    }

}
