package com.example.goodfood.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.goodfood.R;
import com.example.goodfood.adapter.ProfileViewPagerAdapter;
import com.example.goodfood.model.User;
import com.example.goodfood.tablayout.FollowingFragment;
import com.example.goodfood.tablayout.RecipesFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FollowingFragment followingFragment;
    private RecipesFragment recipesFragment;
    private ProfileViewPagerAdapter adapter;

    private CircleImageView imageViewAvatar;
    private TextView textViewName,textViewAddress,textViewCount;
    private Button buttonFollow;

    private String uid;

    public OtherProfileFragment(String uid){
        this.uid = uid;
    }

    private static final String[] TITLES = new String[]{"Recipes","Following"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_other_profile, container,false);

        buttonFollow = v.findViewById(R.id.btn_follow);

        DatabaseReference followRef = FirebaseDatabase.getInstance().getReference("follow");
        followRef.child(FirebaseAuth.getInstance().getUid()).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    buttonFollow.setText("Following");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    final User user = snapshot.getValue(User.class);
                    imageViewAvatar = v.findViewById(R.id.iv_other_avatar);
                    textViewName = v.findViewById(R.id.tv_other_name);
                    textViewAddress = v.findViewById(R.id.tv_other_address);
                    textViewCount = v.findViewById(R.id.tv_other_count);

                    Picasso.with(getContext()).load(user.getAvatar()).into(imageViewAvatar);
                    textViewName.setText(user.getUserName());
                    textViewAddress.setText(user.getAddress());
                    textViewCount.setText(user.getLikes()+" likes");

                    buttonFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (buttonFollow.getText().equals("Follow")) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("follow");
                                reference.child(FirebaseAuth.getInstance().getUid()).child(uid).setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "Followed " + user.getUserName(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                buttonFollow.setText("Following");
                            }
                            else{
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("follow");
                                reference.child(FirebaseAuth.getInstance().getUid()).child(uid).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "Unfollowed "+user.getUserName(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                buttonFollow.setText("Follow");
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        viewPager = v.findViewById(R.id.pager_other_profile);
        tabLayout = v.findViewById(R.id.tab_other_profile);
        recipesFragment = new RecipesFragment(uid);
        followingFragment = new FollowingFragment(uid);
        tabLayout.setupWithViewPager(viewPager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(recipesFragment);
        fragments.add(followingFragment);
        List<String> titles = new ArrayList<>(Arrays.asList(TITLES));
        adapter = new ProfileViewPagerAdapter(getFragmentManager(),0,fragments,titles);
        viewPager.setAdapter(adapter);

        return v;
    }
}
