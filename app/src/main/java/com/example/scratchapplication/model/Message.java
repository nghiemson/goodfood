package com.example.scratchapplication.model;

public class Message {
    private String message;
    private String idUserSend;
    private String idUserReceive;
    private Long timeZone;

    public Message(String message, String idUserSend, String idUserReceive, Long timeZone) {
        this.message = message;
        this.idUserSend = idUserSend;
        this.idUserReceive = idUserReceive;
        this.timeZone = timeZone;
    }

    public String getMessage() {
        return message;
    }

    public String getIdUserSend() {
        return idUserSend;
    }

    public String getIdUserReceive() {
        return idUserReceive;
    }

    public Long getTimeZone() {
        return timeZone;
    }
}
