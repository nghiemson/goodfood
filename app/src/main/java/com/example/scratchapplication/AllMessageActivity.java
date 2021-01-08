package com.example.scratchapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scratchapplication.model.ChatLog;
import com.example.scratchapplication.socketio.WebSocket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class AllMessageActivity extends AppCompatActivity {
    private Socket socket;
    private RecyclerView recyclerViewChatLog;
    private List<ChatLog> chatLogs;
    private EditText editTextSearch;
    private ChatLogAdapter adapter;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_message);
        //toolbar
        toolbar = findViewById(R.id.all_messages_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading messages...");
        progressDialog.show();
        //socket
        socket = WebSocket.getInstance();
        socket.connect();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uId", FirebaseAuth.getInstance().getUid());
        Gson gson = new Gson();
        try {
            JSONObject obj = new JSONObject(gson.toJson(jsonObject));
            socket.emit("GetListNewsMess",obj);
            socket.emit("EnterUser", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        socket.on("testLog", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                String object = (String) args[0];
//                Log.e("test log",object.toString());
//            }
//        });
        socket.on("receivedListMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        chatLogs = new ArrayList<>();
                        JSONArray arr = (JSONArray) args[0];
                        for (int i = 0; i<arr.length();i++) {
                            try {
                                JSONObject obj = arr.getJSONObject(i);
                                String jsonMessage = obj.optString("messages");
                                JSONObject objMessage = new JSONObject(jsonMessage);
                                ChatLog chatLog = new ChatLog(
                                        obj.optString("userId"),
                                        obj.optString("avatar"),
                                        obj.optString("userName"),
                                        objMessage.optString("message"),
                                        Long.parseLong(objMessage.optString("timeZone"))
                                );
                                Log.e("log", chatLog.getName());
                                chatLogs.add(chatLog);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Collections.sort(chatLogs,new ChatLog());
                        if (chatLogs.size()==0){
                            Toast.makeText(AllMessageActivity.this, "0 message is retrieved", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        recyclerViewChatLog = findViewById(R.id.rv_chat_profile_list);
                        recyclerViewChatLog.setLayoutManager(new LinearLayoutManager(AllMessageActivity.this));
                        adapter = new ChatLogAdapter(chatLogs);
                        recyclerViewChatLog.setAdapter(adapter);
                    }
                });
            }
        });
        editTextSearch=findViewById(R.id.edt_search_chat);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String textSearch = editTextSearch.getText().toString().trim();
                    List<ChatLog> newList = new ArrayList<>();
                    for (ChatLog chatLog : chatLogs) {
                        if (chatLog.getName().contains(textSearch)) {
                            newList.add(chatLog);
                        }
                    }
                    adapter = new ChatLogAdapter(newList);
                    recyclerViewChatLog.setAdapter(adapter);
                    return true;
                }
                return false;
            }
        });
    }
    class ChatLogAdapter extends RecyclerView.Adapter<ChatLogAdapter.ChatLogViewHolder> {
        List<ChatLog> chatLogList;

        public ChatLogAdapter(List<ChatLog> chatLogList) {
            this.chatLogList = chatLogList;
        }

        @NonNull
        @Override
        public ChatLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(AllMessageActivity.this).inflate(R.layout.item_profile_chat, parent, false);
            return new ChatLogViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatLogViewHolder holder, int position) {
            ChatLog chatLog = chatLogList.get(position);
            Picasso.with(AllMessageActivity.this).load(chatLog.getAvatar()).into(holder.imageViewAvatar);
            holder.textViewName.setText(chatLog.getName());
            holder.textViewLastMessage.setText(chatLog.getLastMessage());
            holder.layoutProfileChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AllMessageActivity.this, ViewMessageActivity.class);
                    intent.putExtra("idReceive", chatLog.getId());
                    intent.putExtra("AVATAR",chatLog.getAvatar());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return chatLogList.size();
        }

        class ChatLogViewHolder extends RecyclerView.ViewHolder {
            ConstraintLayout layoutProfileChat;
            CircleImageView imageViewAvatar;
            TextView textViewName, textViewLastMessage;
            public ChatLogViewHolder(@NonNull View itemView) {
                super(itemView);
                imageViewAvatar = itemView.findViewById(R.id.img_profile_chat);
                textViewLastMessage = itemView.findViewById(R.id.txt_last_message);
                textViewName = itemView.findViewById(R.id.txt_profile_chat);
                layoutProfileChat = itemView.findViewById(R.id.layout_chat_log);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}