package com.example.androiddevassignment.model;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.example.androiddevassignment.ConstVals;
import com.example.androiddevassignment.Database;
import com.example.androiddevassignment.network.GetBasesRequest;
import com.example.androiddevassignment.network.GetTokenRequest;
import com.example.androiddevassignment.MainActivity;
import com.example.androiddevassignment.network.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusinessLogics {

    private static final String TAG = "BusinessLogics";

    private ArrayList<Database> databases;

    public ArrayList<Database> getDatabases(){
        return databases;
    }

    public void fetchDataBases(MainActivity activity){
        Request request1 = new Request(ConstVals.Action.RequestBases);
        request1.execute();
        Request request2 = new GetBasesRequest(activity, ConstVals.Action.GetBases);
        request2.execute();
    }

    public int parseDbData(String s){
        if(s==null){
            databases = null;
            return -1;
        }
        databases = new ArrayList<>();
        int idx = -1;
        try{
            JSONArray arr = new JSONArray(s);
            if(arr.length()==0){
                databases = null;
                return -1;
            }
            JSONObject data = arr.getJSONObject(0);
            data = new JSONObject(data.getString("Message"));
            arr = data.getJSONArray("Databases");
            for(int i = 0;i<arr.length();i++){
                data = arr.getJSONObject(i);
                Database db = new Database(data.getString("DatabaseId"),data.getString("DisplayName"));
                if(data.getBoolean("IsDefault")){
                    idx = i;
                }
                databases.add(db);
            }
        }catch(JSONException exc){
            Log.e(TAG,Log.getStackTraceString(exc));
            databases = null;
            return -1;
        }
        return idx;
    }

    public void loginAction(MainActivity activity, String tenantId, String login, String password){
        Request request1 = new Request(ConstVals.Action.RequestToken);
        request1.execute(tenantId,login,password);
        Request request2 = new GetTokenRequest(activity,ConstVals.Action.GetToken);
        request2.execute();
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
}
