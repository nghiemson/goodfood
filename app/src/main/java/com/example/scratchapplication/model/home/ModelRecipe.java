package com.example.scratchapplication.model.home;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.scratchapplication.model.Comment;
import com.example.scratchapplication.room.DataConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@Entity(tableName = "recipes")
public class ModelRecipe implements Comparator<ModelRecipe>, Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "rid")
    @SerializedName("rId")
    @Expose
    private String rid;
    @ColumnInfo(name ="uid")
    @NonNull
    @SerializedName("uId")
    @Expose
    private String uid;
    @ColumnInfo(name = "name")
    @NonNull
    @SerializedName("name")
    @Expose
    private String name;
    @ColumnInfo(name = "description")
    @NonNull
    @SerializedName("description")
    @Expose
    private String description;
    @ColumnInfo(name = "urlCover")
    @NonNull
    @SerializedName("urlCover")
    @Expose
    private String urlCover;
    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "ingredients")
    @NonNull
    @SerializedName("ingredients")
    @Expose
    private List<String> ingredients;
    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "directions")
    @NonNull
    @SerializedName("directions")
    @Expose
    private List<String> directions;
    @Ignore
    @SerializedName("like")
    @Expose
    private Integer like;
    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "usersLike")
    @NonNull
    @SerializedName("usersLike")
    @Expose
    private List<String> usersLike;
    @ColumnInfo(name = "profileAvatar")
    @SerializedName("profileAvatar")
    @Expose
    private String profileAvatar;
    @ColumnInfo(name = "profileName")
    @NonNull
    @SerializedName("profileName")
    @Expose
    private String profileName;
    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "filters")
    @SerializedName("filters")
    @Expose
    private List<String> filters;
    @Ignore
    @SerializedName("dataComment")
    @Expose
    private List<Comment> dataComment;

    public ModelRecipe() {
    }
    public ModelRecipe(String uId, String name, String description, String urlCover,
                       List<String> ingredients, List<String> directions, int like, List<String> usersLike,
                       String profileAvatar, String profileName, List<String> filters) {
        this.uid = uId;
        this.name = name;
        this.description = description;
        this.urlCover = urlCover;
        this.ingredients = ingredients;
        this.directions = directions;
        this.like = like;
        this.usersLike = usersLike;
        this.profileAvatar = profileAvatar;
        this.profileName = profileName;
        this.filters = filters;
    }

    @NonNull
    public String getRid() {
        return rid;
    }

    public void setRid(@NonNull String rid) {
        this.rid = rid;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getUrlCover() {
        return urlCover;
    }

    public void setUrlCover(@NonNull String urlCover) {
        this.urlCover = urlCover;
    }

    @NonNull
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(@NonNull List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(@NonNull List<String> directions) {
        this.directions = directions;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    @NonNull
    public List<String> getUsersLike() {
        return usersLike;
    }

    public void setUsersLike(@NonNull List<String> usersLike) {
        this.usersLike = usersLike;
    }

    public String getProfileAvatar() {
        return profileAvatar;
    }

    public void setProfileAvatar(String profileAvatar) {
        this.profileAvatar = profileAvatar;
    }

    @NonNull
    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(@NonNull String profileName) {
        this.profileName = profileName;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public List<Comment> getDataComment() {
        return dataComment;
    }

    public void setDataComment(List<Comment> dataComment) {
        this.dataComment = dataComment;
    }

    @Override
    public int compare(ModelRecipe o1, ModelRecipe o2) {
        return o2.getUsersLike().size() - o1.getUsersLike().size();
    }
}
