package com.example.scratchapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeSqlActivity extends AppCompatActivity implements Serializable {
    TextView textViewName,textViewDesc;
    RecyclerView listViewIngredients,listViewDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_sql);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String desc = intent.getStringExtra("DESC");
        ArrayList<String> ingredients = (ArrayList<String>) intent.getSerializableExtra("LIST_INGREDIENTS");
        ArrayList<String> directions = (ArrayList<String>) intent.getSerializableExtra("LIST_DIRECTIONS");

        textViewName = findViewById(R.id.txt_recipe_name_sql);
        textViewName.setText(name);
        textViewDesc = findViewById(R.id.txt_recipe_desc_sql);
        textViewDesc.setText(desc);
        listViewIngredients = findViewById(R.id.list_ingredients);
        listViewIngredients.setAdapter(new SimpleAdapter(ingredients));
        listViewDirections = findViewById(R.id.list_directions);
        listViewDirections.setAdapter(new SimpleAdapter(directions));

    }
    class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.MyViewHolder> {
        List<String> list;
        public SimpleAdapter(List<String> list){
            this.list = list;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(RecipeSqlActivity.this).inflate(R.layout.simple_row,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String text = list.get(position);
            holder.textView.setText(position+1+".\t"+text);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.text_row);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}