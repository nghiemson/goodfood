package com.example.scratchapplication.fragment.main;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.R;
import com.example.scratchapplication.adapter.FeedAdapter;
import com.example.scratchapplication.api.JsonApi;
import com.example.scratchapplication.api.RestClient;
import com.example.scratchapplication.model.Profile;
import com.example.scratchapplication.model.ProfilePojo;
import com.example.scratchapplication.model.home.ModelRecipe;
import com.example.scratchapplication.room.RecipesViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView myList;
    private RecipesViewModel recipesViewModel;

    //View
    private EditText editTextSearch;
    public SearchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        recipesViewModel = ViewModelProviders.of(getActivity()).get(RecipesViewModel.class);
        final EditText editTextSearch = v.findViewById(R.id.editTextSearch);
        myList = v.findViewById(R.id.recyclerview_trending);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH&&!editTextSearch.getText().toString().trim().equals("")) {
                    List<ModelRecipe> newList = new ArrayList<>();
                    recipesViewModel.getAllRecipes().observe(getActivity(), new Observer<List<ModelRecipe>>() {
                        @Override
                        public void onChanged(List<ModelRecipe> modelRecipes) {
                            String textSearch =editTextSearch.getText().toString().trim();
                            for (ModelRecipe m:modelRecipes){
                                if (m.getName().toLowerCase().contains(textSearch.toLowerCase())) {
                                    newList.add(m);
                                }
                            }
                            for (ModelRecipe m:modelRecipes){
                                if (!newList.contains(m)&&m.getDescription().toLowerCase().contains(textSearch.toLowerCase())) {
                                    newList.add(m);
                                }
                            }
                            for (ModelRecipe m:modelRecipes){
                                if (!newList.contains(m)&&m.getProfileName().toLowerCase().contains(textSearch.toLowerCase())){
                                    newList.add(m);
                                    Log.e("search","add"+m.getProfileName());
                                }
                            }
                            for (ModelRecipe m:modelRecipes){
                                List<String> filters = new ArrayList<>();
                                for (int k = 0; k<m.getFilters().size();k++){
                                    filters.add(m.getFilters().get(k).toLowerCase());
                                }
                                if (!newList.contains(m)&&filters.contains(textSearch)) {
                                    newList.add(m);
                                }
                            }
                            for (ModelRecipe m:modelRecipes){
                                for (String text:m.getIngredients()){
                                    if (!newList.contains(m)&&text.toLowerCase().contains(textSearch.toLowerCase())) {
                                        newList.add(m);
                                        break;
                                    }
                                }
                            }
                            Log.e("list search",newList.size()+"");
                            if (newList.size()==0)
                                Toast.makeText(getContext(), "Không có kết quả tìm kiếm phù hợp", Toast.LENGTH_SHORT).show();
                            else{
                                String uid = FirebaseAuth.getInstance().getUid();
                                JsonApi service = RestClient.createService(JsonApi.class);
                                Call<ProfilePojo> profileCall = service.getProfile(uid);
                                profileCall.enqueue(new Callback<ProfilePojo>() {
                                    @Override
                                    public void onResponse(Call<ProfilePojo> call, Response<ProfilePojo> response) {
                                        Profile profile = response.body().getProfile();
                                        FeedAdapter adapter = new FeedAdapter(getContext(),newList,uid,profile.getFollows(),profile.getSaves());
                                        myList.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onFailure(Call<ProfilePojo> call, Throwable t) {
                                        FeedAdapter adapter = new FeedAdapter(getContext(),newList,uid,new ArrayList<>(),new ArrayList<>());
                                        myList.setAdapter(adapter);
                                    }
                                });
                            }
                        }
                    });
                    return true;
                }
                return false;
            }
        });
        return v;
    }

}