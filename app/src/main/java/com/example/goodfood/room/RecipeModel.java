package com.example.goodfood.room;


public class RecipeModel {
    private String rid;

    private String recipeName;

    private  String recipeDesc;

    public RecipeModel() {
    }

    public RecipeModel(String rid, String recipeName, String recipeDesc) {
        this.rid = rid;
        this.recipeName = recipeName;
        this.recipeDesc = recipeDesc;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDesc() {
        return recipeDesc;
    }

    public void setRecipeDesc(String recipeDesc) {
        this.recipeDesc = recipeDesc;
    }
}
