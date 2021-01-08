package com.example.scratchapplication.dialog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.OnItemClickListener;
import com.example.scratchapplication.R;
import com.example.scratchapplication.adapter.GalleryAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomSheetGallery extends BottomSheetDialogFragment {
    private List<Uri> galleryUri;
    private Context context;
    private ImageView imageViewCloseGallery;
    private RecyclerView recyclerViewGallery;

    public BottomSheetGallery(List<Uri> galleryUri, Context context){
        this.galleryUri = galleryUri;
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_gallery,container,false);
        imageViewCloseGallery = v.findViewById(R.id.image_close_dialog_gallery);
        imageViewCloseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        recyclerViewGallery = v.findViewById(R.id.rv_edit_gallery);
        GalleryAdapter galleryAdapter = new GalleryAdapter(galleryUri,context);
        recyclerViewGallery.setAdapter(galleryAdapter);
        galleryAdapter.setOnClickItemListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
