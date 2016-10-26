package com.buffet.network;

import com.buffet.models.Branch;
import com.buffet.models.Promotion;

/**
 * Created by Tastomy on 10/20/2016 AD.
 */

public class ServerRequest {
    private String operation;
    private int pro_id;
    private int branch_id;
//    private User user;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setPromotionId(int pro_id){
        this.pro_id = pro_id;
    }

    public void setBranchId(int branch_id){
        this.branch_id = branch_id;
    }

//    public void setUser(User user) {
//        this.user = user;
//    }
}
