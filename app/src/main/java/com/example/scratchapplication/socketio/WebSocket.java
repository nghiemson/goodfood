package com.example.scratchapplication.socketio;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class WebSocket {
    private static final String TAG = Socket.class.getSimpleName();
    private static final String SOCKET_URL = "https://scratch-backend-app.herokuapp.com";
    private static final String SOCKET_URL_TEST = "http://localhost:8080";

    private static Socket instance;

    public static Socket getInstance(){
        if (instance==null){
            try{
                instance = IO.socket(SOCKET_URL);
            }catch (URISyntaxException e){
                e.printStackTrace();
            }
        }
        return instance;
    }
}
