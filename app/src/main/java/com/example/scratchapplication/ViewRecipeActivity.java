package com.example.scratchapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.scratchapplication.adapter.PagerViewRecipeAdapter;
import com.example.scratchapplication.api.JsonApi;
import com.example.scratchapplication.api.RestClient;
import com.example.scratchapplication.fragment.viewrecipe.CookFragment;
import com.example.scratchapplication.fragment.viewrecipe.CommentsFragment;
import com.example.scratchapplication.fragment.viewrecipe.IngredientsFragment;
import com.example.scratchapplication.model.RecipePojo;
import com.example.scratchapplication.model.home.ModelRecipe;
import com.example.scratchapplication.room.RecipesViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRecipeActivity extends AppCompatActivity {
    private static final String[] TITLES = new String[]{"Ingredients","How to Cook","Comments"};
    private TabLayout tabLayout;
    private ViewPager pager;
    private Toolbar toolbar;
    private CommentsFragment commentsFragment;
    private IngredientsFragment ingredientsFragment;
    private CookFragment cookFragment;
    private RecipesViewModel recipesViewModel;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_recipe);
        recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        Intent intent = getIntent();
        String rId = intent.getStringExtra("RID");
        uid = intent.getStringExtra("UID");
        init(rId);
    }

    private void init(String id) {
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapse_toolbar_layout);
        ImageView imageView = findViewById(R.id.image_cover_collapse);
        toolbar = findViewById(R.id.recipe_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        JsonApi api = RestClient.createService(JsonApi.class);
        Call<RecipePojo> call = api.getRecipeDetail(new JsonApi.Rid(id));
        call.enqueue(new Callback<RecipePojo>() {
            @Override
            public void onResponse(Call<RecipePojo> call, Response<RecipePojo> response) {
                if (!response.isSuccessful()){
                    Log.e("Code",response.code()+" "+call.request().url().toString());
                    return;
                }
                ModelRecipe modelRecipe = response.body().getModelRecipe();
                collapsingToolbarLayout.setTitle(modelRecipe.getName());
                Picasso.with(getApplicationContext()).load(modelRecipe.getUrlCover()).into(imageView);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                pager = findViewById(R.id.pager_view_recipe);
                tabLayout = findViewById(R.id.tabs_view_recipe);
                commentsFragment = new CommentsFragment(modelRecipe.getRid(),modelRecipe.getDataComment());
                ingredientsFragment = IngredientsFragment.newInstance(modelRecipe.getIngredients(),ViewRecipeActivity.this);
                cookFragment = CookFragment.newInstance(modelRecipe.getDirections(),ViewRecipeActivity.this);
                tabLayout.setupWithViewPager(pager);
                List<Fragment> fragments = new ArrayList<>();

                fragments.add(ingredientsFragment);
                fragments.add(cookFragment);
                fragments.add(commentsFragment);
                List<String> titles = new ArrayList<>(Arrays.asList(TITLES));
                pager.setAdapter(new PagerViewRecipeAdapter(getSupportFragmentManager(),0,fragments,titles));
            }

            @Override
            public void onFailure(Call<RecipePojo> call, Throwable t) {
                recipesViewModel.getRecipeById(id).observe(ViewRecipeActivity.this, new Observer<ModelRecipe>() {
                    @Override
                    public void onChanged(ModelRecipe recipe) {
                        collapsingToolbarLayout.setTitle(recipe.getName());
                        Picasso.with(getApplicationContext()).load(recipe.getUrlCover()).into(imageView);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowTitleEnabled(true);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                        pager = findViewById(R.id.pager_view_recipe);
                        tabLayout = findViewById(R.id.tabs_view_recipe);
                        //commentsFragment = new CommentsFragment(recipe.getRid(),recipe.getDataComment());
                        ingredientsFragment = IngredientsFragment.newInstance(recipe.getIngredients(),ViewRecipeActivity.this);
                        cookFragment = CookFragment.newInstance(recipe.getDirections(),ViewRecipeActivity.this);
                        tabLayout.setupWithViewPager(pager);
                        List<Fragment> fragments = new ArrayList<>();

                        fragments.add(ingredientsFragment);
                        fragments.add(cookFragment);
                        //fragments.add(commentsFragment);
                        List<String> titles = new ArrayList<>(Arrays.asList(TITLES));
                        titles.remove("Comments");
                        pager.setAdapter(new PagerViewRecipeAdapter(getSupportFragmentManager(),0,fragments,titles));
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.item_toolbar_profile){
            Intent intent = new Intent(ViewRecipeActivity.this, OtherProfileActivity.class);
            intent.putExtra("UID",uid);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}