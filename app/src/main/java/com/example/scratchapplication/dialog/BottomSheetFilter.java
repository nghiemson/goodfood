package com.example.scratchapplication.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scratchapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetFilter extends BottomSheetDialogFragment {
    private ChipGroup chipGroupFilter;

    private List<String> listFilter;
    private boolean filterFollow;
    private boolean orderByLike;

    private Button buttonSaveFilter;
    private Chip chipFollowFilter,chipOrderByLike;
    private BottomSheetFilterListener mListener;

    public BottomSheetFilter(List<String> listFilter,boolean filterFollow, boolean orderByLike){
        this.listFilter = listFilter;
        this.filterFollow = filterFollow;
        this.orderByLike = orderByLike;
        Log.e("FILTER_DIALOG", this.listFilter.size()+" "+this.filterFollow+" "+this.orderByLike);
    }

    public interface BottomSheetFilterListener{
        void onButtonSaveFilterClicked(List<String>listFilter, boolean filterFollow, boolean orderByLike);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_filter,container,false);
        chipGroupFilter = view.findViewById(R.id.chipgroup_filter);
        chipFollowFilter = view.findViewById(R.id.chip_following_filter);
        chipOrderByLike = view.findViewById(R.id.chip_like_count_orderby);
        //set recent value
        for (int i=0; i<chipGroupFilter.getChildCount(); i++){
            Chip chip = (Chip) chipGroupFilter.getChildAt(i);
            if (listFilter.contains(chip.getText().toString())){
                chip.setChecked(true);
            }
            else {
                chip.setChecked(false);
            }
        }

        if (filterFollow)
            chipFollowFilter.setChecked(true);
        if (orderByLike)
            chipOrderByLike.setChecked(true);
        //save filter
        buttonSaveFilter = view.findViewById(R.id.btn_save_filter);
        buttonSaveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listFilter = new ArrayList<>();
                for (int i=0; i<chipGroupFilter.getChildCount(); i++){
                    Chip chip = (Chip) chipGroupFilter.getChildAt(i);
                    if (chip.isChecked()){
                        listFilter.add(chip.getText().toString());
                    }
                }
                filterFollow = false;
                orderByLike = false;
                if (chipFollowFilter.isChecked())
                    filterFollow = true;
                if (chipOrderByLike.isChecked())
                    orderByLike = true;
                Log.e("Check", filterFollow +""+ orderByLike);
                mListener.onButtonSaveFilterClicked(listFilter,filterFollow,orderByLike);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetFilterListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement BottomSheetFilterListener");
        }
    }
}
