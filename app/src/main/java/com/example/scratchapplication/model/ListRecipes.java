package com.example.scratchapplication.model;

import com.example.scratchapplication.model.home.ModelRecipe;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListRecipes {
    @SerializedName("data")
    private List<ModelRecipe> data;

    public List<ModelRecipe> getData() {
        return data;
    }
}
