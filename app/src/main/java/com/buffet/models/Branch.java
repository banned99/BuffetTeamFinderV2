package com.buffet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by YaYaTripleSix on 23-Oct-16.
 */

public class Branch {

    @SerializedName("branch_id")
    @Expose
    private int branchId;
    @SerializedName("branch_name")
    @Expose
    private String branchName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    /**
     *
     * @return
     * The branchId
     */
    public int getBranchId() {
        return branchId;
    }

    /**
     *
     * @param branchId
     * The branch_id
     */
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    /**
     *
     * @return
     * The branchName
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     *
     * @param branchName
     * The branch_name
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     *
     * @return
     * The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longtitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
