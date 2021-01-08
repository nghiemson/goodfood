package com.example.scratchapplication.fragment.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.AllMessageActivity;
import com.example.scratchapplication.CreateRecipeActivity;
import com.example.scratchapplication.MainActivity;
import com.example.scratchapplication.R;
import com.example.scratchapplication.adapter.FeedAdapter;
import com.example.scratchapplication.api.JsonApi;
import com.example.scratchapplication.api.ProfileServiceRepository;
import com.example.scratchapplication.api.RestClient;
import com.example.scratchapplication.dialog.BottomSheetFilter;
import com.example.scratchapplication.model.ListRecipes;
import com.example.scratchapplication.model.ListUsers;
import com.example.scratchapplication.model.Profile;
import com.example.scratchapplication.model.ProfilePojo;
import com.example.scratchapplication.model.home.ModelRecipe;
import com.example.scratchapplication.room.ProfileViewModel;
import com.example.scratchapplication.room.RecipesViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //View
    private RecyclerView recyclerView;
    private Button buttonAdd;
    private ImageView imageViewFilter;
    private ImageView imageViewAllMessages;

    private FeedAdapter adapter;
    private ShimmerFrameLayout shimmerFrameLayout;

    //private List<ModelRecipe> modelRecipeList;
    private List<String> listFilter;
    private boolean filterFollow,orderByLike;

    private RecipesViewModel recipesViewModel;
    private ProfileViewModel profileViewModel;

    public HomeFragment(List<String> listFilter,boolean filterFollow, boolean orderByLike) {
        this.listFilter = listFilter;
        this.filterFollow = filterFollow;
        this.orderByLike = orderByLike;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        shimmerFrameLayout = v.findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmerAnimation();
        imageViewAllMessages = v.findViewById(R.id.image_messages);
        imageViewAllMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllMessageActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = v.findViewById(R.id.recyclerview_feed);
        String uid =FirebaseAuth.getInstance().getCurrentUser().getUid();
        adapter = new FeedAdapter(container.getContext(),new ArrayList<>(),uid,new ArrayList<>(),new ArrayList<>());
        //layout
        LinearLayoutManager layout = new LinearLayoutManager(getContext());

        //set rv
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        JsonApi service = RestClient.createService(JsonApi.class);
        service.getAllRecipes().enqueue(new Callback<ListRecipes>() {
            @Override
            public void onResponse(Call<ListRecipes> call, Response<ListRecipes> response) {
                if (response.isSuccessful()){
                    List<ModelRecipe> modelRecipes = response.body().getData();
                    updateUI(modelRecipes,uid);
                }
            }

            @Override
            public void onFailure(Call<ListRecipes> call, Throwable t) {
                Toast.makeText(getContext(), "Không có kết nối internet", Toast.LENGTH_SHORT).show();
                recipesViewModel.getAllRecipes().observe(getActivity(), new Observer<List<ModelRecipe>>() {
                    @Override
                    public void onChanged(List<ModelRecipe> modelRecipes) {
                        if (modelRecipes.size()>0){
                            updateUI(modelRecipes,uid);
                        }
                    }
                });
            }
        });
        //button add
        buttonAdd =(Button) v.findViewById(R.id.btn_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateRecipeActivity.class);
                startActivity(intent);
            }
        });
        //filter
        imageViewFilter = v.findViewById(R.id.image_filter);
        imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFilter bottomSheetFilter = new BottomSheetFilter(listFilter,filterFollow,orderByLike);
                bottomSheetFilter.show(getFragmentManager(),"bottomSheetFilter");
            }
        });

        return v;
    }
    private void updateUI(List<ModelRecipe> modelRecipes,String uid){
        profileViewModel.getProfileById(uid).observe(getActivity(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                if (profile != null){
                    List<ModelRecipe> list = new ArrayList<>(modelRecipes);
                    //sap xep
                    if (orderByLike){
                        Collections.sort(list,new ModelRecipe());
                    }
                    else list = new ArrayList<>(modelRecipes);
                    //loc theo doi
                    if (filterFollow){
                        if (profile.getFollows().size()==0){
                            Toast.makeText(getContext(), "Hiện tại bạn chưa follow người dùng nào", Toast.LENGTH_SHORT).show();
                            filterFollow = false;
                        }else {
                            Iterator<ModelRecipe> iterator = list.iterator();
                            while (iterator.hasNext()){
                                if (!profile.getFollows().contains(iterator.next().getUid())){
                                    iterator.remove();
                                }
                            }
                        }
                    }
                    //loc chu de
                    if (listFilter.size()>0){
                        Iterator<ModelRecipe> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            boolean check = false;
                            for (String filter : listFilter) {
                                if (iterator.next().getFilters().contains(filter))
                                    check = true;
                            }
                            if (!check)
                                iterator.remove();
                        }
                    }
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    adapter = new FeedAdapter(getContext(),list,uid,profile.getFollows(),profile.getSaves());
                    recyclerView.setAdapter(adapter);
                }
                else {
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    adapter = new FeedAdapter(getContext(),modelRecipes,uid,new ArrayList<>(),new ArrayList<>());
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("attach","on");
        recipesViewModel = ViewModelProviders.of(getActivity()).get(RecipesViewModel.class);
        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmerAnimation();
    }
}