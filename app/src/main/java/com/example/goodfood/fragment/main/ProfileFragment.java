package com.example.goodfood.fragment.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.goodfood.EditProfileActivity;
import com.example.goodfood.R;
import com.example.goodfood.SettingsActivity;
import com.example.goodfood.adapter.ProfileViewPagerAdapter;
import com.example.goodfood.model.User;
import com.example.goodfood.tablayout.FollowingFragment;
import com.example.goodfood.tablayout.RecipesFragment;
import com.example.goodfood.tablayout.SaveFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Ahihi";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int REP_LIST_ITEMS = 100;

    private Button button;
    private ImageView imageView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FollowingFragment followingFragment;
    private RecipesFragment recipesFragment;
    private SaveFragment saveFragment;

    private CircleImageView imageViewAvatar;
    private TextView textViewName,textViewAddress,textViewCount;


    private ProfileViewPagerAdapter adapter;
    private static final String[] TITLES = new String[]{"Recipes","Saved","Following"};
    public ProfileFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SmsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);
        viewPager = v.findViewById(R.id.pager_recipe);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String name = user.getDisplayName();
        Uri avatar = user.getPhotoUrl();
        imageViewAvatar = v.findViewById(R.id.avatar);
        textViewName = v.findViewById(R.id.name);
        Picasso.with(getContext())
                .load(avatar)
                .into(imageViewAvatar);
        textViewName.setText(name);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profile = snapshot.getValue(User.class);
                textViewAddress = v.findViewById(R.id.address);
                textViewAddress.setText(profile.getAddress());
                textViewCount = v.findViewById(R.id.txt_count);
                textViewCount.setText(profile.getLikes()+" like");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        button = v.findViewById(R.id.settings);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileFragment.this.getActivity(), SettingsActivity.class);
                    startActivity(intent);
                }
            });

        imageView = v.findViewById(R.id.edit_profile);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), EditProfileActivity.class));
                }
            });

        tabLayout = v.findViewById(R.id.Tab_Layout);
        recipesFragment = new RecipesFragment(user.getUid());
        saveFragment = new SaveFragment();
        followingFragment = new FollowingFragment(uid);
        tabLayout.setupWithViewPager(viewPager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(recipesFragment);
        fragments.add(saveFragment);
        fragments.add(followingFragment);
        List<String> titles = new ArrayList<>(Arrays.asList(TITLES));
        adapter = new ProfileViewPagerAdapter(getActivity().getSupportFragmentManager(),0,fragments,titles);
        viewPager.setAdapter(adapter);
        return v;
    }
}


