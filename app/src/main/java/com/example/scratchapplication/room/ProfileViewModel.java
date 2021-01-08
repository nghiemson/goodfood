package com.example.scratchapplication.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.scratchapplication.api.ProfileServiceRepository;
import com.example.scratchapplication.model.Profile;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    private RecipeRoomDBRepository recipeRoomDBRepository;
    private ProfileServiceRepository profileServiceRepository;
    private LiveData<Profile> mProfile;
    private LiveData<List<Profile>> observable,mUsers;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        recipeRoomDBRepository = new RecipeRoomDBRepository(application);
        profileServiceRepository = new ProfileServiceRepository(application);
        observable = profileServiceRepository.profileServiceAllUser();
    }
    public LiveData<List<Profile>> getAllUsers(){
        mUsers = recipeRoomDBRepository.getAllUsers();
        return mUsers;
    }
    public LiveData<Profile> getProfileById(String id){
        profileServiceRepository.providesProfileService(id);
        mProfile = recipeRoomDBRepository.getProfileById(id);
        return mProfile;
    }
    public LiveData<Profile> getProfileAdapter(String id){
        mProfile = recipeRoomDBRepository.getProfileById(id);
        return mProfile;
    }
}
