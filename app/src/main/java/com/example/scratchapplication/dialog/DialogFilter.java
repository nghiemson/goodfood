package com.example.scratchapplication.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.scratchapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogFilter extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(),R.style.MyRounded_MaterialComponents_MaterialAlertDialog) ;
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter,null);
        builder.setView(view);
        return builder.create();
    }
}
