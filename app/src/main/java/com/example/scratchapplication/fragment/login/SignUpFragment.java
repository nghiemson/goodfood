package com.example.scratchapplication.fragment.login;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scratchapplication.R;
import com.example.scratchapplication.api.JsonApi;
import com.example.scratchapplication.api.RestClient;
import com.example.scratchapplication.model.Profile;
import com.example.scratchapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SignUpFragment extends Fragment {

    private Button buttonSignUp;
    private TextView login;
    private EditText txtFullName;
    private EditText txtEmailSignUp;
    private EditText txtPassSignUp;
    private FirebaseAuth mAuth;
    // Initialize Firebase Auth


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_sign_up,container,false);

            txtFullName = v.findViewById(R.id.txtFullName);
            txtEmailSignUp = v.findViewById(R.id.txtEmailSignUp);
            txtPassSignUp = v.findViewById(R.id.txtPassSignUp);

            buttonSignUp = v.findViewById(R.id.btn_signUp);
            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = txtFullName.getText().toString();
                    String email = txtEmailSignUp.getText().toString();
                    String password = txtPassSignUp.getText().toString();

                    createAccount(name, email, password);

//                    Intent intent = new Intent(getContext(), MainActivity.class);
//                    intent.putExtra("CHECK", false);
//                    startActivity(intent);
//                    getActivity().finish();
                }
            });

            login = v.findViewById(R.id.idSignIn);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.login_container, new SignInFragment())
                            .commit();
                }
            });
        return v;
    }

    private void createAccount(final String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse("https://kansai-resilience-forum.jp/wp-content/uploads/2019/02/IAFOR-Blank-Avatar-Image-1.jpg"))
                                    .build();
                            user.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("UPDATEPROFILE", "User profile updated.");
                                            }
                                        }
                                    });
                            String uid = user.getUid();

                            ArrayList followers = new ArrayList();
                            String avatar = "https://kansai-resilience-forum.jp/wp-content/uploads/2019/02/IAFOR-Blank-Avatar-Image-1.jpg";
                            System.out.println(avatar);
                            if (user.getPhotoUrl() != null){
                                avatar = user.getPhotoUrl().toString();
                            }
                            User dataUser = new User(name, avatar,"",0, followers);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");
                            myRef.child(uid).setValue(dataUser);
                            //Add profile to nodejs
                            Profile profile = new Profile(new ArrayList<String>(),new ArrayList<String>(),"",avatar,0,name,uid);
                            JsonApi api = RestClient.createService(JsonApi.class);
                            retrofit2.Call<Profile> call = api.addProfile(profile);
                            call.enqueue(new Callback<Profile>() {
                                @Override
                                public void onResponse(Call<Profile> call, Response<Profile> response) {
                                    if (!response.isSuccessful()){
                                        Log.e("Code_add_user",response.code()+"");
                                        return;
                                    }
                                }

                                @Override
                                public void onFailure(Call<Profile> call, Throwable t) {

                                }
                            });
                            //
                            getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.login_container, new SignInFragment())
                                        .commit();
                        } else {
                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }
                });
    }
}
