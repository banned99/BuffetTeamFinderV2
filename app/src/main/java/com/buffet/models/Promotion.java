package com.buffet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by YaYaTripleSix on 23-Oct-16.
 */

public class Promotion {
    @SerializedName("pro_id")
    @Expose
    private int proId;
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
    private int maxPerson;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("cat_name")
    @Expose
    private String cat_name;
    @SerializedName("cat_id")
    @Expose
    private int cat_id;

    /**
     *
     * @return
     * The proId
     */
    public int getProId() {
        return proId;
    }

    /**
     *
     * @param proId
     * The pro_id
     */
    public void setProId(int proId) {
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
    public int getMaxPerson() {
        return maxPerson;
    }

    /**
     *
     * @param maxPerson
     * The max_person
     */
    public void setMaxPerson(int maxPerson) {
        this.maxPerson = maxPerson;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    /**
     *
     * @return
     * The cat_id
     */
    public int getCatId() {
        return cat_id;
    }

    /**
     *
     * @param cat_id
     * The pro_id
     */
    public void setCatId(int cat_id) {
        this.cat_id = cat_id;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */

    public void setDescription(String description) {
        this.description = description;
    }
    /**
     *
     * @return
     * The cat_name
     */
    public String getCatName() {
        return cat_name;
    }

    /**
     *
     * @param cat_name
     * The cat_name
     */
    public void setCatName(String cat_name) {
        this.cat_name = cat_name;
    }
}
