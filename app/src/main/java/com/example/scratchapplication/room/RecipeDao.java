package com.example.scratchapplication.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.scratchapplication.model.home.ModelRecipe;

import java.util.List;

@Dao
public interface RecipeDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(ModelRecipe recipe);

    @Query("SELECT * from recipes")
    LiveData<List<ModelRecipe>> getAllRecipes();

    @Query("SELECT * from recipes where rId = :rId")
    LiveData<ModelRecipe> getRecipeByRid(String rId);

    @Query("DELETE FROM recipes")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(List<ModelRecipe> recipes);
}
