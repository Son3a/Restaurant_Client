package com.nsb.restaurant.model;

import java.io.Serializable;

public class BookingModel implements Serializable {
    String idBooking, timeBooking, numOfPeople, status;
    String createdTime, nameClient, emailClient, phoneNumClient, tableName;

    public BookingModel() {
    }

    public BookingModel(String idBooking, String timeBooking, String numOfPeople, String status) {
        this.idBooking = idBooking;
        this.timeBooking = timeBooking;
        this.numOfPeople = numOfPeople;
        this.status = status;
    }

    public BookingModel(String idBooking, String timeBooking, String numOfPeople, String status, String createdTime, String nameClient, String emailClient, String phoneNumClient, String tableName) {
        this.idBooking = idBooking;
        this.timeBooking = timeBooking;
        this.numOfPeople = numOfPeople;
        this.status = status;
        this.createdTime = createdTime;
        this.nameClient = nameClient;
        this.emailClient = emailClient;
        this.phoneNumClient = phoneNumClient;
        this.tableName = tableName;
    }

    public BookingModel(String timeBooking, String numOfPeople, String status, String createdTime, String nameClient, String emailClient, String phoneNumClient, String tableName) {
        this.timeBooking = timeBooking;
        this.numOfPeople = numOfPeople;
        this.status = status;
        this.createdTime = createdTime;
        this.nameClient = nameClient;
        this.emailClient = emailClient;
        this.phoneNumClient = phoneNumClient;
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getPhoneNumClient() {
        return phoneNumClient;
    }

    public void setPhoneNumClient(String phoneNumClient) {
        this.phoneNumClient = phoneNumClient;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getTimeBooking() {
        return timeBooking;
    }

    public void setTimeBooking(String timeBooking) {
        this.timeBooking = timeBooking;
    }

    public String getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(String numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
