package com.buffet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by YaYaTripleSix on 23-Oct-16.
 */

public class Deal {

    @SerializedName("deal_id")
    @Expose
    private int dealId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("current_person")
    @Expose
    private int currentPerson;
    @SerializedName("deal_owner")
    @Expose
    private int dealOwner;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     *
     * @return
     * The dealId
     */
    public int getDealId() {
        return dealId;
    }

    /**
     *
     * @param dealId
     * The deal_id
     */
    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The time
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @param time
     * The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     *
     * @return
     * The currentPerson
     */
    public int getCurrentPerson() {
        return currentPerson;
    }

    /**
     *
     * @param currentPerson
     * The current_person
     */
    public void setCurrentPerson(int currentPerson) {
        this.currentPerson = currentPerson;
    }

    /**
     *
     * @return
     * The dealOwner
     */
    public int getDealOwner() {
        return dealOwner;
    }

    /**
     *
     * @param dealOwner
     * The deal_owner
     */
    public void setDealOwner(int dealOwner) {
        this.dealOwner = dealOwner;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

}
