package com.example.androiddevassignment.network;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.androiddevassignment.ConstVals;
import com.example.androiddevassignment.MainActivity;
import com.example.androiddevassignment.R;
import com.example.androiddevassignment.view.DialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetTokenRequest extends Request {

    private static final String TAG = "GetTokenRequest";

    private MainActivity activity;

    public GetTokenRequest(MainActivity activity, @NonNull ConstVals.Action action){
        super(action);
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s==null){
            showAuthFailure();
            activity = null;
            return;
        }
        try {
            JSONArray arr = new JSONArray(s);
            JSONObject obj = arr.getJSONObject(0);
            obj = new JSONObject(obj.getString("Message"));
            if(obj.isNull("JwtToken")){
                showAuthFailure();
                return;
            }
            obj = obj.getJSONObject("JwtToken");
            String accessToken = obj.getString("AccessToken");
            String tokenType = obj.getString("TokenType");
            int expiresIn = obj.getInt("ExpiresIn");
            String refreshToken = obj.getString("RefreshToken");
            new DialogView(activity,accessToken,tokenType,expiresIn,refreshToken).get().show();
        }catch(JSONException exc){
            Log.e(TAG,Log.getStackTraceString(exc));
        }
        activity = null;
    }

    private void showAuthFailure(){
        AlertDialog.Builder ab = new AlertDialog.Builder(activity, R.style.AlertDialog);
        ab.setMessage(R.string.auth_failure).setPositiveButton("OK",null)
                .create().show();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        activity = null;
    }
}
