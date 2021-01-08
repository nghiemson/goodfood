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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListTextAdapter extends RecyclerView.Adapter<ListTextAdapter.MyViewHolder> {
    List<String> listString;
    Context context;
    private List<Integer> drawableStepList;
    public ListTextAdapter(List<String> listString, Context context){
        this.listString = listString;
        this.context = context;
        drawableStepList = new ArrayList<>();
        drawableStepList.add(R.drawable.ic_1);
        drawableStepList.add(R.drawable.ic_2);
        drawableStepList.add(R.drawable.ic_3);
        drawableStepList.add(R.drawable.ic_4);
        drawableStepList.add(R.drawable.ic_5);
        drawableStepList.add(R.drawable.ic_6);
        drawableStepList.add(R.drawable.ic_7);
        drawableStepList.add(R.drawable.ic_8);
        drawableStepList.add(R.drawable.ic_9);
        drawableStepList.add(R.drawable.ic_10);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_text,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.with(context).load(drawableStepList.get(position)).into(holder.imageViewStep);
        holder.textView.setText(listString.get(position));
    }

    @Override
    public int getItemCount() {
        return listString.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewStep,imageViewEdit;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewStep = itemView.findViewById(R.id.image_step_ingredient);
            imageViewEdit = itemView.findViewById(R.id.image_edit_ingredients_rv);
            imageViewEdit.setVisibility(View.INVISIBLE);
            textView = itemView.findViewById(R.id.txt_ingredient);
        }
    }
}
