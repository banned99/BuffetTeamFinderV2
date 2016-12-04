package com.buffet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tastomy on 12/4/2016 AD.
 */

public class DealMember {
    private int status;
    @SerializedName("member_id")
    @Expose
    private int memberId;
    @SerializedName("deal_id")
    @Expose
    private int dealId;

    public int getMemberId() {
        return memberId;
    }
    public int getStatus() {
        return status;
    }
    public int getDealId() {
        return dealId;
    }

    public void setMemberId(int memberId) {
        this.memberId = this.memberId;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public void setDealId(int dealId) {
        this.dealId = dealId;
    }
}
