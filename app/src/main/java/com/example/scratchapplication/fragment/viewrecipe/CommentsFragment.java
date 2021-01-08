package com.example.scratchapplication.fragment.viewrecipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scratchapplication.R;
import com.example.scratchapplication.adapter.CommentAdapter;
import com.example.scratchapplication.api.JsonApi;
import com.example.scratchapplication.api.RestClient;
import com.example.scratchapplication.model.Comment;
import com.example.scratchapplication.model.recipe.CommentView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsFragment extends Fragment {

    private RecyclerView recyclerViewComment;
    private EditText editTextComment;
    private Button buttonComment;
    private List<CommentView> commentViews;
    private CommentAdapter adapter;

    private String rId;
    private List<Comment> listComments;
    public CommentsFragment(String rId,List<Comment> listComments){
        this.rId = rId;
        this.listComments = listComments;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_comment, container, false);
        recyclerViewComment = v.findViewById(R.id.rv_comment);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setReverseLayout(true);
        recyclerViewComment.setLayoutManager(layout);
        adapter = new CommentAdapter(listComments,getContext());
        recyclerViewComment.setAdapter(adapter);

        JsonApi api = RestClient.createService(JsonApi.class);
        editTextComment = v.findViewById(R.id.et_comment);
        String uid = FirebaseAuth.getInstance().getUid();
        buttonComment = v.findViewById(R.id.btn_comment);
        buttonComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!editTextComment.getText().toString().trim().equals("")) {
                        Comment comment = new Comment(editTextComment.getText().toString(), uid, rId);
                        Gson gson = new Gson();
                        String json = gson.toJson(comment);
                        Log.e("comment",json);
                        Call<Comment> postComment = api.postComment(comment);
                        postComment.enqueue(new Callback<Comment>() {
                            @Override
                            public void onResponse(Call<Comment> call, Response<Comment> response) {
                                if (!response.isSuccessful()){
                                    Log.e("Code_comment", response.code()+" "+call.request().url().toString());
                                    return;
                                }
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                comment.setAvatar(user.getPhotoUrl().toString());
                                comment.setName(user.getDisplayName());
                                listComments.add(comment);
                                adapter.notifyItemInserted(listComments.size()-1);
                                recyclerViewComment.scrollToPosition(listComments.size()-1);
                                editTextComment.setText("");
                            }

                            @Override
                            public void onFailure(Call<Comment> call, Throwable t) {
                                Toast.makeText(getContext(), "Đã xảy ra lỗi, vui long thử lại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        return v;
    }
}