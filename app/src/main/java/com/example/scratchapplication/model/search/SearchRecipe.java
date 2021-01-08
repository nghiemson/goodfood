package com.example.scratchapplication.model.search;

public class SearchRecipe {
    private String rId;
    private String cover;
    private String name;

    public SearchRecipe() {
    }

    public SearchRecipe(String rId, String cover, String name) {
        this.rId = rId;
        this.cover = cover;
        this.name = name;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
