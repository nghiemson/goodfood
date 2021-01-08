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
 * Use the {@link CookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private List<String> directions;
    private Context context;
    public CookFragment(List<String> directions,Context context) {
        this.directions = directions;
        this.context = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CookFragment newInstance(List<String> directions, Context context) {
        CookFragment fragment = new CookFragment(directions,context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cook, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.rv_directions);
        ListTextAdapter adapter = new ListTextAdapter(directions,context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        return v;
    }
}