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

    @GET("/polyapi")
    Call<List<RestData>> somedata(@Query("getData") String key);

    @FormUrlEncoded
    @POST("/polyapi/?regLocations=1")
    Call<String> regLocations(@Field("courierID") String cID, @Field("latitude") String latL, @Field("longitude") String longL, @Field("timest") String tst);

}
