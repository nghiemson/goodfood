package com.example.goodfood.room;


public class IngredientsModel {

    private int id;
    private String rid;
    private   String ingredient;

    public IngredientsModel() {
    }

    public IngredientsModel(int id, String rid, String ingredient) {
        this.id = id;
        this.rid = rid;
        this.ingredient = ingredient;
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

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
