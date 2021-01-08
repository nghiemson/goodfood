package com.example.scratchapplication.api;

import com.example.scratchapplication.fragment.Follow;
import com.example.scratchapplication.model.Comment;
import com.example.scratchapplication.model.ListRecipes;
import com.example.scratchapplication.model.Like;
import com.example.scratchapplication.model.ListUsers;
import com.example.scratchapplication.model.Message;
import com.example.scratchapplication.model.Profile;
import com.example.scratchapplication.model.ProfilePojo;
import com.example.scratchapplication.model.RecipePojo;
import com.example.scratchapplication.model.Save;
import com.example.scratchapplication.model.UpdatePojo;
import com.example.scratchapplication.model.home.ModelRecipe;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonApi {

    @GET("home/recipes")
    Call<ListRecipes> getAllRecipes();
    @GET("profile/user")
    Call<ListUsers> getAllUsers();
    @POST("home/recipes")
    Call<ModelRecipe> postRecipe(@Body ModelRecipe modelRecipe);
    @POST("home/recipes/like")
    Call<Like> postLike(@Body Like like);
    @POST("home/recipes/save")
    Call<Save> saveRecipe(@Body Save save);
    @GET("profile/info")
    Call<ProfilePojo> getProfile(@Query("uId")String uid);
    @POST("home/recipes/detail")
    Call<RecipePojo> getRecipeDetail(@Body Rid rid);
    @GET("profile/recipes")
    Call<ListRecipes> getProfileRecipes(@Query("uId")String uid);
    @POST("home/recipes/follow")
    Call<Follow> postFollow(@Body Follow follow);
    @POST("home/recipes/comment")
    Call<Comment> postComment(@Body Comment comment);
    @POST("profile/user/add")
    Call<Profile> addProfile(@Body Profile profile);
    @POST("chat/select_user")
    Call<MessagesPojo> chat(@Body JsonObject jsonObject);
    @POST("profile/update_info")
    Call<UpdatePojo> updateProfile(@Body UpdatePojo profile);

    class Rid {
        @SerializedName("rId")
        @Expose
        private String rId;

        public Rid(String rId) {
            this.rId = rId;
        }
    }
    class ChatPojo{
        @SerializedName("uId")
        @Expose
        private String uid;
        @SerializedName("uIdReceive")
        @Expose
        private String uidReceive;

        public ChatPojo(String uid, String uidReceive) {
            this.uid = uid;
            this.uidReceive = uidReceive;
        }
    }
    class MessagesPojo{
        @SerializedName("data")
        @Expose
        private DataMessages dataMessages;

        public DataMessages getDataMessages() {
            return dataMessages;
        }
    }
    class DataMessages{
        @SerializedName("messages")
        @Expose
        private List<Message> messages;
        @SerializedName("idMessage")
        @Expose
        private String idMessage;

        public List<Message> getMessages() {
            return messages;
        }

        public String getIdMessage() {
            return idMessage;
        }
    }

}
