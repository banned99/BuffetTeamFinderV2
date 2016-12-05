package com.buffet.network;

import com.buffet.models.Branch;
import com.buffet.models.DealMember;
import com.buffet.models.Deal;
import com.buffet.models.Message;
import com.buffet.models.Promotion;
import com.buffet.models.User;

import java.util.List;

/**
 * Created by Tastomy on 10/20/2016 AD.
 */

public class ServerResponse {
    private String result;
    private String message;
    private List<User> userobj;
    private User user;
    private List<Promotion> promotion;
    private List<Branch> branch;
    private List<Deal> deal;
    private List<DealMember> dealMember;
    private List<Message> messages;

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
    public List<DealMember> getDealMember(){
        return (List<DealMember>) dealMember;
    }
    public User getUser() {
        return user;
    }
    public List<User> getListUser() {
        return (List<User>)userobj;
    }
    public List<Message> getMessages() {
        return (List<Message>)messages;
    }
}
