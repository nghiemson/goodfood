package com.example.scratchapplication.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class DataConverter implements Serializable {

    @TypeConverter
    public String fromListString(List<String> listString){
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){
        }.getType();
        String json = gson.toJson(listString,type);
        return json;
    }

    @TypeConverter
    public List<String> toIngredients(String string){
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> listString = gson.fromJson(string,type);
        return listString;
    }
}
