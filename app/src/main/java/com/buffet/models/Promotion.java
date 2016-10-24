package com.buffet.models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by YaYaTripleSix on 23-Oct-16.
 */

public class Promotion {
    @SerializedName("pro_id")
    @Expose
    private Integer proId;
    @SerializedName("pro_name")
    @Expose
    private String proName;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("date_start")
    @Expose
    private String dateStart;
    @SerializedName("expire")
    @Expose
    private String expire;
    @SerializedName("max_person")
    @Expose
    private Integer maxPerson;

    private int image;

    /**
     *
     * @return
     * The proId
     */
    public Integer getProId() {
        return proId;
    }

    /**
     *
     * @param proId
     * The pro_id
     */
    public void setProId(Integer proId) {
        this.proId = proId;
    }

    /**
     *
     * @return
     * The proName
     */
    public String getProName() {
        return proName;
    }

    /**
     *
     * @param proName
     * The pro_name
     */
    public void setProName(String proName) {
        this.proName = proName;
    }

    /**
     *
     * @return
     * The price
     */
    public Double getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The dateStart
     */
    public String getDateStart() {
        return dateStart;
    }

    /**
     *
     * @param dateStart
     * The date_start
     */
    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    /**
     *
     * @return
     * The expire
     */
    public String getExpire() {
        return expire;
    }

    /**
     *
     * @param expire
     * The expire
     */
    public void setExpire(String expire) {
        this.expire = expire;
    }

    /**
     *
     * @return
     * The maxPerson
     */
    public Integer getMaxPerson() {
        return maxPerson;
    }

    /**
     *
     * @param maxPerson
     * The max_person
     */
    public void setMaxPerson(Integer maxPerson) {
        this.maxPerson = maxPerson;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}
