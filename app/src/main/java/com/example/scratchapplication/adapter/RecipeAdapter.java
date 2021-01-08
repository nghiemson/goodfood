package com.example.scratchapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.R;
import com.example.scratchapplication.ViewRecipeActivity;
import com.example.scratchapplication.model.home.RecipeFeed;
import com.example.scratchapplication.model.profile.MyProfileRecipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{
    private FragmentManager fragmentManager;
    private List<MyProfileRecipe> mRecipeItems;

    Context mContext;
    public RecipeAdapter(FragmentManager fragmentManager, List<MyProfileRecipe> mRecipeItems, Context mContext) {
        this.fragmentManager = fragmentManager;
        this.mRecipeItems = mRecipeItems;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item,
                parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        MyProfileRecipe myProfileRecipe = mRecipeItems.get(position);
        holder.recipeImageView.setImageResource(myProfileRecipe.getImageRecipe());
        holder.recipeTextView.setText(myProfileRecipe.getNameRecipe());
    }

    @Override
    public int getItemCount() {
        return mRecipeItems.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView recipeImageView;
        TextView recipeTextView;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.img_my_recipe);
            recipeTextView = itemView.findViewById(R.id.tv_my_recipe);
            recipeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ViewRecipeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("RID",mRecipeItems.get(getAdapterPosition()).getrId());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
