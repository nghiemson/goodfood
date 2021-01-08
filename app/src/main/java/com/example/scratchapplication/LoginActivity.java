package com.example.scratchapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;

import com.example.scratchapplication.fragment.login.SignInFragment;

public class LoginActivity extends AppCompatActivity {

    private Fragment fragment;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragment = new SignInFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.login_container,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }

}