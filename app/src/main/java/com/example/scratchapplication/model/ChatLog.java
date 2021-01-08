package com.example.scratchapplication.model;

import java.util.Comparator;

public class ChatLog implements Comparator<ChatLog> {
    private String id;
    private String avatar;
    private String name;
    private String lastMessage;
    private long timeZone;

    public ChatLog(String id, String avatar, String name, String lastMessage, long timeZone) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timeZone = timeZone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(long timeZone) {
        this.timeZone = timeZone;
    }

    public ChatLog() {
    }

    @Override
    public int compare(ChatLog o1, ChatLog o2) {
        return (int) (o2.getTimeZone()-o1.getTimeZone());
    }
}
