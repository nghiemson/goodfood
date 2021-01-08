package com.example.scratchapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.R;
import com.example.scratchapplication.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private List<Comment> listComments;
    private Context context;

    public CommentAdapter(List<Comment> listComments,Context context){
        this.listComments = listComments;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comment comment = listComments.get(position);
        holder.textViewName.setText(comment.getName());
        holder.textViewComment.setText(comment.getComment());
        Picasso.with(context).load(comment.getAvatar()).into(holder.imageViewAvatar);
    }

    @Override
    public int getItemCount() {
        return listComments.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageViewAvatar;
        TextView textViewName;
        TextView textViewComment;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.image_avatar_comment);
            textViewName = itemView.findViewById(R.id.txt_profile_name_comment);
            textViewComment = itemView.findViewById(R.id.txt_comment);
        }
    }
}
