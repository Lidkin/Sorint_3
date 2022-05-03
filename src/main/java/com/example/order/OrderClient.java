package com.example.order;

import com.example.BaseHttp;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class OrderClient extends BaseHttp {

    private final String baseURI = "https://qa-scooter.praktikum-services.ru";
    private final String apiOrder = "/api/v1/orders/";
    final BodyOrder order = new BodyOrder();


    @Step("do GET request by track number {track} to /api/v1/orders/track for the getting order data if it's possible or error message if not.")
    public Response doGetByTrack(Object track){
        return doGetRequestWithParam(baseURI + apiOrder + "track", track);
    }

    @Step("do GET request to /api/v1/orders/ for the entire list of orders without a courier id")
    public Response doGet(){
        return doGetRequest(baseURI + apiOrder);
    }

    @Step("set color: {color}  in order body, get POST request to /api/v1/orders/ and getting response")
    public Response doPost(String color) {
        return doPostRequest(baseURI + apiOrder, order.getOrderBody().setColor(new String[]{color}));
    }

    @Step("send request PUT to /api/v1/orders/accept/ with courierId {courier} and orderId {order}")
    public Response doPutWithParam(int order, int courier) {
        return doPutRequestWithParam(baseURI + apiOrder + "accept/" + order, courier);
    }

    @Step("do PUT request to /api/v1/orders/accept/ depending on one of the empty parameters (courierId = {courier}, orderId = {order})")
    public Response doPut(int order, int courier) {
        Response response;
        if (order == 0) {
            response = doPutRequest(baseURI + apiOrder + "accept/" + courier);
        } else {
            response = doPutRequest(baseURI + apiOrder + "accept/" + order);
        }
        return response;
    }

}
