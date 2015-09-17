package com.example.dmk.appservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dmk on 09.09.15.
 */
public class StatData {
    @SerializedName("status")
    public String mStatus;

    @SerializedName("error")
    public String mError;

    @SerializedName("data")
    public String mData;

    public StatData(){

    }
}
