package com.example.goodfood.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodfood.OtherProfileActivity;
import com.example.goodfood.R;
import com.example.goodfood.ViewRecipeActivity;
import com.example.goodfood.model.recipe.RecipeCreate;
import com.example.goodfood.room.RecipeModel;
import com.example.goodfood.sql.DatabaseHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeedStringAdapter extends RecyclerView.Adapter<FeedStringAdapter.MyViewHolder> {
    List<String> keys;
    Context mContext;
    public FeedStringAdapter(List<String> keys,Context context){
        this.keys = keys;
        this.mContext = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_feed,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recipes");
        databaseReference.child(keys.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    final RecipeCreate recipe = snapshot.getValue(RecipeCreate.class);
                    Picasso.with(mContext).load(recipe.getProfileAvatar()).into(holder.imageViewAvatar);
                    Picasso.with(mContext).load(recipe.getUrlCover()).into(holder.imageViewCover);
                    holder.textViewProfileName.setText(recipe.getProfileName());
                    holder.textViewRecipeName.setText(recipe.getName());
                    holder.textViewRecipeDesc.setText(recipe.getDescription());
                    holder.layoutTittle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, OtherProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("UID", recipe.getpId());
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    });

                    DatabaseHandler databaseHandler = new DatabaseHandler(mContext);
                    List<String> listId = new ArrayList<>();
                    listId = databaseHandler.getListId();
                    if (listId.contains(keys.get(position))){
                        holder.buttonSave.setText("Saved");
                    }
                    else {
                        holder.buttonSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(mContext)
                                        .setTitle("Save")
                                        .setMessage("Save to cookbook?")
                                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                DatabaseHandler databaseHandler = new DatabaseHandler(mContext);
                                                RecipeModel recipeModel =
                                                        new RecipeModel(
                                                                keys.get(position),
                                                                recipe.getName(),
                                                                recipe.getDescription());
                                                databaseHandler.addRecipe(recipeModel);
                                                databaseHandler.addListId(keys.get(position));
                                                for (String ingredient : recipe.getIngredients()) {
                                                    databaseHandler.addIngredients(keys.get(position), ingredient);
                                                }
                                                for (String direction : recipe.getDirections()) {
                                                    databaseHandler.addDirections(keys.get(position), direction);
                                                }
                                                holder.buttonSave.setText("Saved");
                                                holder.buttonSave.setClickable(false);
                                                Toast.makeText(mContext, "Saved " + recipeModel.getRecipeName(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return keys.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutTittle;
        private ImageView imageViewAvatar, imageViewCover, imageViewLike;
        private TextView textViewProfileName, textViewRecipeName, textViewRecipeDesc, textViewLikeCount, textViewCmtCount;
        private Button buttonSave;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewAvatar = itemView.findViewById(R.id.image_avatar);
            imageViewCover = itemView.findViewById(R.id.image_cover);
            imageViewCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ViewRecipeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("RID", keys.get(getAdapterPosition()));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
            imageViewLike = itemView.findViewById(R.id.image_like);

            textViewProfileName = itemView.findViewById(R.id.txt_profile_name);
            textViewRecipeName = itemView.findViewById(R.id.txt_recipe_name);
            textViewRecipeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ViewRecipeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("RID", keys.get(getAdapterPosition()));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
            textViewRecipeDesc = itemView.findViewById(R.id.txt_recipe_desc);
            textViewLikeCount = itemView.findViewById(R.id.txt_like_count);
            textViewCmtCount = itemView.findViewById(R.id.txt_cmt_count);
            buttonSave = itemView.findViewById(R.id.btn_save);

            textViewProfileName = itemView.findViewById(R.id.txt_profile_name);
            layoutTittle = itemView.findViewById(R.id.title_bar);

        }
    }
}
