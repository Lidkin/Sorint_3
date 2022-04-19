package com.example.order;

public class Order {

   String firstName;
   String lastName;
   String address;
   String metroStation;
   String phone;
   Number rentTime;
   String deliveryDate;
   String comment;
   String[] color;

   public Order(String firstName, String lastName, String address, String metroStation, String phone, Number rentTime, String deliveryDate, String comment, String[] color) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
      this.metroStation = metroStation;
      this.phone = phone;
      this.rentTime = rentTime;
      this.deliveryDate = deliveryDate;
      this.comment = comment;
      this.color = color;

   }

   public Object setColor(String[] color) {
      this.color = color;
      return this;
   }
}
