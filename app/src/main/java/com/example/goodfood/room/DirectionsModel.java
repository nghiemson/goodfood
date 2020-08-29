package com.example.goodfood.room;


public class DirectionsModel {
    private int id;
    private String rid;
    private String direction;

    public DirectionsModel() {
    }

    public DirectionsModel(int id, String rid, String direction) {
        this.id = id;
        this.rid = rid;
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
