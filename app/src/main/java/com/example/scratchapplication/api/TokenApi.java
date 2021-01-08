package com.example.scratchapplication.api;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TokenApi {
    @POST("profile/fcmToken")
    Call<TokenSend> sendToken(@Body TokenSend tokenSend);
    @POST("profile/logout/fcmToken")
    Call<JsonObject> deleteToken(@Body JsonObject jsonObject);

    class TokenSend{
        @SerializedName("uId")
        @Expose
        private String uid;
        @SerializedName("fcmToken")
        @Expose
        private String token;

        public String getUid() {
            return uid;
        }

        public String getToken() {
            return token;
        }

        public TokenSend(String uid, String token) {
            this.uid = uid;
            this.token = token;
        }
    }
}
