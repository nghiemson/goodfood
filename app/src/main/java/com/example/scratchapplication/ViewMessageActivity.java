package com.example.scratchapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.scratchapplication.adapter.ChatAdapter;
import com.example.scratchapplication.api.JsonApi;
import com.example.scratchapplication.api.RestClient;
import com.example.scratchapplication.model.Message;
import com.example.scratchapplication.model.Profile;
import com.example.scratchapplication.model.ProfilePojo;
import com.example.scratchapplication.socketio.WebSocket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMessageActivity extends AppCompatActivity {
    public static String idChat = "";
    private RecyclerView recyclerViewMessage;
    private ChatAdapter adapter;

    private EditText editTextChat;
    private Button buttonChat;
    private Socket socket;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);
        String myId = FirebaseAuth.getInstance().getUid();
        toolbar = findViewById(R.id.messages_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String idReceive = intent.getStringExtra("idReceive");
        idChat = idReceive;
        String avatar = intent.getStringExtra("AVATAR");
        // truyen id nguoi dung len ma
        // co phai truyen id nguoi nhận đâu
        socket = WebSocket.getInstance();
        socket.connect();
        JsonObject enterUser = new JsonObject();
        enterUser.addProperty("uId", myId);
        Gson gson = new Gson();
        String json = gson.toJson(enterUser);
        try {
            JSONObject obj = new JSONObject(json);
            socket.emit("EnterUser", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        editTextChat = findViewById(R.id.edt_chatbox);
        buttonChat = findViewById(R.id.btn_chatbox_send);

        JsonApi service = RestClient.createService(JsonApi.class);
        service.getProfile(idReceive).enqueue(new Callback<ProfilePojo>() {
            @Override
            public void onResponse(Call<ProfilePojo> call, Response<ProfilePojo> response) {
                if (response.isSuccessful()){
                    Profile profile = response.body().getProfile();
                    toolbar.setTitle(profile.getUserName());
                }
            }

            @Override
            public void onFailure(Call<ProfilePojo> call, Throwable t) {

            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uId",FirebaseAuth.getInstance().getUid());
        jsonObject.addProperty("uIdReceive",idReceive);
        Call<JsonApi.MessagesPojo> call = service.chat(jsonObject);
        call.enqueue(new Callback<JsonApi.MessagesPojo>() {
            @Override
            public void onResponse(Call<JsonApi.MessagesPojo> call, Response<JsonApi.MessagesPojo> response) {
                if (!response.isSuccessful()){
                    Log.e("callChat", response.code()+" "+call.request().url().toString());
                    return;
                }
                recyclerViewMessage = findViewById(R.id.rv_message_list);
                recyclerViewMessage.setLayoutManager(new LinearLayoutManager(ViewMessageActivity.this));
                JsonApi.DataMessages dataMessages = response.body().getDataMessages();
                List<Message> messageList = new ArrayList<>();
                if (dataMessages!=null &&dataMessages.getMessages() != null)
                    messageList = response.body().getDataMessages().getMessages();
                adapter = new ChatAdapter(messageList, ViewMessageActivity.this, avatar);
                recyclerViewMessage.setAdapter(adapter);
                recyclerViewMessage.scrollToPosition(messageList.size()-1);

                socket.on("receivedMessage", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject data = (JSONObject) args[0];
                                String text = data.optString("message");
                                String idUserSend = data.optString("idUserSend");
                                String idUserReceive = data.optString("idUserReceive");
                                Long timeZone = Long.parseLong(data.optString("timeZone"));
                                Message message = new Message(text, idUserSend, idUserReceive, timeZone);
                                Log.e("on revc", message.getIdUserSend()+" to "+ message.getIdUserReceive());
                                adapter.addMessage(message);
                                recyclerViewMessage.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerViewMessage.smoothScrollToPosition(adapter.getItemCount()-1);
                                    }
                                });
                            }
                        });
                    }
                });

                buttonChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = editTextChat.getText().toString().trim();
                        if (!text.equals("")){
                            editTextChat.setText("");
                            JsonObject data = new JsonObject();
                            data.addProperty("to",idReceive);
                            data.addProperty("from",myId);
                            data.addProperty("message", text);
                            data.addProperty("idMessage",response.body().getDataMessages().getIdMessage());
                            Gson gson = new Gson();
                            String json = gson.toJson(data);
                            try {
                                JSONObject obj = new JSONObject(json);
                                socket.emit("MessageNews",obj);
                                Message message = new Message(text,myId,idReceive, new Long(0));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonApi.MessagesPojo> call, Throwable t) {
                Log.e("failChat", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        idChat ="";
        socket.disconnect();
    }
}