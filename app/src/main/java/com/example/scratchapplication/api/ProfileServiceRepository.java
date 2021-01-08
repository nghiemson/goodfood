package com.example.scratchapplication.api;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.scratchapplication.model.ListUsers;
import com.example.scratchapplication.model.Profile;
import com.example.scratchapplication.model.ProfilePojo;
import com.example.scratchapplication.room.RecipeRoomDBRepository;
import com.example.scratchapplication.room.RecipeRoomDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileServiceRepository {
    Application application;
    Profile profile;

    public ProfileServiceRepository(Application application){
        this.application = application;
    }
    public LiveData<List<Profile>> profileServiceAllUser(){
        MutableLiveData<List<Profile>> data = new MutableLiveData<>();
        try {
            JsonApi service = RestClient.createService(JsonApi.class);
            RecipeRoomDBRepository recipeRoomDBRepository = new RecipeRoomDBRepository(application);
            service.getAllUsers().enqueue(new Callback<ListUsers>() {
                @Override
                public void onResponse(Call<ListUsers> call, Response<ListUsers> response) {
                    if (response.isSuccessful()){
                        List<Profile> profiles = response.body().getProfiles();
                        data.setValue(profiles);
                        if (profiles.size()>0)
                            recipeRoomDBRepository.insertAllUsers(profiles);
                        Log.e("profile", profiles.size()+"");
                    }
                }

                @Override
                public void onFailure(Call<ListUsers> call, Throwable t) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public MutableLiveData<Profile> providesProfileService(String uid){
        MutableLiveData<Profile> data = new MutableLiveData<>();
        try{
            JsonApi service = RestClient.createService(JsonApi.class);
            RecipeRoomDBRepository roomDBRepository = new RecipeRoomDBRepository(application);
            service.getProfile(uid).enqueue(new Callback<ProfilePojo>() {
                @Override
                public void onResponse(Call<ProfilePojo> call, Response<ProfilePojo> response) {
                    if (!response.isSuccessful()){
                        Log.e("code profile",response.code()+"");
                        return;
                    }
                    profile = response.body().getProfile();
                    Log.e("profileFollows",profile.getFollows().size()+"");
                    data.setValue(profile);
                    roomDBRepository.insertProfile(profile);
                }
                @Override
                public void onFailure(Call<ProfilePojo> call, Throwable t) {
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

}
