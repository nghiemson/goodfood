package com.example.scratchapplication.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.scratchapplication.model.Profile;
import com.example.scratchapplication.model.home.ModelRecipe;

@Database(entities = {ModelRecipe.class,Profile.class},version = 2)
public abstract class RecipeRoomDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    public abstract ProfileDao profileDao();

    private static RecipeRoomDatabase INSTANCE;

    public static synchronized RecipeRoomDatabase getDatabase(Context context){
        if (INSTANCE == null){
            synchronized (RecipeRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),RecipeRoomDatabase.class,"recipe_database")
                                                    .fallbackToDestructiveMigration()
                                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
