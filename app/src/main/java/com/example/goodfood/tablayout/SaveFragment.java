package com.example.goodfood.tablayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodfood.R;
import com.example.goodfood.RecipeSqlActivity;
import com.example.goodfood.room.RecipeModel;
import com.example.goodfood.sql.DatabaseHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveFragment extends Fragment implements Serializable{

    private RecyclerView recyclerView;
    private List<RecipeModel> recipes;

    public SaveFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        DatabaseHandler handler = new DatabaseHandler(getContext());
        recipes = new ArrayList<>();
        recipes = handler.getAllRecipes();
        SaveAdapter saveAdapter = new SaveAdapter();
        recyclerView = view.findViewById(R.id.rv_save);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(saveAdapter);
        return view;
    }
    class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_save,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final RecipeModel recipe = recipes.get(position);

            holder.textViewName.setText(recipe.getRecipeName());
            holder.textViewDesc.setText(recipe.getRecipeDesc());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler data = new DatabaseHandler(getContext());

                    List<String> directions = new ArrayList<>();
                    directions = data.getDirectionsList(recipe.getRid());

                    List<String> ingredients = new ArrayList<>();
                    ingredients = data.getIngredientsList(recipe.getRid());

                    Intent intent = new Intent(getContext(), RecipeSqlActivity.class);
                    intent.putExtra("NAME",recipe.getRecipeName());
                    intent.putExtra("DESC",recipe.getRecipeDesc());
                    intent.putExtra("LIST_INGREDIENTS", (Serializable) ingredients);
                    intent.putExtra("LIST_DIRECTIONS",(Serializable) directions);
                    getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return recipes.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textViewName,textViewDesc;
            LinearLayout layout;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.layout_save);
                textViewName = itemView.findViewById(R.id.save_name);
                textViewDesc = itemView.findViewById(R.id.save_desc);
            }
        }
    }
}

