package com.example.androiddevassignment.network.neww;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DocsVisionApi {

    String contentTypeHeader = "Content-Type: application/json; charset=UTF-8";

    @Headers(contentTypeHeader)
    @POST("SyncWebService/Api/SyncMessage/PostMessage")
    Call<Void> requestDatabases(@Body SyncMessage msg);
    @Headers(contentTypeHeader)
    @POST("SyncWebService/Api/SyncMessage/GetMessages")
    Call<List<SyncMessage>> getDatabases(@Body SyncMessage msg);
    @Headers(contentTypeHeader)
    @POST("SyncWebService/Api/SyncMessage/PostAuthMessage")
    Call<Void> requestToken(@Body SyncMessage msg);
    @Headers(contentTypeHeader)
    @POST("SyncWebService/Api/SyncMessage/GetAuthMessages")
    Call<List<SyncMessage>> getToken(@Body SyncMessage msg);

}
