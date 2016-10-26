package com.buffet.network;

import com.buffet.models.Branch;
import com.buffet.models.Deal;
import com.buffet.models.Promotion;
import com.buffet.models.User;

import java.util.List;

/**
 * Created by Tastomy on 10/20/2016 AD.
 */

public class ServerResponse {
    private String result;
    private String message;
    private User user;
    private List<Promotion> promotion;
    private List<Branch> branch;
    private List<Deal> deal;

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public List<Promotion> getPromotion(){
        return (List<Promotion>) promotion;
    }
    public List<Branch> getBranch(){
        return (List<Branch>) branch;
    }
    public List<Deal> getDeal(){
        return (List<Deal>) deal;
    }
    public User getUser() {
        return user;
    }
}
