package com.example.androiddevassignment.network.neww;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Request {

    private static final String TAG = "Request";

    private DocsVisionApi api;
    private Retrofit retrofit;

    private static final String DEFAULT_BASE_URL = "http://almaz3.digdes.com";

    private boolean valid = false;

    public Request(String baseUrl){
        init(baseUrl);
    }

    private void init(String baseUrl){
        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl == null || baseUrl.length() == 0 ? DEFAULT_BASE_URL : baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(DocsVisionApi.class);
        }catch(IllegalArgumentException exc){
            Log.e(TAG,Log.getStackTraceString(exc));
            return;
        }
        valid = true;
    }

    public DocsVisionApi getApi(){
        return api;
    }

    public boolean isValid(){
        return valid;
    }

}
