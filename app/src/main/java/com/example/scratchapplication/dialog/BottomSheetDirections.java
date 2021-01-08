package com.example.scratchapplication.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.OnItemClickListener;
import com.example.scratchapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class BottomSheetDirections extends BottomSheetDialogFragment {
    private Button buttonAddDirection;
    private Button buttonSaveDirections;
    private ImageView imageViewClose;
    private ImageView imageViewStep;
    private EditText editTextDirection;
    private RecyclerView recyclerViewDirections;
    private List<String> directionsList;
    private List<Integer> drawableStepList;

    private BottomSheetListener mListener;


    public interface BottomSheetListener{
        void onButtonClickedSaveDirections(List<String> list);
    }

    public BottomSheetDirections(){


        directionsList = new ArrayList<>();
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


    class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.MyViewHolder>{

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_directions,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String direction = directionsList.get(position);
            
            Picasso.with(getContext()).load(drawableStepList.get(position)).into(holder.imageViewStep);
            holder.textViewDirection.setText(direction);
        }

        @Override
        public int getItemCount() {
            return directionsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder  {

            private ImageView imageViewStep;
            private ImageView imageViewEdit;
            private TextView textViewDirection;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageViewStep = itemView.findViewById(R.id.image_step);
                imageViewEdit = itemView.findViewById(R.id.image_edit_direction_rv);
                textViewDirection = itemView.findViewById(R.id.txt_direction);

                imageViewEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener!=null){
                            int position = getAdapterPosition();
                            if (position!=RecyclerView.NO_POSITION){
                                onItemClickListener.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_directions,container,false);
        buttonAddDirection = v.findViewById(R.id.btn_add_direction_into_bts);
        editTextDirection = v.findViewById(R.id.et_direction);
//        buttonSaveDirections = v.findViewById(R.id.btn_save_directions);
//        buttonSaveDirections.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        imageViewClose = v.findViewById(R.id.image_close_dialog_directions);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        imageViewStep = v.findViewById(R.id.image_step_direction_addedit);
        Picasso.with(getContext()).load(drawableStepList.get(directionsList.size()));
        recyclerViewDirections = v.findViewById(R.id.rv_directions_in_dialog);
        final DirectionsAdapter directionsAdapter = new DirectionsAdapter();

        recyclerViewDirections.setAdapter(directionsAdapter);
        recyclerViewDirections.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDirections.setHasFixedSize(true);
        //onclick add
        final View.OnClickListener onClickListenerAdd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = directionsList.size();
                String direction = editTextDirection.getText().toString();
                if (!direction.equals("")&&position<9){
                    directionsList.add(direction);
                    directionsAdapter.notifyItemInserted(position);
                    editTextDirection.setText("");
                    recyclerViewDirections.scrollToPosition(position);
                    editTextDirection.setFocusable(true);
                    imageViewStep.setImageResource(drawableStepList.get(directionsList.size()));
                    Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        };
        buttonAddDirection.setOnClickListener(onClickListenerAdd);
        //item recyclerview click
        directionsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                imageViewStep.setImageResource(drawableStepList.get(position));
                editTextDirection.setText(directionsList.get(position));
                editTextDirection.setSelection(editTextDirection.getText().length());
                buttonAddDirection.setText("Edit");
                buttonAddDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //onclick edit
                        String direction = editTextDirection.getText().toString();
                        if (!direction.equals("")){
                            directionsList.set(position,editTextDirection.getText().toString());
                            directionsAdapter.notifyItemChanged(position);
                            Toast.makeText(getContext(), "Edited", Toast.LENGTH_SHORT).show();
                            imageViewStep.setImageResource(drawableStepList.get(directionsList.size()));
                            editTextDirection.setText("");
                            buttonAddDirection.setText("Add Direction");
                            buttonAddDirection.setOnClickListener(onClickListenerAdd);
                        }
                    }
                });
            }
        });
        buttonSaveDirections = v.findViewById(R.id.btn_save_directions);
        buttonSaveDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClickedSaveDirections(directionsList);
                dismiss();
            }
        });
        return v;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement Bottomsheetlistener");
        }
    }
}
