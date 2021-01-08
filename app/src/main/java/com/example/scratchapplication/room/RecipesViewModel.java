package com.example.scratchapplication.room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.scratchapplication.api.WebServiceRepository;
import com.example.scratchapplication.model.Profile;
import com.example.scratchapplication.model.home.ModelRecipe;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    private RecipeRoomDBRepository recipeRoomDBRepository;
    private LiveData<List<ModelRecipe>> mAllRecipes;
    private LiveData<ModelRecipe> mRecipe;
    private WebServiceRepository webServiceRepository;
    private LiveData<List<ModelRecipe>> retroObservable;
    public RecipesViewModel(Application application){
        super(application);
        recipeRoomDBRepository = new RecipeRoomDBRepository(application);
        webServiceRepository = new WebServiceRepository(application);
        mAllRecipes = recipeRoomDBRepository.getAllRecipes();
        retroObservable = webServiceRepository.providesWebservice();
    }
    public LiveData<List<ModelRecipe>> getAllRecipes(){
        return mAllRecipes;
    }
    public LiveData<ModelRecipe> getRecipeById(String rId){
        mRecipe = recipeRoomDBRepository.getRecipeById(rId);
        return mRecipe;
    }
}
