package com.example.androiddevassignment.model;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.example.androiddevassignment.network.neww.AuthRequest;
import com.example.androiddevassignment.network.neww.EmptyCallback;
import com.example.androiddevassignment.network.neww.Request;
import com.example.androiddevassignment.ConstVals;
import com.example.androiddevassignment.Database;
import com.example.androiddevassignment.network.neww.SyncMessage;
import com.example.androiddevassignment.MainActivity;
import com.example.androiddevassignment.network.neww.TokenResponse;
import com.example.androiddevassignment.network.old.GetBasesRequest;
import com.example.androiddevassignment.network.old.GetTokenRequest;
import com.example.androiddevassignment.network.old.OldRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class BusinessLogics {

    private static final String TAG = "BusinessLogics";

    private ArrayList<Database> databases;

    public ArrayList<Database> getDatabases(){
        return databases;
    }

    private int count;

    public boolean fetchDatabases(final MainActivity activity){
        Request request = new Request(ConstVals.serverAddress);
        if(!request.isValid()){
            return false;
        }
        String uuid = UUID.randomUUID().toString();
        request.getApi().requestDatabases(
                new SyncMessage(uuid,ConstVals.Action.RequestBases.getMessageType())
        ).enqueue(new EmptyCallback<Void>());
        count = 0;
        getDatabases(activity,request,uuid);
        return true;
    }
    private void getDatabases(final MainActivity activity, final Request request, final String uuid){
        request.getApi().getDatabases(
                new SyncMessage(uuid,ConstVals.Action.GetBases.getMessageType())
        ).enqueue(new Callback<List<SyncMessage>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<SyncMessage>> call, Response<List<SyncMessage>> response) {
                count++;
                if(count>6){
                    return;
                }
                if(response.body()==null||response.body().size()==0){
                    try {
                        Thread.sleep(500);
                    }catch (InterruptedException exc){
                        Log.e(TAG,Log.getStackTraceString(exc));
                    }
                    getDatabases(activity,request,uuid);
                    return;
                }
                int idx = parseDbData(response.body().get(0));
                activity.view.setIdx(idx);
                activity.view.fillDbData(activity,activity.model.getDatabases(),true);
                activity.view.requestLoginFocus();
            }
            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<SyncMessage>> call, Throwable t) {
                Log.e(TAG,Log.getStackTraceString(t));
            }
        });
    }
    private int parseDbData(SyncMessage msg){
        if(msg==null){
            databases = null;
            return -1;
        }
        databases = new ArrayList<>();
        try {
            return parseDbData(new JSONObject(msg.getMessage()));
        }catch(JSONException exc) {
            Log.e(TAG, Log.getStackTraceString(exc));
            databases = null;
            return -1;
        }
    }
    private int parseDbData(JSONObject data) throws JSONException{
        int idx = -1;
        JSONArray arr = data.getJSONArray("Databases");
        for(int i = 0;i<arr.length();i++){
            data = arr.getJSONObject(i);
            Database db = new Database(data.getString("DatabaseId"),data.getString("DisplayName"));
            if(data.getBoolean("IsDefault")){
                idx = i;
            }
            databases.add(db);
        }
        return idx;
    }

    public void login(MainActivity activity, String tenantId, String login, String password){
        Request request = new Request(ConstVals.serverAddress);
        String uuid = UUID.randomUUID().toString();
        request.getApi().requestToken(
                new SyncMessage(uuid,ConstVals.Action.RequestToken.getMessageType(),
                        new AuthRequest(tenantId,login,password))
        ).enqueue(new EmptyCallback<Void>());
        count = 0;
        getToken(activity, request, uuid);
    }
    private void getToken(final MainActivity activity, final Request request, final String uuid){
        request.getApi().getToken(
                new SyncMessage(uuid,ConstVals.Action.GetToken.getMessageType())
        ).enqueue(new Callback<List<SyncMessage>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<SyncMessage>> call, Response<List<SyncMessage>> response) {
                count++;
                if(count>6){
                    activity.view.showAuthFailureDialog(activity);
                    return;
                }
                if(response.body()==null||response.body().size()==0){
                    try {
                        Thread.sleep(500);
                    }catch (InterruptedException exc){
                        Log.e(TAG,Log.getStackTraceString(exc));
                    }
                    getToken(activity,request,uuid);
                    return;
                }
                onTokenReceived(activity,response.body().get(0));
            }
            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<SyncMessage>> call, Throwable t) {
                Log.e(TAG,Log.getStackTraceString(t));
            }
        });
    }
    private void onTokenReceived(MainActivity activity, SyncMessage msg){
        try {
            JSONObject obj = new JSONObject(msg.getMessage());
            if (obj.isNull("JwtToken")) {
                activity.view.showAuthFailureDialog(activity);
                return;
            }
            Gson gson = new Gson();
            obj = obj.getJSONObject("JwtToken");
            TokenResponse tokenData = gson.fromJson(obj.toString(),TokenResponse.class);
            activity.view.showTokenReceivedDialog(activity,tokenData);
        }catch(JSONException exc){
            Log.e(TAG,Log.getStackTraceString(exc));
        }
    }

    public void restoreInstanceState(Bundle savedInstanceState){
        if(savedInstanceState==null){
            return;
        }
        if(savedInstanceState.containsKey("databases")) {
            databases = (ArrayList<Database>)savedInstanceState.getSerializable("databases");
        }
    }
    public void saveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if(databases!=null) {
            outState.putSerializable("databases", databases);
        }
    }

    /**
     * Old methods
     **/

    public void fetchDatabasesOld(MainActivity activity){
        OldRequest request1 = new OldRequest(ConstVals.Action.RequestBases);
        request1.execute();
        OldRequest request2 = new GetBasesRequest(activity, ConstVals.Action.GetBases);
        request2.execute();
    }

    public void loginActionOld(MainActivity activity, String tenantId, String login, String password){
        OldRequest request1 = new OldRequest(ConstVals.Action.RequestToken);
        request1.execute(tenantId,login,password);
        OldRequest request2 = new GetTokenRequest(activity,ConstVals.Action.GetToken);
        request2.execute();
    }

    public int parseDbData(String s){
        if(s==null){
            databases = null;
            return -1;
        }
        databases = new ArrayList<>();
        try{
            JSONArray arr = new JSONArray(s);
            if(arr.length()==0){
                databases = null;
                return -1;
            }
            JSONObject data = arr.getJSONObject(0);
            data = new JSONObject(data.getString("Message"));
            return parseDbData(data);
        }catch(JSONException exc){
            Log.e(TAG,Log.getStackTraceString(exc));
            databases = null;
            return -1;
        }
    }
}
