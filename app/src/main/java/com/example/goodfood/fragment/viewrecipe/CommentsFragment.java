package com.example.goodfood.fragment.viewrecipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goodfood.R;
import com.example.goodfood.model.recipe.CommentView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewComment;
    private EditText editTextComment;
    private Button buttonComment;
    private List<CommentView> commentViews;

    private DatabaseReference databaseRef;
    private FirebaseRecyclerAdapter<CommentView, CommentViewHolder> adapter;

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatar;
        TextView textViewName;
        TextView textViewComment;
        public CommentViewHolder(@NonNull View v) {
            super(v);
            imageViewAvatar = itemView.findViewById(R.id.image_avatar_comment);
            textViewName = itemView.findViewById(R.id.txt_profile_name_comment);
            textViewComment = itemView.findViewById(R.id.txt_comment);
        }
    }


    public CommentsFragment(String mParam1, String mParam2) {
        this.mParam1 = mParam1;
        this.mParam2 = mParam2;
    }

    public static CommentsFragment newInstance(String param1, String param2) {
        CommentsFragment fragment = new CommentsFragment(param1,param2);

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

        View v = inflater.inflate(R.layout.fragment_comment, container, false);
        recyclerViewComment = v.findViewById(R.id.rv_comment);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setReverseLayout(true);
        recyclerViewComment.setLayoutManager(layout);
        commentViews = new ArrayList<>();

        editTextComment = v.findViewById(R.id.et_comment);
        buttonComment = v.findViewById(R.id.btn_comment);

            buttonComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    databaseRef.child(mParam1).push().setValue(new CommentView(
                            user.getPhotoUrl().toString(),
                            user.getDisplayName(),
                            editTextComment.getText().toString().trim()
                    ));
                    editTextComment.setText("");
                }
            });

        databaseRef = FirebaseDatabase.getInstance().getReference("comments");
        SnapshotParser<CommentView> parser = new SnapshotParser<CommentView>() {
            @NonNull
            @Override
            public CommentView parseSnapshot(@NonNull DataSnapshot snapshot) {
                CommentView commentView = snapshot.getValue(CommentView.class);
                return commentView;
            }
        };
        final DatabaseReference commentRef = databaseRef.child(mParam1);
        FirebaseRecyclerOptions<CommentView> options = new FirebaseRecyclerOptions.Builder<CommentView>()
                .setQuery(commentRef,parser)
                .build();
        adapter = new FirebaseRecyclerAdapter<CommentView, CommentViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull final CommentViewHolder holder,
                                            int i,
                                            @NonNull CommentView commentView) {
                if (commentView.getComment()!=null){
                    //Log.e("RV",commentView.getComment());
                    holder.textViewName.setText(commentView.getName());
                    holder.textViewComment.setText(commentView.getComment());
                    Picasso.with(getContext()).load(commentView.getAvatar()).into(holder.imageViewAvatar);
                }
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
                return new CommentViewHolder(v);
            }
        };

        recyclerViewComment.setAdapter(adapter);

        return v;
    }

    @Override
    public void onPause() {
        adapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        adapter.startListening();
        super.onResume();
    }
}