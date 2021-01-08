package com.example.scratchapplication.model.recipe;

public class Additional {
    private String servingTime;
    private String nutrition;
    private String tags;

    public Additional() {
    }

    public Additional(String servingTime, String nutrition, String tags) {
        this.servingTime = servingTime;
        this.nutrition = nutrition;
        this.tags = tags;
    }

    public String getServingTime() {
        return servingTime;
    }

    public void setServingTime(String servingTime) {
        this.servingTime = servingTime;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
