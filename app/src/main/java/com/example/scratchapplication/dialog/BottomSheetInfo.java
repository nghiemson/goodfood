package com.example.scratchapplication.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scratchapplication.R;
import com.example.scratchapplication.model.recipe.Additional;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetInfo extends BottomSheetDialogFragment {
    private ImageView imageViewClose;
    private EditText editTextServingTime;
    private EditText editTextNutrition;
    private EditText editTextTags;
    private Button buttonSaveInfo;

    private BottomSheetListener mListener;

    public interface BottomSheetListener{
        void onButtonClickedSaveInfo(Additional additional);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_info,container,false);
        imageViewClose = v.findViewById(R.id.image_close_dialog_info);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        editTextServingTime = v.findViewById(R.id.et_serving_time);
        editTextNutrition = v.findViewById(R.id.et_nutrition);
        editTextTags = v.findViewById(R.id.et_tags);
        buttonSaveInfo = v.findViewById(R.id.btn_save_info);



        buttonSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Additional additional = new Additional(
                        editTextServingTime.getText().toString(),
                        editTextNutrition.getText().toString(),
                        editTextTags.getText().toString()
                );
                mListener.onButtonClickedSaveInfo(additional);
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
