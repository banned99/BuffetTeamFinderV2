package com.buffet.network;

import com.buffet.models.Promotion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Tastomy on 10/20/2016 AD.
 */

public interface ServiceAction {
    @GET("promotion.php") Call<ServerResponse> getPromotion();
    @POST("promotion.php") Call<ServerResponse> getSearchPromotion(@Body ServerRequest request);
    @POST("branch.php") Call<ServerResponse> getBranch(@Body ServerRequest request);
    @POST("deal.php") Call<ServerResponse> getDeal(@Body ServerRequest request);
    @POST("account.php") Call<ServerResponse> accountProcess(@Body ServerRequest request);
    @POST("deal.php") Call<ServerResponse> joinDeal(@Body ServerRequest request);
    @POST("deal.php") Call<ServerResponse> getDealJoined(@Body ServerRequest request);
    @POST("deal.php") Call<ServerResponse> getDealOwner(@Body ServerRequest request);
    @POST("deal.php") Call<ServerResponse> getDealMember(@Body ServerRequest request);
}
