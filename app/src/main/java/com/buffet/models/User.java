package com.buffet.models;

/**
 * Created by Tastomy on 10/27/2016 AD.
 */

public class User {
    private String name;
    private String email;
    private String tel;
    private String member_id;
    private String password;
    private String old_password;
    private String new_password;
    private String fbid;

    public String getFbid() {
        return fbid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMember_id() {
        return member_id;
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
