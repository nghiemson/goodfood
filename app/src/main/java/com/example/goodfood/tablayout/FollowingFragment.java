package com.example.goodfood.tablayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodfood.OtherProfileActivity;
import com.example.goodfood.R;
import com.example.goodfood.model.User;
import com.example.goodfood.model.profile.FollowingList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowingFragment extends Fragment {
    // TODO: Customize parameter argument names
    // TODO: Customize parameters
    private static final String ARG_PARAM1 = "param1";
    List<FollowingList> mFollowingList;
    RecyclerView recyclerView;
    private String uid;

    public FollowingFragment(String uid){
        this.uid =uid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following_list, container, false);

        // Set the adapter

        recyclerView = view.findViewById(R.id.following_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("follow");
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    List<String> keys = new ArrayList<>();
                    for (DataSnapshot d:snapshot.getChildren()){
                        keys.add(d.getKey());
                    }
                    FolllowingAdapter adapter = new FolllowingAdapter(keys);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    class FolllowingAdapter extends RecyclerView.Adapter<FolllowingAdapter.MyViewHolder> {
        List<String> keys;

        public FolllowingAdapter(List<String> keys){
            this.keys = keys;

        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_list_item,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("follow");
            ref.child(uid).child(keys.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot snapshot) {
                    if (snapshot.exists()){

                        User profile = snapshot.getValue(User.class);
                        Picasso.with(getContext()).load(profile.getAvatar()).into(holder.avatar);
                        holder.textViewName.setText(profile.getUserName());
                        holder.textViewAddress.setText(profile.getAddress());
                        holder.layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), OtherProfileActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("UID", snapshot.getKey());
                                intent.putExtras(bundle);
                                getContext().startActivity(intent);
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return keys.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textViewName,textViewAddress,textViewCount;
            CircleImageView avatar;
            RelativeLayout layout;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                avatar = itemView.findViewById(R.id.following_avatar);
                textViewName = itemView.findViewById(R.id.following_name);
                textViewAddress = itemView.findViewById(R.id.following_address);
                textViewCount = itemView.findViewById(R.id.following_count);
                textViewCount.setText("1 follower");
                layout = itemView.findViewById(R.id.layout_follow_item);
            }
        }
    }
}
