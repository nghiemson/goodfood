package com.example.goodfood.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.goodfood.room.RecipeModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipe";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_recipes_table =
                String.format("CREATE TABLE %s(%s TEXT PRIMARY KEY, %s TEXT, %s TEXT)",
                        "recipes", "id", "name", "desc");
        db.execSQL(create_recipes_table);
        String create_ingredients_table =
                String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT)",
                        "ingredients", "id", "rid", "ingredient");
        db.execSQL(create_ingredients_table);
        String create_directions_table =
                String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT)",
                        "directions", "id", "rid", "direction");
        db.execSQL(create_directions_table);
        String create_listid_table =
                String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT)",
                        "list", "id", "rid");
        db.execSQL(create_listid_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_recipes_table = String.format("DROP TABLE IF EXISTS %s", "recipes");
        db.execSQL(drop_recipes_table);
        String drop_ingredients_table = String.format("DROP TABLE IF EXISTS %s", "ingredients");
        db.execSQL(drop_ingredients_table);
        String drop_directions_table = String.format("DROP TABLE IF EXISTS %s", "directions");
        db.execSQL(drop_directions_table);
        String drop_list_table = String.format("DROP TABLE IF EXISTS %s", "list");
        db.execSQL(drop_list_table);
        onCreate(db);
    }
    public void addRecipe(RecipeModel recipeModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",recipeModel.getRid());
        values.put("name",recipeModel.getRecipeName());
        values.put("desc",recipeModel.getRecipeDesc());

        db.insert("recipes",null,values);
        db.close();
    }
    public void addIngredients(String rid, String ingredient){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("id",recipeModel.getRid());
        values.put("rid",rid);
        values.put("ingredient",ingredient);

        db.insert("ingredients",null,values);
        db.close();
    }
    public void addDirections(String rid, String direction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("rid",rid);
        values.put("direction",direction);

        db.insert("directions",null,values);
        db.close();
    }
    public List<RecipeModel> getAllRecipes(){
        List<RecipeModel> recipeModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM recipes",null);
        if (cursor!=null)
            cursor.moveToFirst();
        while (cursor.isAfterLast()==false) {
            RecipeModel recipe = new RecipeModel(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            recipeModels.add(recipe);
            cursor.moveToNext();
        }
        Log.e("SQL",recipeModels.size()+"");
        return recipeModels;
    }
    public List<String> getIngredientsList(String rid){
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ingredients WHERE rid = '"+rid+"'",null);
        if (cursor!=null)
            cursor.moveToFirst();
        while (cursor.isAfterLast()==false) {
            String ingredient = cursor.getString(2);
            list.add(ingredient);
            cursor.moveToNext();
        }
        return list;
    }
    public List<String> getDirectionsList(String rid){
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM directions WHERE rid = '"+rid+"'",null);
        if (cursor!=null)
            cursor.moveToFirst();
        while (cursor.isAfterLast()==false) {
            String direction = cursor.getString(2);
            list.add(direction);
            cursor.moveToNext();
        }
        return list;
    }
    public void addListId(String rid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("rid",rid);
        db.insert("list",null,values);
        db.close();
    }
    public List<String> getListId(){
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM list",null);
        if (cursor!=null)
            cursor.moveToFirst();
        while (cursor.isAfterLast()==false) {
            String rid = cursor.getString(1);
            list.add(rid);
            cursor.moveToNext();
        }
        return list;
    }
}
