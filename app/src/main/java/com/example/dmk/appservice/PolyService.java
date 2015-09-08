package com.example.dmk.appservice;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by dmk on 07.09.15.
 */
public interface PolyService {

    @GET("/polyapi")
    Call<List<RestData>> somedata(@Query("getData") String key);


}
