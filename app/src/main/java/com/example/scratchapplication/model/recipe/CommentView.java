package com.example.scratchapplication.model.recipe;

public class CommentView {
    private String avatar;
    private String name;
    private String comment;

    public CommentView() {
    }

    public CommentView(String avatar, String name, String comment) {
        this.avatar = avatar;
        this.name = name;
        this.comment = comment;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
