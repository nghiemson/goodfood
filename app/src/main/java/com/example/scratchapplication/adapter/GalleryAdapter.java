package com.example.scratchapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.OnItemClickListener;
import com.example.scratchapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    List<Uri> uriList;
    Context context;
    private OnItemClickListener onItemClickListener;
    public void setOnClickItemListener(OnItemClickListener onClickItemListener){
        this.onItemClickListener = onClickItemListener;
    }
    public GalleryAdapter(List<Uri> uriList,Context context){
        this.uriList = uriList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add_gallery,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Uri uri = uriList.get(position);
        Picasso.with(context).load(uri).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item_gallery);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }
}
