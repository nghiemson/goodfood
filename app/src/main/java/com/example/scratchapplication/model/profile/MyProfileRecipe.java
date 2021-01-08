package com.example.scratchapplication.model.profile;

public class MyProfileRecipe {
    private String rId;
    private int imageRecipe;
    private String nameRecipe;



    public MyProfileRecipe( String rId, int imageRecipe, String nameRecipe) {

        this.rId = rId;
        this.imageRecipe = imageRecipe;
        this.nameRecipe = nameRecipe;

    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public int getImageRecipe() {
        return imageRecipe;
    }

    public String getNameRecipe() {
        return nameRecipe;
    }

    public void setImageRecipe(int imageRecipe) {
        this.imageRecipe = imageRecipe;
    }

    public void setNameRecipe(String nameRecipe) {
        this.nameRecipe = nameRecipe;
    }
}