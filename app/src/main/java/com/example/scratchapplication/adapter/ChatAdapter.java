package com.example.scratchapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scratchapplication.R;
import com.example.scratchapplication.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private List<Message> mMessageList;
    private String profileAvatar;
    private Context mContext;

    public ChatAdapter(List<Message> mMessageList, Context mContext, String profileAvatar) {
        this.mMessageList = mMessageList;
        this.mContext = mContext;
        this.profileAvatar = profileAvatar;

    }

    @Override
    public int getItemViewType(int position) {
        String myId = FirebaseAuth.getInstance().getUid();
        Message message = mMessageList.get(position);

        if (message.getIdUserSend().equals(myId)){
            return VIEW_TYPE_MESSAGE_SENT;
        }
        else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
        //return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Log.e("type", viewType+"");
        if (viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent,parent,false);
            return new SentMessageHolder(view);
        }
        else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    class SentMessageHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage;
        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.txt_message_sent);
        }
        void bind(Message message){
            textViewMessage.setText(message.getMessage());
        }
    }
    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageViewProfile;
        private TextView textViewMessage;
        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.img_message_profile);
            textViewMessage = itemView.findViewById(R.id.txt_message_received);
        }
        void bind(Message message){
            if (!profileAvatar.equals(""))
                Picasso.with(mContext).load(profileAvatar).into(imageViewProfile);
            textViewMessage.setText(message.getMessage());
        }
    }
    public void addMessage(Message message){
        mMessageList.add(message);
        notifyItemInserted(mMessageList.size()-1);
    }
}
