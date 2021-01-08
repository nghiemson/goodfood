package com.example.scratchapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceManager;

import com.example.scratchapplication.api.JsonApi;
import com.example.scratchapplication.api.RestClient;
import com.example.scratchapplication.model.Profile;
import com.example.scratchapplication.model.ProfilePojo;
import com.example.scratchapplication.model.UpdatePojo;
import com.example.scratchapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private CircleImageView imageViewAvatar;
    private EditText editTextName,editTextEmail,editTextAddress;
    private Button buttonEdit;
    private TextView textViewUpload;
    private Uri uriUpload;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String uid = FirebaseAuth.getInstance().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("avatar");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        imageViewAvatar = findViewById(R.id.edit_avatar);
        Picasso.with(this).load(user.getPhotoUrl()).into(imageViewAvatar);
        editTextName = findViewById(R.id.et_fullname);
        editTextName.setText(user.getDisplayName());
        editTextAddress = findViewById(R.id.et_address);
        editTextEmail = findViewById(R.id.et_email);
        editTextEmail.setText(user.getEmail());
        textViewUpload = findViewById(R.id.tv_edit_avatar);
        textViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        buttonEdit = findViewById(R.id.btn_edit);
        JsonApi api = RestClient.createService(JsonApi.class);
        api.getProfile(uid).enqueue(new Callback<ProfilePojo>() {
            @Override
            public void onResponse(Call<ProfilePojo> call, Response<ProfilePojo> response) {
                if (response.isSuccessful()){
                    Profile profile = response.body().getProfile();
                    editTextAddress.setText(profile.getAddress());
                }
            }

            @Override
            public void onFailure(Call<ProfilePojo> call, Throwable t) {

            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String name = editTextName.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String address = editTextAddress.getText().toString().trim();
                JsonApi service = RestClient.createService(JsonApi.class);

                if (name.equals("")||email.equals("")||address.equals("")){
                    Toast.makeText(EditProfileActivity.this, "Invalid data input", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uriUpload!=null) {
                    StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uriUpload));

                    mUploadTask = fileRef.putFile(uriUpload)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    if (taskSnapshot.getMetadata() != null) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {

                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

                                                    UpdatePojo profile = new UpdatePojo(name, address,uri.toString(),uid);
                                                    service.updateProfile(profile).enqueue(new Callback<UpdatePojo>() {
                                                        @Override
                                                        public void onResponse(Call<UpdatePojo> call, Response<UpdatePojo> response) {
                                                            if (response.isSuccessful()){
                                                                UserProfileChangeRequest profileChangeRequest;
                                                                profileChangeRequest = new UserProfileChangeRequest.Builder()
                                                                        .setDisplayName(name)
                                                                        .setPhotoUri(Uri.parse(uri.toString()))
                                                                        .build();

                                                                user.updateEmail(email)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Log.d("EMAIL", "User email address updated.");
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                return;
                                                                            }
                                                                        });
                                                                user.updateProfile(profileChangeRequest)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Log.d("UPDATEPROFILE", "User profile updated.");
                                                                                }
                                                                            }
                                                                        });
                                                                finish();
                                                            }
                                                        }
                                                        @Override
                                                        public void onFailure(Call<UpdatePojo> call, Throwable t) {
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                }
                else {
                    UpdatePojo profile = new UpdatePojo(name, address,user.getPhotoUrl().toString(),uid);
                    service.updateProfile(profile).enqueue(new Callback<UpdatePojo>() {
                        @Override
                        public void onResponse(Call<UpdatePojo> call, Response<UpdatePojo> response) {
                            if (response.isSuccessful()){
                                //update firebaseuser
                                UserProfileChangeRequest profileChangeRequest;
                                profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                                user.updateEmail(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d("EMAIL", "User email address updated.");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                return;
                                            }
                                        });
                                user.updateProfile(profileChangeRequest)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("UPDATEPROFILE", "User profile updated.");
                                                }
                                            }
                                        });
                                finish();
                            }
                            else {
                                Log.e("code", response.code()+"");
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdatePojo> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    uriUpload = data.getData();
                    Picasso.with(this).load(uriUpload).into(imageViewAvatar);
                }
                break;

            default:
        }
    }
}
