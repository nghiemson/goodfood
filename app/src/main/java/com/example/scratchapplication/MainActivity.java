package com.example.scratchapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.scratchapplication.api.RestClient;
import com.example.scratchapplication.api.TokenApi;
import com.example.scratchapplication.dialog.BottomSheetFilter;
import com.example.scratchapplication.fragment.main.HomeFragment;
import com.example.scratchapplication.fragment.main.ProfileFragment;
import com.example.scratchapplication.fragment.main.SearchFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomSheetFilter.BottomSheetFilterListener {

    BottomNavigationView bottomNavigation;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent = getIntent();
        boolean check = intent.getBooleanExtra("CHECK",true);
        if (currentUser==null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        else {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            Log.e("token", task.getResult());
                            String token = task.getResult();
                            sendToken(token);
                        }
                    });
            bottomNavigation = findViewById(R.id.bottom_navigation);
            bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment(new ArrayList<>(), false, false)).commit();
                bottomNavigation.setSelectedItemId(R.id.navigation_home);
            }
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    openFragment(new HomeFragment(new ArrayList<String>(),false,false));
                    break;
                case R.id.navigation_profile:
                    openFragment(ProfileFragment.newInstance("",""));
                    break;
                case R.id.navigation_search:
                    openFragment(SearchFragment.newInstance("", ""));
                    break;
            }
            return true;
        }
    };
    private void sendToken(String token){
        TokenApi.TokenSend tokenSend = new TokenApi.TokenSend(FirebaseAuth.getInstance().getUid(),token);
        TokenApi service = RestClient.createService(TokenApi.class);
        service.sendToken(tokenSend).enqueue(new Callback<TokenApi.TokenSend>() {
            @Override
            public void onResponse(Call<TokenApi.TokenSend> call, Response<TokenApi.TokenSend> response) {
                Log.e("sendTokenCode", response.code()+"");
            }

            @Override
            public void onFailure(Call<TokenApi.TokenSend> call, Throwable t) {

            }
        });
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }

    @Override
    public void onButtonSaveFilterClicked(List<String> listFilter, boolean filterFollow, boolean orderByLike) {
        openFragment(new HomeFragment(listFilter,filterFollow,orderByLike));
    }
}
