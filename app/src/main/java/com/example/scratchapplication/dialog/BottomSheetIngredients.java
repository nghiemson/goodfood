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

import com.example.scratchapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class BottomSheetIngredients extends BottomSheetDialogFragment {
    private ImageView imageViewClose;
    private ImageView imageViewStep;
    private RecyclerView recyclerView;
    private EditText editText;
    private Button buttonAdd;
    private Button buttonSave;

    private List<String> ingredientsList;
    private List<Integer> drawableStepList;
    private BottomSheetListener mListener;


    public interface BottomSheetListener{
        void onButtonClickedSaveIngredients(List<String> list);
    }
    public BottomSheetIngredients(){

        ingredientsList = new ArrayList<>();
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
    class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_text,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String ingredient = ingredientsList.get(position);
            holder.textViewIngredient.setText(ingredient);
            holder.imageViewStep.setImageResource(drawableStepList.get(position));
        }

        @Override
        public int getItemCount() {
            return ingredientsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageViewStep;
            private ImageView imageViewEdit;
            private TextView textViewIngredient;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageViewStep = itemView.findViewById(R.id.image_step_ingredient);

                imageViewEdit = itemView.findViewById(R.id.image_edit_ingredients_rv);
                textViewIngredient = itemView.findViewById(R.id.txt_ingredient);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_ingredients, container, false);
        imageViewClose = v.findViewById(R.id.image_close_dialog_ingredients);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        imageViewStep = v.findViewById(R.id.image_step_ingredient_addedit);
        Picasso.with(getContext()).load(drawableStepList.get(ingredientsList.size()));

        final IngredientsAdapter ingredientsAdapter = new IngredientsAdapter();
        recyclerView = v.findViewById(R.id.rv_ingredients_in_dialog);
        recyclerView.setAdapter(ingredientsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        editText = v.findViewById(R.id.et_ingredient);

        buttonAdd = v.findViewById(R.id.btn_add_ingredient_into_bts);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ingredientsList.size();
                String ingredient = editText.getText().toString().trim();
                if (!ingredient.equals("")&&position<9){
                    ingredientsList.add(ingredient);
                    ingredientsAdapter.notifyItemInserted(position);
                    editText.setText("");
                    recyclerView.scrollToPosition(position);
                    editText.setFocusable(true);
                    imageViewStep.setImageResource(drawableStepList.get(position+1));
                    Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonSave = v.findViewById(R.id.btn_save_ingredients);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClickedSaveIngredients(ingredientsList);
                System.out.println("clicked");
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
