package com.buffet.models;

/**
 * Created by Belal on 5/29/2016.
 */
public class Message {
    private int usersId;
    private String message;
    private String sentAt;
    private String name;

    public Message(int usersId, String message, String name) {
        this.usersId = usersId;
        this.message = message;
        this.name = name;
    }


    public int getUsersId() {
        return usersId;
    }

    public String getMessage() {
        return message;
    }

    public String getSentAt() {
        return sentAt;
    }

    public String getName() {
        return name;
    }
}
