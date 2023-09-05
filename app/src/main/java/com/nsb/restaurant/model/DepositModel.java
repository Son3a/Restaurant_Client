package com.nsb.restaurant.model;

import java.io.Serializable;

public class DepositModel implements Serializable {
    String idDeposit, moneyDeposit, timeDeposit, idBooking, idTable, typePay;

    public DepositModel() {
    }

    public DepositModel(String idDeposit, String moneyDeposit, String timeDeposit, String idBooking, String idTable, String typePay) {
        this.idDeposit = idDeposit;
        this.moneyDeposit = moneyDeposit;
        this.timeDeposit = timeDeposit;
        this.idBooking = idBooking;
        this.idTable = idTable;
        this.typePay = typePay;
    }

    public String getTypePay() {
        return typePay;
    }

    public void setTypePay(String typePay) {
        this.typePay = typePay;
    }

    public String getIdDeposit() {
        return idDeposit;
    }

    public void setIdDeposit(String idDeposit) {
        this.idDeposit = idDeposit;
    }

    public String getMoneyDeposit() {
        return moneyDeposit;
    }

    public void setMoneyDeposit(String moneyDeposit) {
        this.moneyDeposit = moneyDeposit;
    }

    public String getTimeDeposit() {
        return timeDeposit;
    }

    public void setTimeDeposit(String timeDeposit) {
        this.timeDeposit = timeDeposit;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getIdTable() {
        return idTable;
    }

    public void setIdTable(String idTable) {
        this.idTable = idTable;
    }
}
