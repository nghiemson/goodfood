package com.example.scratchapplication.tablayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.R;
import com.example.scratchapplication.model.home.ModelRecipe;
import com.example.scratchapplication.room.RecipesViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveFragment extends Fragment implements Serializable{

    private RecyclerView recyclerView;
    private List<ModelRecipe> recipes;
    private List<String> saves;

    public SaveFragment(List<String> saves){
        this.saves = saves;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        //DatabaseHandler handler = new DatabaseHandler(getContext());
        recipes = new ArrayList<>();
        //recipes = handler.getAllRecipes();
        recyclerView = view.findViewById(R.id.rv_save);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        RecipesViewModel recipesViewModel = ViewModelProviders.of(getActivity()).get(RecipesViewModel.class);
        recipesViewModel.getAllRecipes().observe(getActivity(), new Observer<List<ModelRecipe>>() {
            @Override
            public void onChanged(List<ModelRecipe> modelRecipes) {
                recipes.clear();
                for (ModelRecipe modelRecipe:modelRecipes){
                    if (saves.contains(modelRecipe.getRid())){
                        if (!recipes.contains(modelRecipe)) {
                            Log.e("save fragment", modelRecipe.getName());
                            recipes.add(modelRecipe);
                        }

                    }
                }
                recyclerView.setAdapter(new RecipesFragment.MyRecipeAdapter(recipes,getContext()));
            }
        });
        return view;
    }
}

