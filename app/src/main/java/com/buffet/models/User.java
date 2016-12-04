package com.buffet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tastomy on 10/27/2016 AD.
 */

public class User {
    private String name;
    private String email;
    private String tel;
    @SerializedName("member_id")
    @Expose
    private int memberId;
    private String password;
    private String old_password;
    private String new_password;
    private String fbid;
    @SerializedName("gcm_token")
    @Expose
    private String gcmToken;

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getFbid() {
        return fbid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getTel(){
        return tel;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

}
