package com.example.androiddevassignment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

            AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.AlertDialog);
            View v = activity.getLayoutInflater().inflate(R.layout.dialog_layout,null);
            final EditText et = v.findViewById(R.id.token);
            et.setText(accessToken, TextView.BufferType.EDITABLE);
            TextView tv = v.findViewById(R.id.token_type);
            tv.setText(tokenType);
            tv = v.findViewById(R.id.expires);
            tv.setText(String.valueOf(expiresIn));
            tv = v.findViewById(R.id.refresh_token);
            tv.setText(refreshToken);
            Button copy = v.findViewById(R.id.copy);
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyToClipboard(et.getText().toString());
                }
            });
            builder.setView(v).setPositiveButton("OK",null)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            activity = null;
                        }
                    }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            activity = null;
                        }
                    }).create().show();
        }catch(JSONException exc){
            Log.e(TAG,Log.getStackTraceString(exc));
            activity = null;
        }
    }

    private void showAuthFailure(){
        AlertDialog.Builder ab = new AlertDialog.Builder(activity,R.style.AlertDialog);
        ab.setMessage(R.string.auth_failure).setPositiveButton("OK",null)
                .create().show();
    }

    private void copyToClipboard(String text){
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("AccessToken", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(activity,R.string.text_copied,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        activity = null;
    }
}
