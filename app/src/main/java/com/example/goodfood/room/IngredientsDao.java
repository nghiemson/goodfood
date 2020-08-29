package com.example.goodfood.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IngredientsDao {
    @Query("SELECT * FROM ingredients")
    List<DirectionsModel> getAll();
    @Query("SELECT * FROM ingredients WHERE rid = :rid")
    List<DirectionsModel> loadAllByRid(String rid);
    @Query("DELETE FROM ingredients")
    void deleteAll();

    @Insert
    void insertAll(IngredientsModel... ingredientsModels);

    @Delete
    void delete(IngredientsModel ingredientsModel);
}
