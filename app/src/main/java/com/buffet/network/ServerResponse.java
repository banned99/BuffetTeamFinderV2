package com.buffet.network;

import com.buffet.models.Promotion;

import java.util.List;

/**
 * Created by Tastomy on 10/20/2016 AD.
 */

public class ServerResponse {
    private String result;
    private String message;
//    private User user;
    private List<Promotion> promotion;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
    public List<Promotion> getPromotion(){
        return (List<Promotion>) promotion;
    }

//    public User getUser() {
//        return user;
//    }
}
