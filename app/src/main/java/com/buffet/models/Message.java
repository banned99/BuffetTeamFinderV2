package com.buffet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Belal on 5/29/2016.
 */
public class Message {
    @SerializedName("member_id")
    @Expose
    private int memberId;
    private String message;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    private String name;

    public Message(int memberId, String message, String name) {
        this.memberId = memberId;
        this.message = message;
        this.name = name;
    }


    public int getUsersId() {
        return memberId;
    }

    public String getMessage() {
        return message;
    }

    public String getcreateTime() {
        return createTime;
    }

    public String getName() {
        return name;
    }

    public void setUsersId() {
        this.memberId = memberId;
    }

    public void setMessage() {
        this.message = message;
    }

    public void setcreateTime() {
        this.createTime = createTime;
    }

    public void setName() {
        this.name = name;
    }
}
