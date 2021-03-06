package com.buffet.network;

import com.buffet.models.Branch;
import com.buffet.models.Deal;
import com.buffet.models.Message;
import com.buffet.models.Promotion;
import com.buffet.models.User;

import java.util.List;

/**
 * Created by Tastomy on 10/20/2016 AD.
 */

public class ServerRequest {
    private String operation;
    private int pro_id;
    private int branch_id;
    private String search;
    private User user;
    private Deal deal;
    private Message message;


    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setPromotionId(int pro_id){
        this.pro_id = pro_id;
    }

    public void setSearch(String search){
        this.search = search;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDeal(Deal deal){ this.deal = deal;}

    public void setBranchId(int branch_id){
        this.branch_id = branch_id;
    }

    public void setMessage(Message message){
        this.message = message;
    }

}
