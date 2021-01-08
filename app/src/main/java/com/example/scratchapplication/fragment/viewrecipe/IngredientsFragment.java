package com.example.scratchapplication.fragment.viewrecipe;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scratchapplication.R;
import com.example.scratchapplication.adapter.ListTextAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private List<String> ingredients;
    private Context context;

    public IngredientsFragment(List<String> ingredients,Context context) {
        this.ingredients = ingredients;
        this.context = context;
    }


    public static IngredientsFragment newInstance(List<String> ingredients, Context context) {
        IngredientsFragment fragment = new IngredientsFragment(ingredients,context);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_ingredients, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.rv_ingredients);
        ListTextAdapter adapter = new ListTextAdapter(ingredients,context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        return v;
    }
}