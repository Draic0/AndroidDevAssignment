package com.example.androiddevassignment.network.old;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.androiddevassignment.ConstVals;
import com.example.androiddevassignment.MainActivity;
import com.example.androiddevassignment.view.DialogView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetTokenRequest extends OldRequest {

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
            activity.view.showAuthFailureDialog(activity);
            activity = null;
            return;
        }
        try {
            JSONArray arr = new JSONArray(s);
            JSONObject obj = arr.getJSONObject(0);
            obj = new JSONObject(obj.getString("Message"));
            if(obj.isNull("JwtToken")){
                activity.view.showAuthFailureDialog(activity);
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

    @Override
    protected void onCancelled() {
        super.onCancelled();
        activity = null;
    }
}
