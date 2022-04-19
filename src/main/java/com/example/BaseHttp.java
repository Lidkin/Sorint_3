package com.example;

import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BaseHttp {

    private final String JSON = "application/json";
    private final RestAssuredConfig config = RestAssuredConfig.newConfig()
            .sslConfig(new SSLConfig().relaxedHTTPSValidation())
            .redirect(new RedirectConfig().followRedirects(true));


    protected Response doGetRequest(String uri) {
        return given().config(config)
                .header("Content-Type", JSON)
                .get(uri);
    }

    protected Response doGetRequestWithParam(String uri, Object orderTrack) {
        return given().config(config)
                .header("Content-Type", JSON)
                .queryParam("t", orderTrack)
                .get(uri);
    }
    protected Response doPostRequest(String uri, Object body){
        return given().config(config)
                .header("Content-Type", JSON)
                .body(body)
                .post(uri);
    }

    protected Response doPutRequestWithParam(String uri, int courierId){
        return given().config(config)
                .header("Content-Type", JSON)
                .queryParam("courierId", courierId)
                .put(uri);
    }

    protected Response doPutRequest(String uri){
        return given().config(config)
                .header("Content-Type", JSON)
                .put(uri);
    }

    protected Response doDeleteRequest(String uri){
        return given().config(config)
                .header("Content-Type", JSON)
                .delete(uri);
    }

    protected Response doDeleteRequestWithBody(String uri){
        return given().config(config)
                .header("Content-Type", JSON)
                .body("{\"id:\"}")
                .delete(uri);
    }
}
