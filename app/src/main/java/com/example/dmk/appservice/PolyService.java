package com.example.dmk.appservice;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by dmk on 07.09.15.
 */
public interface PolyService {

    @GET("/polyapi/?action=getTasks")
    Call<List<RestData>> getTasks(@Query("cToken") String cToken);

    @FormUrlEncoded
    @POST("/polyapi/?action=regLocations")
    Call<StatData> regLocations(@Field("courierID") String cID, @Field("latitude") String latL, @Field("longitude") String longL, @Field("timest") String tst);

    @GET("/polyapi/?action=authCourier")
    Call<StatData> authCourier(@Query("cLogin") String cLogin, @Query("cPass") String cPass);

}
