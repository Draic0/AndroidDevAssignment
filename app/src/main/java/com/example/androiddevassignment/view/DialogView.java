package com.example.androiddevassignment.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddevassignment.R;

public class DialogView {

    private EditText token;
    private TextView tType, expires, rfrToken;
    private Button copy;
    private AlertDialog ad;

    public DialogView(final Activity activity, String accessToken, String tokenType, int expiresIn, String refreshToken){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialog);
        View v = activity.getLayoutInflater().inflate(R.layout.dialog,null);
        token = v.findViewById(R.id.token);
        token.setText(accessToken, TextView.BufferType.EDITABLE);
        tType = v.findViewById(R.id.token_type);
        tType.setText(tokenType);
        expires = v.findViewById(R.id.expires);
        expires.setText(String.valueOf(expiresIn));
        rfrToken = v.findViewById(R.id.refresh_token);
        rfrToken.setText(refreshToken);
        copy = v.findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(activity, token.getText().toString());
            }
        });
        ad = builder.setView(v).setPositiveButton("OK",null).create();
    }

    public AlertDialog get(){
        return ad;
    }

    private void copyToClipboard(Context context, String text){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("AccessToken", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context,R.string.text_copied,Toast.LENGTH_LONG).show();
    }
}
