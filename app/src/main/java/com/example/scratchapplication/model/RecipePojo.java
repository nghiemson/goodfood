package com.example.scratchapplication.model;

import com.example.scratchapplication.model.home.ModelRecipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipePojo {
    @SerializedName("data")
    @Expose
    private ModelRecipe modelRecipe;

    public ModelRecipe getModelRecipe() {
        return modelRecipe;
    }
}
