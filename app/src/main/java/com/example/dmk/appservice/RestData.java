package com.example.dmk.appservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dmk on 07.09.15.
 */
public class RestData {
    @SerializedName("taskID")
    public String dTaskID;

    @SerializedName("title")
    public String dTitle;

    @SerializedName("descr")
    public String dDescr;

    @SerializedName("latitude")
    public String dLatitude;

    @SerializedName("longitude")
    public String dLongitude;

    @SerializedName("timest")
    public String dTimest;

    public RestData(){

    }
}
