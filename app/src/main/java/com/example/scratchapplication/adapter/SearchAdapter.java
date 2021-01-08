package com.example.scratchapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.R;
import com.example.scratchapplication.model.search.SearchRecipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context mContext;
    List<SearchRecipe> mList;
    public SearchAdapter(List<SearchRecipe> list,Context context){
        mContext=context;
        mList=list;
    }
    @NonNull
    @Override
    //inflate item search cho view
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchRecipe searchRecipe = mList.get(position);
        Picasso.with(mContext)
                .load(searchRecipe.getCover())
                .into(holder.imageViewCover);
        holder.textViewRecipeName.setText(searchRecipe.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewCover;
        private TextView textViewRecipeName;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_cover_search);
            textViewRecipeName = itemView.findViewById(R.id.txt_recipe_name_search);
        }
    }
}
