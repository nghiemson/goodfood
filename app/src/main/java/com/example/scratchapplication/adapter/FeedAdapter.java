package com.example.scratchapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.MainActivity;
import com.example.scratchapplication.OtherProfileActivity;
import com.example.scratchapplication.R;
import com.example.scratchapplication.ViewRecipeActivity;
import com.example.scratchapplication.api.JsonApi;
import com.example.scratchapplication.api.RestClient;
import com.example.scratchapplication.model.Like;
import com.example.scratchapplication.model.Profile;
import com.example.scratchapplication.model.Save;
import com.example.scratchapplication.model.home.ModelRecipe;
import com.example.scratchapplication.room.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {
    private Context context;
    private List<ModelRecipe> list;
    private String myUid;
    private List<String> followedList;
    private List<String> savedList;
    private List<String> filter;
    private boolean filterFollow;
    private boolean orderByLike;
    private ProfileViewModel profileViewModel;

    public FeedAdapter(Context context, List<ModelRecipe> list, String myUid, List<String> followedList, List<String> savedList) {
        this.context = context;
        this.list = list;
        this.myUid = myUid;
        this.followedList = followedList;
        this.savedList = savedList;
        profileViewModel = ViewModelProviders.of((FragmentActivity) context).get(ProfileViewModel.class);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelRecipe model = list.get(position);
        profileViewModel.getProfileAdapter(model.getUid()).observe((FragmentActivity) context, new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                holder.textViewProfileName.setText(profile.getUserName());

                Picasso.with(context)
                        .load(profile.getAvatar())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.imageViewAvatar, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(context).load(model.getProfileAvatar()).into(holder.imageViewAvatar);
                            }
                        });
            }
        });
        holder.textViewRecipeName.setText(model.getName());
        holder.textViewRecipeDesc.setText(model.getDescription());
        Picasso.with(context)
                .load(model.getUrlCover())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.imageViewCover, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        Picasso.with(context).load(model.getUrlCover()).into(holder.imageViewCover);
                    }
                });
        //like count
        holder.textViewLikeCount.setText(String.valueOf(model.getLike()));
        if (model.getUsersLike().contains(myUid)){
            holder.imageViewLike.setImageResource(R.drawable.ic_liked);
        }
        else {
            holder.imageViewLike.setImageResource(R.drawable.ic_like);
        }
        String txtLike = model.getUsersLike().size()>1?" likes":" like";
        holder.textViewLikeCount.setText(model.getUsersLike().size()+ txtLike);


        JsonApi api = RestClient.createService(JsonApi.class);
        //like
        holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UID", myUid);
                if (model.getUsersLike().contains(myUid)){
                    Picasso.with(context).load(R.drawable.ic_liked).into(holder.imageViewLike);
                    model.getUsersLike().remove(myUid);
                }
                else {
                    Picasso.with(context).load(R.drawable.ic_like).into(holder.imageViewLike);
                    model.getUsersLike().add(myUid);
                }
                holder.textViewLikeCount.setText(model.getUsersLike().size()+"");
                notifyItemChanged(position);
                Like like = new Like(model.getRid(),myUid);
                Call<Like> call = api.postLike(like);
                call.enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {
                        if (!response.isSuccessful()){
                            Log.e("Code", "" +response.code());
                            return;
                        }
                    }
                    @Override
                    public void onFailure(Call<Like> call, Throwable t) {
                    }
                });
            }
        });
        //save
        if (savedList.contains(model.getRid())){
            holder.buttonSave.setClickable(false);
            holder.buttonSave.setText("Saved");
        }
        else {
            holder.buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Save")
                            .setMessage("Lưu món ăn này?")
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Call<Save> call = api.saveRecipe(new Save(myUid, model.getRid()));
                                    call.enqueue(new Callback<Save>() {
                                        @Override
                                        public void onResponse(Call<Save> call, Response<Save> response) {
                                            if (response.isSuccessful()) {
                                                holder.buttonSave.setText("Saved");
                                                holder.buttonSave.setClickable(false);
                                                Toast.makeText(context, "Đã lưu " + model.getName(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Save> call, Throwable t) {

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });
        }
        //xem trang cac nhan
        holder.layoutTittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherProfileActivity.class);
                intent.putExtra("UID",model.getUid());
                context.startActivity(intent);
            }
        });
        //xem chi tiet
        holder.imageViewCover.setOnClickListener(viewDetailRecipe(model));
        holder.textViewRecipeName.setOnClickListener(viewDetailRecipe(model));
        holder.textViewRecipeDesc.setOnClickListener(viewDetailRecipe(model));

    }
    private View.OnClickListener viewDetailRecipe(ModelRecipe model){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewRecipeActivity.class);
                intent.putExtra("RID",model.getRid());
                intent.putExtra("UID",model.getUid());
                context.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutTittle;
        private ImageView imageViewAvatar, imageViewCover, imageViewLike;
        private TextView textViewProfileName, textViewRecipeName, textViewRecipeDesc, textViewLikeCount, textViewCmtCount;
        private Button buttonSave;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.image_avatar);
            imageViewCover = itemView.findViewById(R.id.image_cover);

            imageViewLike = itemView.findViewById(R.id.image_like);


            textViewProfileName = itemView.findViewById(R.id.txt_profile_name);
            textViewRecipeName = itemView.findViewById(R.id.txt_recipe_name);

            textViewRecipeDesc = itemView.findViewById(R.id.txt_recipe_desc);
            textViewLikeCount = itemView.findViewById(R.id.txt_like_count);

            buttonSave = itemView.findViewById(R.id.btn_save);

            textViewProfileName = itemView.findViewById(R.id.txt_profile_name);
            layoutTittle = itemView.findViewById(R.id.title_bar);
        }
    }
}
