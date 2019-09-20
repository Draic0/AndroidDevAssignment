package com.example.androiddevassignment.network.old;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.androiddevassignment.ConstVals.Action;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class OldRequest extends AsyncTask<String,Void,String> {

    private static final String TAG = "OldRequest";

    private static String requestId;

    private Action action;

    public OldRequest(@NonNull Action action){
        this.action = action;
    }

    @Override
    protected String doInBackground(String[] params) {
        URL url;
        HttpURLConnection connection;
        try {
            String requestMethod = action.getRequestMethod();
            if(requestId==null||requestMethod.equals("POST")){
                requestId = UUID.randomUUID().toString();
            }
            int count = 0;
            while(true) {
                url = new URL(action.getActionString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(requestMethod);
                connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");

                connection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                String requestString = buildRequestString(params);
                Log.d(TAG,requestString);
                wr.writeBytes(requestString);
                wr.flush();
                wr.close();

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Response Code: " + connection.getResponseCode());
                Log.d(TAG, "Response Message: " + connection.getResponseMessage());

                if (requestMethod.equals("GET")) {
                    if(!String.valueOf(responseCode).startsWith("2")){
                        return null;
                    }
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String input;
                    StringBuilder response = new StringBuilder();
                    while ((input = in.readLine()) != null) {
                        response.append(input);
                    }
                    in.close();
                    if(action==Action.GetToken&&response.toString().equals("[]")){
                        try {
                            Thread.sleep(500);
                        }catch(InterruptedException exc){
                            Log.e(TAG,Log.getStackTraceString(exc));
                        }
                        if(count==6){
                            return null;
                        }
                        count++;
                        continue;
                    }
                    Log.d(TAG, "Response: "+response.toString());
                    return response.toString();
                }
                break;
            }
        }catch(MalformedURLException exc){
        }catch(IOException exc){
            Log.e(TAG, Log.getStackTraceString(exc));
        }
        return null;
    }

    private String buildRequestString(String[] params){
        String tenantId = null, login = null, password = null;
        if(params.length>0){
            tenantId = params[0];
            login = params[1];
            password = params[2];
        }
        JSONObject message = null;
        try{
            message = new JSONObject();
            switch(action){
                case RequestBases:
                    break;
                case GetBases:
                    break;
                case RequestToken:
                    message.put("Login",login);//kmg04
                    message.put("Password",password);//kmg004
                    message.put("TenantId",tenantId);
                    break;
                case GetToken:
                    break;
            }
        }catch(JSONException exc){
            Log.e(TAG,Log.getStackTraceString(exc));
        }
        JSONObject o = new JSONObject();
        try{
            o.put("RequestId", requestId);
            o.put("MessageType",action.getMessageType());
            o.put("SettingsHash",0);
            o.put("Message",message.toString());
        }catch(JSONException exc){
            Log.e(TAG,Log.getStackTraceString(exc));
        }
        return o.toString();
    }
}
