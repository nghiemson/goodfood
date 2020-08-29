package com.example.goodfood.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipesDao {
    @Query("SELECT * FROM recipes")
    List<DirectionsModel> getAll();
    @Query("SELECT * FROM recipes WHERE rid = :rid LIMIT 1")
    RecipeModel loadByRid(String rid);

    @Query("DELETE FROM recipes")
    void deleteAll();

    @Insert
    void insertAll(DirectionsModel... directionsModels);

    @Delete
    void delete(DirectionsModel directionsModel);
}
