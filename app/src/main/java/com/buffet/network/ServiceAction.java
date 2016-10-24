package com.buffet.network;

import com.buffet.models.Promotion;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Tastomy on 10/20/2016 AD.
 */

public interface ServiceAction {
    @GET("buffet/promotion.php") Call<ServerResponse> getPromotion();
}
