package com.example.dmk.appservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dmk on 07.09.15.
 */
public class RestData {
    @SerializedName("key")
    public String mKey;

    @SerializedName("value")
    public String mValue;

    public RestData(){

    }
}
