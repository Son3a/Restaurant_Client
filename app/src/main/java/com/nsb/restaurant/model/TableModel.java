package com.nsb.restaurant.model;

public class TableModel {
    String id, name, numOfPeople, idRoom;

    public TableModel() {
    }

    public TableModel(String id, String name, String numOfPeople, String idRoom) {
        this.id = id;
        this.name = name;
        this.numOfPeople = numOfPeople;
        this.idRoom = idRoom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(String numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }
}
