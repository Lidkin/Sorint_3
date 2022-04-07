package com.example.order;

import org.apache.commons.lang3.RandomStringUtils;

public class BodyOrder {

    public Order getOrderBody() {
        return orderBody;
    }

    public String metroStation(){
        String metro = RandomStringUtils.randomNumeric(3);
        int i = Integer.parseInt(metro.trim());
        if (i >= 237) {
            i = 237;
        }
        return Integer.toString(i);
    }

    String phone = RandomStringUtils.randomNumeric(10);
    String app = RandomStringUtils.randomNumeric(2);
    String firstName = RandomStringUtils.randomAlphabetic(10);
    String lastName = RandomStringUtils.randomAlphabetic(10);
    String street = RandomStringUtils.randomAlphabetic(10);
    String[] colorList = {};
    String metroStation = metroStation();

    Order orderBody = new Order(
            firstName,
            lastName,
            street + " , " + app,
            metroStation,
            phone,
            5,
            "2022-06-13",
            " ",
            colorList
    );
}
