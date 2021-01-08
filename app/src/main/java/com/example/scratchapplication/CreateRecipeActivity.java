package com.example.scratchapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.scratchapplication.adapter.ListText2Adapter;
import com.example.scratchapplication.api.JsonApi;
import com.example.scratchapplication.api.RestClient;
import com.example.scratchapplication.model.Comment;
import com.example.scratchapplication.model.home.ModelRecipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRecipeActivity extends AppCompatActivity{
    private static final int PICK_IMAGE_REQUEST = 1;
    //private static final int PICK_MULTI_IMAGE_REQUEST = 2;
    private ImageView imageViewUploadCover;
    private ImageView imageEditGallery;
    private EditText editTextName;
    private EditText editTextDesc;
    private EditText editTextIngredients;
    private EditText editTextDirections;
    private Button buttonAddDirections;
    private Button buttonAddIngredients;
    private Button buttonPost;

    private Uri imageCoverUri;
    private List<String> ingredients;
    private List<String> directions;
    private List<String> usersLike;
    private List<Comment> comments;
    private List<String> filters;

    private ProgressDialog progressDialog;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    private ModelRecipe modelRecipePost;
    private RecyclerView recyclerViewIngredients, recyclerViewDirections;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        progressDialog = new ProgressDialog(this);
        //tao model
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ingredients = new ArrayList<>();
        directions = new ArrayList<>();
        usersLike = new ArrayList<>();
        filters = new ArrayList<>();
        modelRecipePost = new ModelRecipe(uId,"","","",ingredients,directions,0,usersLike,"",
                "",filters);
        //
        mStorageRef = FirebaseStorage.getInstance().getReference("covers");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("recipes");

        //cover
        imageViewUploadCover = findViewById(R.id.image_upload_cover);
        imageViewUploadCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        //recyclerview
        LinearLayoutManager layoutIngredients = new LinearLayoutManager(this);
        layoutIngredients.setReverseLayout(true);
        LinearLayoutManager layoutDirections = new LinearLayoutManager(this);
        layoutIngredients.setReverseLayout(true);

        ListText2Adapter ingredientsAdapter = new ListText2Adapter(ingredients,this);
        ListText2Adapter directionsAdapter = new ListText2Adapter(directions,this);

        recyclerViewIngredients = findViewById(R.id.rv_ingredients_create);
        recyclerViewIngredients.setLayoutManager(layoutIngredients);
        recyclerViewIngredients.setAdapter(ingredientsAdapter);

        recyclerViewDirections = findViewById(R.id.rv_directions_create);
        recyclerViewDirections.setLayoutManager(layoutDirections);
        recyclerViewDirections.setAdapter(directionsAdapter);
        //ingredients
        editTextIngredients = findViewById(R.id.et_add_ingredients);
        buttonAddIngredients = findViewById(R.id.btn_add_ingredients);
        buttonAddIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextIngredients.getText().toString().trim();
                if (text.equals(""))
                    return;
                ingredients.add(text);
                editTextIngredients.setText("");
                ingredientsAdapter.notifyItemInserted(ingredients.size()-1);
            }
        });
        //directions
        editTextDirections = findViewById(R.id.et_add_directions);
        buttonAddDirections = findViewById(R.id.btn_add_direction);
        buttonAddDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextDirections.getText().toString().trim();
                if (text.equals(""))
                    return;
                directions.add(text);
                editTextDirections.setText("");
                directionsAdapter.notifyItemInserted(directions.size()-1);
            }
        });

        editTextName = findViewById(R.id.et_recipe_name);
        editTextDesc = findViewById(R.id.et_recipe_desc);

        buttonPost = findViewById(R.id.btn_post);
        //handler
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            buttonPost.setBackgroundResource(R.drawable.button_visible_background);
        }
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUploadTask!=null&&mUploadTask.isInProgress()){
                    Toast.makeText(CreateRecipeActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    modelRecipePost.setName(editTextName.getText().toString());
                    modelRecipePost.setDescription(editTextDesc.getText().toString());

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    modelRecipePost.setProfileName(user.getDisplayName());


                    if(user.getPhotoUrl()!=null) {
                        modelRecipePost.setProfileAvatar(user.getPhotoUrl().toString());
                    }
                    else {
                        modelRecipePost.setProfileAvatar("https://kansai-resilience-forum.jp/wp-content/uploads/2019/02/IAFOR-Blank-Avatar-Image-1.jpg");
                    }
                    modelRecipePost.setIngredients(ingredients);
                    modelRecipePost.setDirections(directions);
                    ChipGroup chipGroup = findViewById(R.id.chipgroup_create_recipe);
                    for (int i = 0; i< chipGroup.getChildCount();i++){
                        Chip chip = (Chip) chipGroup.getChildAt(i);
                        if (chip.isChecked()){
                            filters.add(chip.getText().toString());
                        }
                    }
                    modelRecipePost.setFilters(filters);
                    uploadFile();
                }
            }
        });
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if(imageCoverUri==null||
                editTextName.getText().toString().trim().equals("")||
                editTextDesc.getText().toString().trim().equals("")||
                modelRecipePost.getIngredients()==null||
                modelRecipePost.getDirections()==null
        ){
            Toast.makeText(this, "Data is invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageCoverUri!=null){
            progressDialog.setMessage("Đang tải lên...");
            progressDialog.show();
            final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis()+"." + getFileExtension(imageCoverUri));
            mUploadTask = fileRef.putFile(imageCoverUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            },500);
                            if(taskSnapshot.getMetadata()!=null){
                                if (taskSnapshot.getMetadata().getReference()!=null){
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            modelRecipePost.setUrlCover(uri.toString());
                                            Log.e("UPLOAD",uri.toString());
                                            //call firebase
                                            String uploadId = mDatabaseRef.push().getKey();
                                            mDatabaseRef.child(uploadId).setValue(modelRecipePost);
                                            //call api
                                            JsonApi service = RestClient.createService(JsonApi.class);
                                            Call<ModelRecipe> call = service.postRecipe(modelRecipePost);
                                            call.enqueue(new Callback<ModelRecipe>() {
                                                @Override
                                                public void onResponse(Call<ModelRecipe> call, Response<ModelRecipe> response) {
                                                    if (!response.isSuccessful()){
                                                        Log.e("Code","Code: "+response.code());
                                                        return;
                                                    }
                                                    progressDialog.dismiss();
                                                    Toast.makeText(CreateRecipeActivity.this, "Post successfully", Toast.LENGTH_SHORT).show();
                                                    //intent
                                                    Intent intent = new Intent(CreateRecipeActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                @Override
                                                public void onFailure(Call<ModelRecipe> call, Throwable t) {
                                                    Toast.makeText(CreateRecipeActivity.this, "Post failed!", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            });
                                        }
                                    });
                                }
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateRecipeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
            Toast.makeText(this, "No file Selected", Toast.LENGTH_SHORT).show();
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
                    imageCoverUri = data.getData();
                    Picasso.with(this).load(imageCoverUri).into(imageViewUploadCover);
                    imageViewUploadCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                break;

            default:
        }
    }
}