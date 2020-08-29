package com.example.goodfood.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DirectionsDao {
    @Query("SELECT * FROM directions")
    List<DirectionsModel> getAll();
    @Query("SELECT * FROM directions WHERE rid = :rid")
    List<DirectionsModel> loadAllByRid(String rid);
    @Query("DELETE FROM directions")
    void deleteAll();

    @Insert
    void insertAll(DirectionsModel... directionsModels);

    @Delete
    void delete(DirectionsModel directionsModel);
}
